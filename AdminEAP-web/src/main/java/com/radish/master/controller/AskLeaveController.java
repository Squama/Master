package com.radish.master.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.AskLeave;
import com.radish.master.entity.volumePay.Loans;

@Controller
@RequestMapping("/askleave")
public class AskLeaveController {
	
	String prefix = "/workmanage/askleave/";
	@Autowired
	private BaseService baseService;
	@Resource
	private RuntimePageService runtimePageService;
	@RequestMapping("/alllook")
	public String alllook(HttpServletRequest request){
		return prefix+"index";
	}
	@RequestMapping("/grindex")
	public String grindex(HttpServletRequest request){
		request.setAttribute("jkr", SecurityUtil.getUserId());
		return prefix+"grindex";
	}
	
	@RequestMapping("/bmshindex")
	public String bmshindex(HttpServletRequest request){
		User u = SecurityUtil.getUser();
		String deptid = u.getDeptId();
		request.setAttribute("deptid", deptid);
		return prefix+"bmshindex";
	}
	@RequestMapping("/bmlook")
	public String bmlook(HttpServletRequest request){
		String  id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix+"bmlook";
	}
	@RequestMapping("/auidtBgs/{id}")//办公室
	public String auidtBgs(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("id", id);
		return prefix+"bgsAuidt";
	}
	@RequestMapping("/auidtBoss/{id}")//boss
	public String auidtBoss(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("id", id);
		return prefix+"bossAuidt";
	}
	@RequestMapping("/cwlook")
	public String cwlook(HttpServletRequest request){
		String  id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix+"look";
	}
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		String  id = request.getParameter("id");
		if(id==null){
			
			User u = SecurityUtil.getUser();
			request.setAttribute("ryid", u.getId());
			request.setAttribute("xm", u.getName());
			if(u.getDeptId()!=null){
				Org bm = baseService.get(Org.class, u.getDeptId());
				if(bm==null){
					request.setAttribute("msg","未找到所属部门，请联系办公管理人员！");	
				}
			}else{
				request.setAttribute("msg","请联系办公管理人员，完善个人部门信息！");
			}
		}else{
			request.setAttribute("id", id);
		}
		
		return prefix+"add";
	}
	@RequestMapping("/look")
	public String look(HttpServletRequest request){
		return prefix+"look";
	}
	
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		String id = request.getParameter("id");
		Result r = new Result();
		AskLeave jk = baseService.get(AskLeave.class, id);
		r.setData(jk);
		return r;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,AskLeave jk){
		String id = request.getParameter("id");
		Result r = new Result();
		if(id==null){//新增
			String userid = jk.getAskid();
			User u = baseService.get(User.class, userid);
			Org bm = baseService.get(Org.class, u.getDeptId());
			jk.setDept(bm.getName());
			jk.setAskame(u.getName());
			jk.setPid(u.getDeptId());
			jk.setStatus("10");
			baseService.save(jk);
		}else{//修改
			AskLeave j = baseService.get(AskLeave.class, id);
			j.setEnddate(jk.getEnddate());
			j.setContent(jk.getContent());
			j.setStartdate(jk.getStartdate());
			j.setAlldays(jk.getAlldays());
			baseService.update(j);
		}
		return r;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(HttpServletRequest request){
		String id = request.getParameter("id");
		AskLeave j = baseService.get(AskLeave.class, id);
		Result r = new Result();
		baseService.delete(j);
		return r;
	}
	
	//提交部门审核
	@RequestMapping("/bmsh")
	@ResponseBody
	public Result bmsh(HttpServletRequest request){
		String id = request.getParameter("id");
		AskLeave j = baseService.get(AskLeave.class, id);
		j.setStatus("20");
		baseService.update(j);
		Result r = new Result();
		return r;
		
	}
	
	//部门审核
		@RequestMapping("/bmsubmit")
		@ResponseBody
		public Result bmsubmit(HttpServletRequest request,AskLeave jk){
			String id = request.getParameter("id");
			String lx = request.getParameter("lx");
			AskLeave j = baseService.get(AskLeave.class, id);
			
			User user = SecurityUtil.getUser();
			Org bm = baseService.get(Org.class, j.getPid());
			if("10".equals(lx)){//通过
				j.setStatus("40");
				String name ="部门："+bm.getName()+",姓名："+jk.getAskame()+",请假申请【审核】";

		        // businessKey
		        String businessKey = j.getId();

		        // 配置流程变量
		        Map<String, Object> variables = new HashMap<>();
		        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
		        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
		        variables.put("taskName", name);
		        variables.put("days", j.getAlldays());
                                          
		        // 启动流程----1天之内到办公室办结。
			    runtimePageService.startProcessInstanceByKey("AskLeave", name, variables, user.getId(), businessKey);
			}else if("20".equals(lx)){//驳回
				j.setStatus("30");
			}
			j.setBmyj(jk.getBmyj());
			j.setBmshr(user.getName());
			baseService.update(j);
			Result r = new Result();
			return r;
		}
}
