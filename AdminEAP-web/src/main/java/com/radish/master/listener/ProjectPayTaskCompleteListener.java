/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.UserLeave;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.volumePay.ProjectPay;
import com.radish.master.entity.volumePay.ProjectPayDet;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.system.Arith;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月13日    Create this file
* </pre>
* 
*/

public class ProjectPayTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = 4294247719992051739L;

    @Override
    public void notify(DelegateTask delegateTask) {
    	
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             ProjectPay zf = baseService.get("from ProjectPay where id=:id", params);
             
             zf.setStatus("30");
            
             boolean flag = true;
             
             //支付明细状态修改
             List<ProjectPayDet> mxs = baseService.find(" from ProjectPayDet where projectPayId='"+businessKey+"'");
             for(ProjectPayDet mx :mxs){
            	 if(mx.getFk()!=null&&Double.valueOf(mx.getFk())==0){//本次不付款，状态改成完成
            		 mx.setStatus("30");
            	 }else{
            		 flag = false;
            		 mx.setStatus("20");
            	 }
            	 baseService.update(mx);
             }
             if(flag){
            	 zf.setStatus("50");
             }
             baseService.update(zf);
             
        }

    }
    
}
