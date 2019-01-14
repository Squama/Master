package com.radish.master.controller.safty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.BuildDiary;
import com.radish.master.entity.Project;
import com.radish.master.entity.safty.SafeBuildDiary;
import com.radish.master.pojo.Options;

@Controller
@RequestMapping("/safediary")
public class SafeDiaryController {
	
	private String prefix = "safetyManage/newDiary/";
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/grlist")
	public String grlist(HttpServletRequest request){
		//列表添加人员条件
		User u = SecurityUtil.getUser();
		request.setAttribute("ryid", u.getId());
		request.setAttribute("ry", u.getName());
		
		//判断人员是否存在班组人员表，存在，搜出班组的项目，不存在 搜出全部项目
		String sql = "select a.id value, a.project_name data from tbl_project a where "
				+ " exists (select b.* from tbl_user_team b where a.id = b.project_id and b.user_id = '"+u.getId()+"')";
		List tjxm = baseService.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		if(tjxm.size()>0){
			request.setAttribute("xm", JSONArray.toJSONString(tjxm));
		}else{
			List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
			request.setAttribute("xm", JSONArray.toJSONString(xm));
		}
		
		
		return prefix +"grlist";
	}
	@RequestMapping("/gllist")
	public String gllist(HttpServletRequest request){
		List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		request.setAttribute("xm", JSONArray.toJSONString(xm));
		
		return prefix +"gllist";
	}
	@RequestMapping("/addindex")
	public String addindex(HttpServletRequest request){
		String xmid=request.getParameter("xmid");
		String rq=request.getParameter("rq");
		String ryid=request.getParameter("ryid");
		String rzid=request.getParameter("rzid");
		String lx=request.getParameter("lx");
		Project xm = baseService.get(Project.class, xmid);
		User u = baseService.get(User.class, ryid);
		request.setAttribute("xmid", xmid);
		request.setAttribute("rq", rq);
		request.setAttribute("ryid", ryid);
		request.setAttribute("rzid", rzid);
		request.setAttribute("lx", lx);
		request.setAttribute("xmmc", xm.getProjectName());
		request.setAttribute("rymc", u.getName());
		
		String jobid = u.getJobId();
		if(jobid==null){
			request.setAttribute("job", "");
		}else{
			Dict d = baseService.get(Dict.class, jobid);
			if(d==null){
				request.setAttribute("job", "");
			}else{
				request.setAttribute("job", d.getName());
			}
		}
		
		return prefix +"addIndex";
	}
	
	//获取选择年月的对于 人 项目 的整月日志数据
	@RequestMapping("/getRq")
	@ResponseBody
	public Result getRq(HttpServletRequest request){
		Result r = new Result();
		String rq = request.getParameter("rzdate");
		String xmid = request.getParameter("xmid");
		String ryid = request.getParameter("ryid");
		
		String[] rqs = rq.split("-");
		int year =Integer.valueOf(rqs[0]);
		int month = Integer.valueOf(rqs[1]);
		Calendar c = Calendar.getInstance();
		c.set(year, month, 0); 
		//获取选择月的天数
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		//System.out.println(dayOfMonth);
		List<Map<String,Object>> sj = new ArrayList<Map<String,Object>>();
		for(int i=1;i<=dayOfMonth;i++){
			Map<String,Object> m = new HashMap<String, Object>();
			String ts = "";
			if(i<10){
				ts = "0"+i;
			}else{
				ts = ""+i;
			}
			//完整日期
			m.put("rq", rq+"-"+ts);
			//当前日
			m.put("ts", ts);
			m.put("ishave", "0");
			sj.add(m);
		}
		//获取 人员-项目-日期 的施工日志
		String hql = " from SafeBuildDiary where xmid='"+xmid+"' "
				+ " and userid='"+ryid+"' "
				+ " and date_format(rzdate, '%Y-%m') = '"+rq+"' ";
		
		List<SafeBuildDiary> rzs = baseService.find(hql);
		//将查出的结果填入对于的list，
		for(SafeBuildDiary rz:rzs){
			Calendar cal = Calendar.getInstance();
			cal.setTime(rz.getRzdate());
			int t = cal.get(Calendar.DATE);
			sj.get(t-1).put("ishave", "1");
			sj.get(t-1).put("rzid", rz.getId());
		}
		
		r.setData(sj);
		
		return r;
	}
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,SafeBuildDiary rz ){
		String rzid = rz.getId();
		Result r = new Result();
		Project xm = baseService.get(Project.class, rz.getXmid());
		User u = baseService.get(User.class, rz.getUserid());
		if(rzid==null){
			rz.setXmmc(xm.getProjectName());
			rz.setRymc(u.getName());
			String i = (String)baseService.save(rz);
			r.setCode(i);
		}else{
			SafeBuildDiary lrz = baseService.get(SafeBuildDiary.class, rzid);
			lrz.setWeather(rz.getWeather());
			lrz.setAirTemp(rz.getAirTemp());
			lrz.setSgnr(rz.getSgnr());
			lrz.setRemark(rz.getRemark());
			lrz.setJobname(rz.getJobname());
			lrz.setZgnr(rz.getZgnr());
			baseService.update(lrz);
		}
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		String id = request.getParameter("id");
		SafeBuildDiary rz= baseService.get(SafeBuildDiary.class, id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rz", rz);
		Result r = new Result();
		r.setSuccess(true);
		r.setData(map);
		return r;
	}
	@RequestMapping("/getRy")
	@ResponseBody
	public Result getRy(HttpServletRequest request){
		Result r = new Result();
		String xmid = request.getParameter("xmid");
		
		String sql = "select a.id value, a.name data from tbl_user a where "
				+ " (exists (select b.* from tbl_user_team b where a.id = b.user_id and b.project_id = '"+xmid+"')"
				+ " or exists  (select c.* from v_gckry c where a.id = c.id) )";
		List tjxm = baseService.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		r.setData(tjxm);
		return r;
	}
}
