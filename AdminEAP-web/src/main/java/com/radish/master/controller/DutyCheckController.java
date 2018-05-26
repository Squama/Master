package com.radish.master.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.CheckDetail;
import com.radish.master.entity.DutyCheck;
import com.radish.master.entity.Project;
import com.radish.master.service.BudgetService;
import com.radish.master.service.DutyCheckService;

@Controller
@RequestMapping("/dutycheck")
public class DutyCheckController {
	@Autowired
	private DutyCheckService dcs;
	
	@Autowired
	private BaseService baseService;
	@Autowired
    private BudgetService budgetService;
	private String prefix="/safetyManage/dutycheck/";
	
	@RequestMapping("/index")
	public String index(){
		return prefix+"dutycheck_list";
	}
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id , u.job_id jobId from tbl_user u , tbl_org o where u.dept_id = o.id and o.code like 'PROJECT%' and u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("users", JSONArray.toJSONString(ul));
		
		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id , u.job_id jobId from tbl_user u , tbl_org o where u.dept_id = o.id and u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("checker", JSONArray.toJSONString(ul1));
		
		return prefix+"dutycheck_addIndex";
	}
	@RequestMapping("/edit")
	public String edit(String id,HttpServletRequest request){
		request.setAttribute("id", id);
	    request.setAttribute("doWhat",request.getParameter("doWhat"));
	    if(id==null){
	    	return prefix+"dutycheck_add";
	    }else{
	    	return prefix+"dutycheck_edit";
	    }
		
	}
	@RequestMapping("/checkMX")
	public String checkMX(String lx,HttpServletRequest request,@RequestParam("id")String id){
		request.setAttribute("id", id);
		List<CheckDetail> list = baseService.findBySql("select * from tbl_checkdetail  where dutycheck_ID='"+id+"'",CheckDetail.class);
		if(list.size()>0){
			request.setAttribute("cd", JSONArray.toJSONString(list.get(0)));
		}
		return prefix+"BD/bd"+lx;
	}
	@RequestMapping("/checkmatch")
	public String checkmatch(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return "/safetyManage/checkmatch/matchIndex";
	}
	@RequestMapping("/checkproject")
	public String checkproject(HttpServletRequest request){
		return "/safetyManage/checkmatch/matchIndex1";
	}
	@RequestMapping("/checkTwoProject")
	public String checkTwoProject(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return "/safetyManage/checkmatch/matchIndex2";
	}
	
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Result save(HttpServletRequest request,DutyCheck dc){
		String id = request.getParameter("id");
		Result r = new Result();
		if(id==null){
			String duty = dc.getDuties();
			if(duty!=null){
				Dict d = baseService.get(Dict.class, duty);
				dc.setDuties(d.getValue());
			}
			dc.setIsValid("1");
			String projectid = dc.getProject_id();
			Project p = baseService.get(Project.class, projectid);
			dc.setProject_name(p.getProjectName());
			String i = dcs.save(dc);
			r.setCode(i);
		}else{
			DutyCheck dcl = dcs.load(id);
			dcl.setName(dc.getName());
			dcl.setCheck_name(dc.getCheck_name());
			dcl.setDeduction(dc.getDeduction());
			dcl.setUnitName(dc.getUnitName());
			dcl.setDuties(dc.getDuties());
			dcl.setCheck_time(dc.getCheck_time());
			dcl.setScore(dc.getScore());
			dcl.setCheck_result(dc.getCheck_result());
			dcl.setJc(dc.getJc());
			dcs.saveOrUpdate(dcl);
			r.setCode(id);
		}
		
    	r.setSuccess(true);
    	return r;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(String id){
		DutyCheck dc = baseService.get(DutyCheck.class, id);
		Result r = new Result();
		try{
		baseService.delete(dc);
		}catch(Exception e){
			r.setSuccess(false);
			return r;
		}
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public DutyCheck load(String id){
		DutyCheck dc = baseService.get(DutyCheck.class, id);
		return dc;
	}
	@RequestMapping(value="/getRylx",method = RequestMethod.POST)
    @ResponseBody
    public Result getRylx(){
		List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='JOBS'",Dict.class);
    	List<Dict> list = baseService.findBySql("select * from tbl_dict where parent_id='"+d.get(0).getId()+"' and code like'PROJECT%'",Dict.class);
      	Result r = new Result();
      	r.setData(list);
    	return r;
    }
	
	@RequestMapping("/saveMx")
	@ResponseBody
	public Result saveMx(HttpServletRequest request,CheckDetail cd){
		String id = request.getParameter("id");
		if(id==null){
			baseService.save(cd);
		}else{
			baseService.update(cd);
		}
		Result r = new Result();
		r.setSuccess(true);
    	return r;
	}
	@RequestMapping("/getData")
	@ResponseBody
	public List<Map> getData(HttpServletRequest request,DutyCheck dc ){
		List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='JOBS'",Dict.class);
    	List<Dict> rylxs = baseService.findBySql("select * from tbl_dict where parent_id='"+d.get(0).getId()+"' and code like'PROJECT%'",Dict.class);
		
		Date check_time = dc.getCheck_time(); 
		String projectid =  dc.getProject_id();
		String sql = " ";
		if(check_time!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String s = sdf.format(check_time);
			sql += " and check_time = '"+s+"'";
		}
		if(projectid!=null){
			
			sql +=" and project_id= '"+projectid+"'";
		}
		List<Map> result = new ArrayList<Map>();//查询结果集
		List<Map> rypjf = new ArrayList<Map>();//职务平均分
		for(Dict rylx:rylxs){
			Map map = new HashMap();
			List<DutyCheck> dls = baseService.findBySql(" select * from tbl_dutycheck where duties='"+rylx.getValue()+"' "+sql, DutyCheck.class);
			Double zf = 0.0;
			for(DutyCheck dl:dls){
				zf +=dl.getScore() ; 
			}
			Double avg = zf/dls.size();
			map.put("fs", avg);
			map.put("rylx", rylx.getName());
			rypjf.add(map);
		}
		
		return rypjf;
	}
	@RequestMapping("/getXmZwData")
	@ResponseBody
	public List<Map> getXmZwData(HttpServletRequest request,DutyCheck dc){
		List<Project> proList = baseService.findBySql(" select * from tbl_project", Project.class);
		String duty = dc.getDuties();
		Date check_time = dc.getCheck_time(); 
		String sql = " ";
		if(check_time!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String s = sdf.format(check_time);
			sql += " and check_time = '"+s+"'";
		}
		List<Map> result = new ArrayList<Map>();//查询结果集
		List<Map> rypjf = new ArrayList<Map>();//职务平均分
		for(Project p :proList){
			Map map = new HashMap();
			
			List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='JOBS'",Dict.class);
	    	List<Dict> rylxs = baseService.findBySql("select * from tbl_dict where parent_id='"+d.get(0).getId()+"' and code like'PROJECT%'",Dict.class);
			List<Map> gzwpjf = new ArrayList<Map>();
			for(Dict rylx:rylxs){
				Map map1 = new HashMap();
				List<DutyCheck> dls = baseService.findBySql(" select * from tbl_dutycheck where project_id='"+p.getId()+"' and duties='"+rylx.getValue()+"' "+sql, DutyCheck.class);
				Double zf = 0.0;
				for(DutyCheck dl:dls){
					if(dl.getScore()!=null){
						zf +=dl.getScore() ; 
					}
					
				}
				Double avg=0.0;
				if(dls.size()>0){
					avg = zf/dls.size();
				}
				map1.put("avg", avg);
				map1.put("rylx", rylx.getName());
				gzwpjf.add(map1);
			
			}
			map.put("fs", gzwpjf);
			map.put("proName", p.getProjectName());
			
			rypjf.add(map);
		}
		return rypjf;
	}
	@RequestMapping("/getTwoXmData")
	@ResponseBody
	public List<Map> getTwoXmData(HttpServletRequest request,DutyCheck dc ){
		List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='JOBS'",Dict.class);
    	List<Dict> rylxs = baseService.findBySql("select * from tbl_dict where parent_id='"+d.get(0).getId()+"' and code like'PROJECT%'",Dict.class);
		Date check_time = dc.getCheck_time(); 
		String projectid =  dc.getProject_id();
		String projectid1 = request.getParameter("project_id1");
		String sql = " ";
		String sql1=" ";
		if(check_time!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String s = sdf.format(check_time);
			sql += " and check_time = '"+s+"'";
			sql1 += " and check_time = '"+s+"'";
		}
		if(projectid!=null){
			
			sql +=" and project_id= '"+projectid+"'";
		}
		if(projectid1!=null){
			
			sql1 +=" and project_id= '"+projectid1+"'";
		}
		List<Map> result = new ArrayList<Map>();//查询结果集
		List<Map> rypjf = new ArrayList<Map>();//职务平均分
		for(Dict rylx:rylxs){
			Map map = new HashMap();
			//第一个项目
			List<DutyCheck> dls = baseService.findBySql(" select * from tbl_dutycheck where duties='"+rylx.getValue()+"' "+sql, DutyCheck.class);
			Double zf = 0.0;
			for(DutyCheck dl:dls){
				zf +=dl.getScore() ; 
			}
			Double avg = 0.0;
			if(dls.size()>0){
				avg = zf/dls.size();
			}
			
			//第二个项目
			List<DutyCheck> dls1 = baseService.findBySql(" select * from tbl_dutycheck where duties='"+rylx.getValue()+"' "+sql1, DutyCheck.class);
			Double zf1 = 0.0;
			for(DutyCheck dl:dls1){
				
				zf1 +=dl.getScore() ; 
			}
			Double avg1 = 0.0;
			if(dls1.size()>0){
				avg1 = zf1/dls1.size();
			}
			map.put("fs", avg);
			map.put("fs1", avg1);
			map.put("rylx", rylx.getName());
			rypjf.add(map);
		}
		
		return rypjf;
	}
}
