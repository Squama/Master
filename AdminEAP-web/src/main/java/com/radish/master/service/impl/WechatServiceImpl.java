/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.radish.master.entity.common.JobRole;
import com.radish.master.pojo.Options;
import com.radish.master.service.WechatService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月2日    Create this file
* </pre>
* 
*/
@Service("wechatService")
public class WechatServiceImpl extends BaseServiceImpl implements WechatService {

    @Override
    public List<Options> getAgentOptions() {
        return this.findMapBySql("select id value, agent_name data from tbl_wechat_config", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getRoleOptions() {
        return this.findMapBySql("select id value, name data from tbl_role", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

	@Override
	public List<User> getUserListByJob(String jobID) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobId", jobID);
        return this.find("from User where jobId = :jobId", params);
	}

	@Override
	public UserRole getUserRole(String userID, String roleID) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
        criteria.add(Restrictions.eq("user.id", userID));
        criteria.add(Restrictions.eq("roleId", roleID));
        List<UserRole> urlist = this.findByCriteria(criteria);
        if(!urlist.isEmpty()){
        	return urlist.get(0);
        }else{
        	return null;
        }
	}

	@Override
	public void setUserRole(String jobID, User user) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobId", jobID);
        List<JobRole> jobRoleList = this.find("from JobRole where jobID = :jobId", params);
        
        for(JobRole jobRole : jobRoleList){
        	UserRole ur = new UserRole();
    		ur.setUser(user);
    		ur.setRoleId(jobRole.getRoleID());
    		this.save(ur);
        }
		
	}

	@Override
	public void clearUserRole(String jobID, User user) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobId", jobID);
        List<JobRole> jobRoleList = this.find("from JobRole where jobID = :jobId", params);
        
        for(JobRole jobRole : jobRoleList){
    		DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
            criteria.add(Restrictions.eq("user.id", user.getId()));
            criteria.add(Restrictions.eq("roleId", jobRole.getRoleID()));
            List<UserRole> urlist = this.findByCriteria(criteria);
            if(!urlist.isEmpty()){
            	this.delete(urlist.get(0));
            }
        }
		
	}

}
