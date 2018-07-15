package com.radish.master.controller.safty;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.safty.JobDuty;

@Controller
@RequestMapping("/dutygl")
public class JobDutyController {
	
	@Autowired
	private BaseService baseService;
	
	private String prefix = "/safetyManage/dutyGl/";
	
	@RequestMapping("/jobduty")
	public String jobduty(HttpServletRequest request){
		return prefix +"dutyJob";
	}
	
	@RequestMapping("/oneduty")
	public String oneduty(HttpServletRequest request){
		String id = SecurityUtil.getUserId();
		User u = baseService.get(User.class, id);
		if(u.getJobId()!=null){
			Dict d = baseService.get(Dict.class,u.getJobId());
			request.setAttribute("zw", d.getName());
			List<JobDuty> zes = baseService.find("from JobDuty where jobID = '"+u.getJobId()+"'");
			
			if(zes.size()>0){
				request.setAttribute("zwms", zes.get(0).getContent());
			}else{
				request.setAttribute("zwms", "无");
			}
		}else{
			request.setAttribute("zw", "无");
			request.setAttribute("zwms", "无");
		}
		if(u.getDutyContent()!=null){
			request.setAttribute("grzz",u.getDutyContent());
		}else{
			request.setAttribute("grzz", "无");
		}
		return prefix +"oneduty";
	}
	
	@RequestMapping("/getDuty")
	@ResponseBody
	public JobDuty getDuty(HttpServletRequest request){
		String jobid = request.getParameter("jobID");
		List<JobDuty> zes = baseService.find("from JobDuty where jobID = '"+jobid+"'");
		if(zes.size()>0){
			return zes.get(0);
		}else{
			return new JobDuty(); 
		}

	}
	@RequestMapping("/saveDuty")
	@ResponseBody
	public Result saveDuty(HttpServletRequest request,JobDuty duty){
		List<JobDuty> zes = baseService.find("from JobDuty where jobID = '"+duty.getJobID()+"'");
		Result r = new Result();
		if(zes.size()>0){
			JobDuty ze = zes.get(0);
			ze.setContent(duty.getContent());
			baseService.update(ze);
		}else{
			baseService.save(duty);
		}
		
		return r;
	}

}
