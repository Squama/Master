/**
 * 
 */
package com.radish.master.listener.project;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.system.Arith;
import com.radish.master.system.SpringUtil;

/**
 * @author tonyd
 *
 */
public class TeamSalaryTaskCompleteListener implements TaskListener{

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
            		salary.setStatus("40");
                }else if ("boss".equals(delegateTask.getTaskDefinitionKey())) {
                	salary.setStatus("50");
                } else if ("account".equals(delegateTask.getTaskDefinitionKey())) {
                	salary.setStatus("60");
                	//分配工资额度到对应工程量的人工支付里面
                	Arith ari = new Arith();
                	if(salary.getTotal()!=null&&!"无".equals(salary.getTotal())){//存在总额
                		if(Double.valueOf(salary.getTotal())>0.0&&Double.valueOf(salary.getApplySum())>0.0){//总额\申请总额大于0
                			//得出总额和申请总额的百分比*各个工程量人工。
                			BigDecimal b1 = new BigDecimal(Double.valueOf(salary.getTotal()));
                	        //BigDecimal b2 = new BigDecimal(Double.valueOf(salary.getApplySum()));
                	        
                	        List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where salary_id='"+businessKey+"'", SalaryVolume.class);
                	        for(SalaryVolume gx :gxs){//拿到所有工程量
            					ProjectVolume gcl = baseService.get(ProjectVolume.class, gx.getVolumeID());
            					BigDecimal b2 = new BigDecimal(Double.valueOf(gcl.getFinalLabour()));
            					Double bfb = b2.divide(b1, 2,BigDecimal.ROUND_HALF_UP).doubleValue();
            					Double rgje = ari.mul(bfb, Double.valueOf(salary.getApplySum()));
            					VolumePay zf = new VolumePay();
            					zf.setIsjz("1");
            					zf.setVolumeId(gcl.getId());
            					zf.setPayType("10");
            					zf.setPayWay("10");
            					zf.setPayMoney(rgje.toString());
            					zf.setRgmoney(rgje.toString());
            					zf.setClmoney("0");
            					zf.setJxmoney("0");
            					zf.setDepartment(gcl.getProjectName()+"（工资单付款数据）");
            					zf.setContent("工资单审核完成自动分配金额,工资id:"+businessKey);
            					zf.setCreateDate(new Date());
            					zf.setStatus("30");
            					if(gcl.getLaborID()!=null){
            						Labor ht = baseService.get(Labor.class, gcl.getLaborID());
            						if(ht!=null){
            							zf.setSkf(ht.getConstructionTeam());
            						}
            					}
            					baseService.save(zf);
            					gcl.setLabourStatus("20");
            					baseService.update(gcl);
            				}
                		}
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
