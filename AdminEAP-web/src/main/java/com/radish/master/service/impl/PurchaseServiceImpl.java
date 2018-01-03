/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Purchase;
import com.radish.master.pojo.MatMap;
import com.radish.master.pojo.Options;
import com.radish.master.service.PurchaseService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月22日    Create this file
* </pre>
* 
*/
@Service("purchaseService")
public class PurchaseServiceImpl extends BaseServiceImpl implements PurchaseService {
    
    @Resource
    private RuntimePageService runtimePageService;

    @Override
    public List<Options> getProjectCombobox() {
        return this.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getBudgetCombobox() {
        return this.findMapBySql("select budget_no value, budget_name data from tbl_budget", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getBudgetComboboxByProject(String projectID) {
        return this.findMapBySql("select budget_no value, budget_name data from tbl_budget where project_id=?", new Object[]{projectID}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getRegionComboboxByBudgetNo(String budgetNo) {
        return this.findMapBySql("select region_code value, region_name data from tbl_budget_tx where budget_no=? and region_code is not null", new Object[]{budgetNo}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public MatMap getMatMap() {
        List<Materiel> list = this.find("from Materiel where isValid = 1");
        Map<String, Materiel> map = new HashMap<String, Materiel>();
        for(Materiel mat : list){
            map.put(mat.getMat_number(), mat);
        }
        MatMap matMap = new MatMap();
        matMap.setMap(map);
        
        return matMap;
    }

    @Override
    public Budget getBudgetByNo(String budgetNo){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.get("from Budget where budgetNo=:budgetNo", params);
    }

    @Override
    public Result startPurchaseApplyFlow(Purchase purchase, String processDefinitionKey) {
        purchase.setStatus("20");
        purchase.setUpdateDateTime(new Date());
        
        this.update(purchase);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "申请预算用料：" + purchase.getBudgetNo() + "，所属项目：" + purchase.getProjectID();
        
        //businessKey
        String businessKey = purchase.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("isAudit", "true");
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables,
                user.getId(), businessKey);
    }
}
