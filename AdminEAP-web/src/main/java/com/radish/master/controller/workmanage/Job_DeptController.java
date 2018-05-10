package com.radish.master.controller.workmanage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.TreeUtil;
import com.radish.master.entity.JobDeptRt;
import com.radish.master.pojo.Options;
import com.radish.master.pojo.OptionsNew;

@Controller
@RequestMapping("/deptjob")
public class Job_DeptController {
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/index")
	public String index(){
		return "/workmanage/job_dept/job_dept";
	}
	
	@RequestMapping("/deptTree")
	@ResponseBody
	public List<TreeNode> deptTree(){
		// 获取数据
        String hql = "from Org order by levelCode asc";
        List<Org> funcs = baseService.find(hql);
        Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
        for (Org func : funcs) {
            TreeNode node = new TreeNode();
            node.setText(func.getName());
            node.setId(func.getId());
            node.setParentId(func.getParentId());
            node.setLevelCode(func.getLevelCode());
            nodelist.put(node.getId(), node);
        }
        // 构造树形结构
        return TreeUtil.getNodeList(nodelist);
	}
	@RequestMapping("/jobTree")
	@ResponseBody
	public List<TreeNode> jobTree(){
		String pid = "";
		String s = " from Dict where code = 'JOBS'";
		List<Dict> dicts = baseService.find(s);
		if(dicts.size()>0){
			pid =dicts.get(0).getId();
		}
		// 获取数据
        String hql = "from Dict  where parentId='"+pid+"' order by levelCode asc";
        List<Dict> funcs = baseService.find(hql);
        Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
        for (Dict func : funcs) {
            TreeNode node = new TreeNode();
            node.setText(func.getName());
            node.setId(func.getId());
            //node.setParentId(func.getParentId());
            //node.setLevelCode(func.getLevelCode());
            nodelist.put(node.getId(), node);
        }
        // 构造树形结构
        return TreeUtil.getNodeList(nodelist);
	}
	@RequestMapping("/getjob")
	@ResponseBody
	public List getjob(HttpServletRequest request){
		String deptid = request.getParameter("deptid");
		List s = new ArrayList();
		
		//获取排序
		String pid = "";
		String hql = " from Dict where code = 'JOBS'";
		List<Dict> dicts = baseService.find(hql);
		if(dicts.size()>0){
			pid =dicts.get(0).getId();
		}
		String sql = "select (@i\\:=@i+1) value ,job.id data from tbl_dict job ,(select @i\\:=-1) as it where  job.parent_id='"+pid+"' order by levelCode asc";
		List<OptionsNew> jg = baseService.findMapBySql(sql,new Object[]{}, new Type[]{StringType.INSTANCE}, OptionsNew.class);
		
		List<JobDeptRt> gxs = baseService.find(" from JobDeptRt where deptId = '"+deptid+"'");
		for(JobDeptRt gx:gxs){
			for(int i=0;i<jg.size();i++){
				if(jg.get(i).getData().equals(gx.getJobId())){
					s.add(jg.get(i).getValue());
					continue;
				}
			}
		}
		
		return s;
	}
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request){
		String deptid = request.getParameter("deptid");
		String jobs = request.getParameter("jobs");
		
		List<JobDeptRt> gxs = baseService.find(" from JobDeptRt where deptId = '"+deptid+"'");
		for(JobDeptRt gx:gxs){
			baseService.delete(gx);
		}
		
		if(jobs.indexOf(",")<0){
			JobDeptRt gx = new JobDeptRt();
			gx.setDeptId(deptid);
			gx.setJobId(jobs);
			baseService.save(gx);
		}else{
			String[] job = jobs.split(",");
			for(int i =0;i<job.length;i++){
				JobDeptRt gx = new JobDeptRt();
				gx.setDeptId(deptid);
				gx.setJobId(job[i]);
				baseService.save(gx);
			}
		}
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
}
