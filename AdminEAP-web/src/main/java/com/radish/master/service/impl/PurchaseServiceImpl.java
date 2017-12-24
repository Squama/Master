/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
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

}
