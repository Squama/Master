/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.project;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.project.Measure;
import com.radish.master.entity.project.MeasureConsume;
import com.radish.master.entity.project.Salary;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年5月8日    Create this file
* </pre>
* 
*/

public class HodSalaryTaskCompleteListener implements TaskListener{
    private static final long serialVersionUID = 8633012790450639220L;

    private static final String TRUE = "true";

    private static final String FALSE = "false";

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            Salary salary = baseService.get(Salary.class, businessKey);

            String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
            Map<String, Object> suggestionParams = new HashMap<>();
            suggestionParams.put("businessKey", businessKey);
            suggestionParams.put("taskNode", taskDefinitionKey);
            ActivitiSuggestion as = baseService.get(suggestionHql, suggestionParams);
            
            if(as == null){
                as = new ActivitiSuggestion();
                as.setCreateDateTime(new Date());
                as.setUpdateDateTime(new Date());
                as.setBusinessKey(businessKey);
                as.setTaskNode(taskDefinitionKey);
                as.setApprove("true");
            }
            
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                salary.setStatus("70");
                as.setApprove("false");
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("fuzeren".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("30");
                } else if ("bangongshi".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("40");
                } else if ("boss".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("50");
                } else if ("account".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("60");
                    
                    StringBuilder buffer = new StringBuilder();

                    buffer.append("SELECT id,project_id,project_sub_id,SUM(construct) AS construct, ");
                    buffer.append("create_date_time,deleted,update_date_time,version,name,project_name,project_sub_name,manage,issue,rule,tax,status ");
                    buffer.append("FROM tbl_measure   ");
                    buffer.append("WHERE project_id=:projectID  GROUP BY project_sub_id ");

                    Map<String, Object> params = new HashMap<String, Object>();

                    params.put("projectID", salary.getProjectID());

                    List<Measure> measureList =  baseService.findBySql(buffer.toString(), params, Measure.class);
                    
                    BigDecimal sum = new BigDecimal("0");
                    for(Measure measure : measureList){
                        sum = sum.add(new BigDecimal(measure.getConstruct()));
                    }
                    
                    for(Measure measure : measureList){
                        
                        BigDecimal consume = new BigDecimal(measure.getConstruct());
                        BigDecimal result = new BigDecimal("0");
                        BigDecimal sumSalary = new BigDecimal(salary.getCostSum());
                        
                        result = consume.divide(sum, 2, BigDecimal.ROUND_HALF_DOWN);
                        
                        NumberFormat percent = NumberFormat.getPercentInstance();
                        percent.setMaximumFractionDigits(2);
                        
                        MeasureConsume mc = new MeasureConsume();
                        mc.setCreateDateTime(new Date());
                        mc.setConsumeName("门卫、机修人员工资，按比例消耗，申请时比例：" + percent.format(result.doubleValue()));
                        mc.setProjectID(salary.getProjectID());
                        mc.setProjectSubID(measure.getProjectSubID());
                        mc.setOperator(SecurityUtil.getUser().getName());
                        mc.setOperateTime(new Date());
                        mc.setType("construct");
                        mc.setAmount(sumSalary.multiply(result).setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString());
                        mc.setOp("-");
                        baseService.save(mc);
                    }
                    
                    
                }
            }

            baseService.save(salary);
            as.setSuggestion(suggestion);
            as.setOperator(SecurityUtil.getUser().getName());
            as.setUpdateDateTime(new Date());
            baseService.save(as);

        }
        
    }
}
