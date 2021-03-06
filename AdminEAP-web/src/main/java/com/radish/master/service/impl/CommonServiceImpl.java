/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.radish.master.pojo.Options;
import com.radish.master.service.CommonService;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月27日    Create this file
 * </pre>
 * 
 */
@Service("commonService")
public class CommonServiceImpl extends BaseServiceImpl implements CommonService {

    @Override
    public List<Options> getProjectCombobox() {
        return this.findMapBySql("select id value, project_name data from tbl_project", new Object[] {}, new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getDeptCombobox() {
        return this.findMapBySql("select code value, name data from tbl_org", new Object[] {}, new Type[] {}, Options.class);
    }
    
    @Override
    public List<Options> getAssetsCombobox(String type) {
        return this.findMapBySql("select id value, name data from tbl_fixedassets_stk where fa_type=?", new Object[] {type}, new Type[] {StringType.INSTANCE}, Options.class);
    }
    @Override
    public List<Options> getAssetsComboboxAndStk(String type) {
        return this.findMapBySql("select id value, CONCAT_WS('-',name,belonged_stock) data from tbl_fixedassets_stk where fa_type=?", new Object[] {type}, new Type[] {StringType.INSTANCE}, Options.class);
    }
    
    @Override
    public List<Options> getAssetsCombobox() {
        return this.findMapBySql("select id value, name data from tbl_fixedassets_stk", new Object[] {}, new Type[] {}, Options.class);
    }
    
    @Override
    public List<Options> getEducationCombobox() {
        return this.findMapBySql("select code value, name data from tbl_dict where parent_id = '402881f053cd670d0153cd6d6be40000' ", new Object[] {},
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getEthnicCombobox() {
        return this.findMapBySql("select code value, name data from tbl_dict where parent_id = '402881f053e55e710153e570c0890001'", new Object[] {},
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getFeeCombobox() {
        return this.findMapBySql("select remark value, name data from tbl_dict where parent_id = '40280cac5f2edcd6015f2ee042510099'", new Object[] {},
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getFeeManageCombobox() {
        return this.findMapBySql("select remark value, name data from tbl_dict where parent_id = '40280cac5f2edcd6015f2ee042510098'", new Object[] {},
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getDepartmentCombobox() {
        return this.findMapBySql("select id value, name data from tbl_org", new Object[] {}, new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getUserCombobox() {
        return this.findMapBySql("select id value, name data from tbl_user where audit_status = 10", new Object[] {}, new Type[] { StringType.INSTANCE },
                Options.class);
    }

    @Override
    public List<Options> getWorkerCombobox() {
        return this.findMapBySql("select id value, name data from tbl_worker", new Object[] {}, new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getTeamComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, team_name data from tbl_project_team where project_id=? AND status != '30'", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    // 首字母大写
    @Override
    public String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

	@Override
	public List<Dict> getRegionCode() {
		String hql = "from Dict where code like 'regionCode%' and code<>'regionCode'  order by levelCode asc";
        List<Dict> dicts = this.find(hql);
        return dicts;
	}
}
