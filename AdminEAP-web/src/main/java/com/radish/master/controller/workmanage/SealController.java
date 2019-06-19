package com.radish.master.controller.workmanage;

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
import com.radish.master.entity.BgwjFile;
import com.radish.master.entity.volumePay.Reimburse;
import com.radish.master.entity.workmanage.Car;
import com.radish.master.entity.workmanage.Caruse;
import com.radish.master.entity.workmanage.Seal;
import com.radish.master.entity.workmanage.Sealuse;
import com.radish.master.pojo.MatInAndOut;

@Controller
@RequestMapping("/seals")
public class SealController {
	String prefix = "workmanage/seal/";
	@Autowired
	private BaseService baseService;
	@Resource
	private RuntimePageService runtimePageService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		return prefix +"list";
	}
	
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		String ck = request.getParameter("ck");
		request.setAttribute("ck", ck);
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix +"addIndex";
	}
	@RequestMapping("/useindex")
	public String useindex(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		request.setAttribute("userid", SecurityUtil.getUserId());
		return prefix +"useindex";
	}
	@RequestMapping("/bmshindex")
	public String bmshindex(HttpServletRequest request){
		User u = SecurityUtil.getUser();
		String deptid = u.getDeptId();
		request.setAttribute("deptid", deptid);
		return prefix+"bmshindex";
	}
	@RequestMapping("/bmAuidt")
	public String bmAuidt(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		request.setAttribute("userid", SecurityUtil.getUserId());
		String ck = request.getParameter("ck");
		request.setAttribute("ck", ck);
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		//获取部门信息
		//List bm = baseService.findMapBySql("select id value, name data,pname pdata from v_depts", new Object[]{}, new Type[]{StringType.INSTANCE}, OptionsDept.class);
		//request.setAttribute("bms", JSONArray.toJSONString(bm));
		List<Seal> cls = baseService.find(" from Seal where 1=1");//有效印章
		for(Seal cl:cls){
			cl.setDescs("");
		}
		request.setAttribute("cls", JSONArray.toJSONString(cls));
		return prefix +"bmAuidt";
	}
	@RequestMapping("/bgsauidt/{id}")//审核查看页
	public String bgsauidt(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("id", id);
		List<Seal> cls = baseService.find(" from Seal where 1=1");//有效印章
		for(Seal cl:cls){
			cl.setDescs("");
		}
		request.setAttribute("cls", JSONArray.toJSONString(cls));
		return prefix+"auidtLookBgs";
		
		
	}
	
	//使用添加
		@RequestMapping("/useadd")
		public String useadd(HttpServletRequest request){
			String lx = request.getParameter("lx");
			request.setAttribute("lx", lx);
			request.setAttribute("userid", SecurityUtil.getUserId());
			String ck = request.getParameter("ck");
			request.setAttribute("ck", ck);
			String id = request.getParameter("id");
			request.setAttribute("id", id);
			//获取部门信息
			//List bm = baseService.findMapBySql("select id value, name data,pname pdata from v_depts", new Object[]{}, new Type[]{StringType.INSTANCE}, OptionsDept.class);
			//request.setAttribute("bms", JSONArray.toJSONString(bm));
			List<Seal> cls = baseService.find(" from Seal where isvalid='1'");//有效印章
			for(Seal cl:cls){
				cl.setDescs("");
			}
			request.setAttribute("cls", JSONArray.toJSONString(cls));
			return prefix +"useaddIndex";
		}
	//打印查询页面
	@RequestMapping("/dyList")
	public String dyList(HttpServletRequest request){
		request.setAttribute("czy", SecurityUtil.getUserId());
		return prefix +"dyList";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Result save(Seal cl,HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		if(id==null){//保存
			cl.setIsvalid("1");
			baseService.save(cl);
			id = cl.getId();
		}else{
			Seal c = baseService.get(Seal.class, id);
			c.setName(cl.getName());
			c.setDescs(cl.getDescs());
			baseService.update(c);
		}
		r.setCode(id);
		
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Seal c = baseService.get(Seal.class, id);
		r.setData(c);
		return r;
		
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Seal c = baseService.get(Seal.class, id);
		c.setIsvalid("0");
		baseService.update(c);
		return r;
	}
	
	@RequestMapping("/saveuse")
	@ResponseBody
	public Result saveuse(Sealuse cl,HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		String deptid = "";
		String dept = "";
		if(u.getDeptId()!=null){
			Org bm = baseService.get(Org.class, u.getDeptId());
			if(bm!=null){
				deptid=bm.getId();
				dept=bm.getName();
			}
		}
		Seal cls = baseService.get(Seal.class, cl.getSealid());//所选印章
		if(id==null){//保存
			cl.setName(cls.getName());
			cl.setStatus("10");
			cl.setSqrid(u.getId());
			cl.setSqr(u.getName());
			cl.setSqtime(new Date());
			cl.setDeptid(deptid);
			cl.setDeptname(dept);
			baseService.save(cl);
			id = cl.getId();
		}else{
			Sealuse c = baseService.get(Sealuse.class, id);
			c.setSealid(cl.getSealid());
			c.setName(cls.getName());
			c.setSl(cl.getSl());
			c.setGsmc(cl.getGsmc());
			c.setSqyy(cl.getSqyy());
			cl.setDeptid(deptid);
			cl.setDeptname(dept);
			baseService.update(c);
		}
		r.setCode(id);
		return r;
	}
	@RequestMapping("/loaduse")
	@ResponseBody
	public Result loaduse(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Sealuse c = baseService.get(Sealuse.class, id);
		r.setData(c);
		return r;
	}
	@RequestMapping("/usedelete")
	@ResponseBody
	public Result usedelete(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Sealuse c = baseService.get(Sealuse.class, id);
		baseService.delete(c);
		return r;
	}
	@RequestMapping("/dosubmit")
	@ResponseBody
	public Result dosubmit(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Sealuse c = baseService.get(Sealuse.class, id);
		c.setStatus("20");
		baseService.update(c);
		return r;
	}
	
	//部门审核
		@RequestMapping("/bmsubmit")
		@ResponseBody
		public Result bmsubmit(HttpServletRequest request,Sealuse jk){
			String id = request.getParameter("id");
			String lx = request.getParameter("lx");
			Sealuse bx = baseService.get(Sealuse.class, id);
			
			User user = SecurityUtil.getUser();
			if("10".equals(lx)){//通过
				bx.setStatus("40");
				String name ="公司/项目："+bx.getGsmc()+"【用章申请审核】";

		        // businessKey
		        String businessKey = bx.getId();

		        // 配置流程变量
		        Map<String, Object> variables = new HashMap<>();
		        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
		        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
		        variables.put("taskName", name);

		        // 启动流程
		        runtimePageService.startProcessInstanceByKey("Seals", name, variables, user.getId(), businessKey);
				
				
			}else if("20".equals(lx)){//驳回
				bx.setStatus("30");
			}
			bx.setBmbhyy(jk.getBmbhyy());
			bx.setBmshr(user.getName());
			baseService.update(bx);
			Result r = new Result();
			return r;
		}
		@RequestMapping("getDyInfo")
		@ResponseBody
		public Result getDyInfo(HttpServletRequest request){
			String kssj = request.getParameter("startdate");
			String jssj = request.getParameter("enddate");
			Result r = new Result();
			String sql = "select q.ID id ,"
					+ "q.name name,"
					+ "q.sqtime sqtime,"
					+ "q.sl sl,"
					+ "q.gsmc gsmc,"
					+ "q.deptname deptname,"
					+ "q.bmshr bmshr from v_yzsq q where q.sqtime >='"+kssj+"' and q.sqtime <= '"+jssj+"'";
			
			List<Sealuse> sts = baseService.findMapBySql(sql, new Object[]{}, new Type[]{}, Sealuse.class);
			r.setData(sts);
			return r;
		}
	
}
