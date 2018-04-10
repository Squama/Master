/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.radish.master.pojo.Options;
import com.radish.master.service.MechService;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月9日    Create this file
 * </pre>
 * 
 */
@Service("mechService")
public class MechServiceImpl extends BaseServiceImpl implements MechService {

    @Override
    public List<Options> getMechComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, name data from tbl_mech_budget where project_id=? AND status=30", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getConsumeCombobox(String mechID) {
        return this.findMapBySql("select id value, name data from tbl_mech_budget_detail where entry_id=?", new Object[] { mechID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

}
