/**
 * 版权所有 (c) 2017
 */
package com.radish.master.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.DictService;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.common.JobRole;
import com.radish.master.service.WechatService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年10月18日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/jobmanage")
public class JobManageController {

    @Resource
    private DictService dictService;
    
    @Resource
    private WechatService wechatService;

    /**
     * 用户列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/tree")
    private String list() {

        return "workmanage/jobmanage/job_tree";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String info(HttpServletRequest request) {
    	request.setAttribute("roleOptions", JSONArray.toJSONString(wechatService.getRoleOptions()));
    	//wechatService.getUserRole("40280c9460fd4ac90160fd8f17720002", "40280cac5f27f894015f2816c4040000");
        
    	
    	
    	return "workmanage/jobmanage/job_role_list";
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public List<Dict> getAll() {

        String hql = "from Dict order by levelCode asc";
        return dictService.find(hql.toString());
    }

    /**
     * getTreeData 构造bootstrap-treeview格式数据
     *
     * @return
     */
    @RequestMapping(value = "/treeData", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeData() {

        return dictService.getTreeDataByCode("JOBS");
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Dict get(@PathVariable("id") String id) {
        Dict dict = dictService.get(Dict.class, id);
        if (!StrUtil.isEmpty(dict.getParentId())) {
            dict.setParentName(dictService.get(Dict.class, dict.getParentId()).getName());
        } else {
            dict.setParentName("系统字典");
        }
        return dict;
    }

    @RequestMapping(value = "/getDictsByCode", method = RequestMethod.POST)
    @ResponseBody
    public List<Dict> getDictsByCode(String code) {
        return dictService.getDictsByCode(code);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(Dict dict) {

        dict.setUpdateDateTime(new Date());
        dictService.saveOrUpdate(dict);
        Dict parent = dictService.get(Dict.class, dict.getParentId());
        dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode());
        dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode() + "s");
        return new Result(true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        try {
            Dict dict = dictService.get(Dict.class, id);
            dictService.delete(dict);
            Dict parent = dictService.get(Dict.class, dict.getParentId());
            dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode());
            dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode() + "s");
            return new Result(true);
        } catch (Exception e) {
            return new Result(false, "该字典已经被其他数据引用，不可删除");
        }
    }
    
    @RequestMapping(value="/select/{id}",method = RequestMethod.GET)
    public String select(@PathVariable("id") String id, HttpServletRequest request) {

    	request.setAttribute("id", id);
        
    	return "workmanage/jobmanage/select_role";
    }
    
    @RequestMapping(value = "/savejobrole", method = RequestMethod.POST)
    @ResponseBody
    public Result saveJobRole(JobRole jobRole) {

    	try{
    		dictService.save(jobRole);
    	}catch(Exception e){
    		return new Result(false);
    	}
    	
    	List<User> userList = wechatService.getUserListByJob(jobRole.getJobID());
    	
    	for(User user : userList){
    		UserRole ur = new UserRole();
    		ur.setUser(user);
    		ur.setRoleId(jobRole.getRoleID());
    		wechatService.save(ur);
    	}
    	
        return new Result(true);
    }
    
    @RequestMapping(value="/deleterole/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result deleteRole(@PathVariable("id") String id) {

    	JobRole jobRole = dictService.get(JobRole.class, id);
    	try{
    		dictService.delete(jobRole);
    	}catch(Exception e){
    		return new Result(false);
    	}
    	
    	List<User> userList = wechatService.getUserListByJob(jobRole.getJobID());
    	
    	for(User user : userList){
    		UserRole ur = wechatService.getUserRole(user.getId(), jobRole.getRoleID());
    		if(ur != null){
    			wechatService.delete(ur);
    		}
    	}
    	
        return new Result(true);
    }
    
}
