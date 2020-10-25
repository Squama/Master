package com.radish.master.listener.fixedassets;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.fixedassets.FixedAssetsPay;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.system.Arith;
import com.radish.master.system.SpringUtil;

public class FixPayTaskCompleteListener implements TaskListener {

	private static final long serialVersionUID = -200919250195337005L;
    
    @Override
    public void notify(DelegateTask delegateTask) {
    	Arith ari = new Arith();
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             FixedAssetsPay zf = baseService.get("from FixedAssetsPay where id=:id", params);
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 zf.setStatus("30");
             }else if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 String suggestion = delegateTask.getVariable("suggestion").toString();
            	 zf.setStatus("40");
            	 zf.setRebutReason(suggestion);
             }
             baseService.update(zf);
        }
    }
}
