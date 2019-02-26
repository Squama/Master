/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.listener.fixedassets;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.fixedassets.FixedAssetsPur;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年2月26日    Create this file
* </pre>
* 
*/

public class FixedAssetsEndListener implements ExecutionListener{

    private static final long serialVersionUID = 8824082869864706275L;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessKey = execution.getVariable(Constants.VAR_BUSINESS_KEY).toString();
        
        BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

        FixedAssetsPur fixedAssetsPur = baseService.get(FixedAssetsPur.class, businessKey);
        
        fixedAssetsPur.setStatus("40");
        
        baseService.save(fixedAssetsPur);
    }
    
}
