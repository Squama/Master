package com.radish.master.controller.volumePay;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/volumepay")
public class VolumePayController {
	private String prefix="/VolumePay/";
	@Resource
    private BudgetService budgetService;
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix+"list";
	}
	@RequestMapping("/payMoney")
	public String payMoney(HttpServletRequest request){
		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u ", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("use", JSONArray.toJSONString(ul1));
		String gclid = request.getParameter("gclid");
		String zfid = request.getParameter("zfid");
		String type = request.getParameter("type");
		String lx = request.getParameter("lx");
		request.setAttribute("gclid", gclid);
		request.setAttribute("zfid", zfid);
		request.setAttribute("type", type);
		request.setAttribute("lx", lx);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix+"payEdit";
	}
	
	@RequestMapping("/isHavePay")
	@ResponseBody
	public Result isHavePay(HttpServletRequest request){
		Result r = new Result();
		String gclid= request.getParameter("gclid");
		String payType = request.getParameter("payType");
		String sql = " select * from tbl_volumePay where volumeId='"+gclid+"' and payType='"+payType+"'";
		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
		if(zfs.size()==0){
			r.setSuccess(true);
		}
		return r;
	}
	@RequestMapping("/lookZfxx")
	@ResponseBody
	public Result lookZfxx(HttpServletRequest request){
		String zfid = request.getParameter("zfid");
		VolumePay zf = baseService.get(VolumePay.class, zfid);
		Result r = new Result();
		r.setData(zf);
		return r;
	}
	@RequestMapping("/getGclxx")
	@ResponseBody
	public Result getGclxx(HttpServletRequest request){
		String gclid = request.getParameter("gclid");
		String type = request.getParameter("type");
		ProjectVolume gcl = baseService.get(ProjectVolume.class, gclid);
		Labor ht = baseService.get(Labor.class, gcl.getLaborID());
		
		VolumePay zf = new VolumePay();
		zf.setDepartment(gcl.getProjectName()+"-"+ht.getConstructionTeam());
		if("10".equals(type)){//人工
			zf.setPayMoney(gcl.getFinalLabour());
		}else if("20".equals(type)){//机械
			zf.setPayMoney(gcl.getFinalMech());
		}else if("30".equals(type)){//材料
			zf.setPayMoney(gcl.getFinalMat());
		}
		zf.setPayType(type);
		zf.setVolumeId(gclid);
		Result r = new Result();
		r.setData(zf);
		return r;
	}
	@RequestMapping("/doPay")
	@ResponseBody
	public Result doPay(HttpServletRequest request,VolumePay zf){
		String gclid = request.getParameter("gclid");
		zf.setVolumeId(gclid);
		zf.setCreateDate(new Date());
		zf.setCreateId(SecurityUtil.getUserId());
		baseService.save(zf);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/isHaveSalary")
	@ResponseBody
	public Result isHaveSalary(HttpServletRequest request){
		Arith arith = new Arith();
		Result r = new Result();
		String gclid = request.getParameter("gclid");
		ProjectVolume gcl = baseService.get(ProjectVolume.class, gclid);
		List<Salary> gz = baseService.findBySql(" select * from tbl_salary where volume_id='"+gclid+"'", Salary.class);
		if(gz.size()>0){
			List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gz.get(0).getId()+"'", SalaryDetail.class);
			Double gzje = 0.0;
			for(SalaryDetail gzmx:gzmxs){
				gzje = arith.add(gzje, Double.valueOf(gzmx.getActual()));
			}
			if(Double.valueOf(gcl.getFinalLabour()).equals(gzje)){
				r.setSuccess(true);
			}else{
				r.setMessage("工程量人工费与工资单人工费不符，请确认工资单人工费。(工程量人工费:"+gcl.getFinalLabour()+",工资单人工费:"+gzje+")");
			}
		}else{
			r.setMessage("未生成工资数据，请先生成工资数据");
		}
		return r;
	}
}
