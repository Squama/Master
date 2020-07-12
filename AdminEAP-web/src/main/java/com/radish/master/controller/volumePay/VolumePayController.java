package com.radish.master.controller.volumePay;

import java.math.BigDecimal;
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
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.project.Worker;
import com.radish.master.entity.volumePay.Reimburse;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/volumepay")
public class VolumePayController {
	private String prefix="/VolumePay/";
	@Resource
    private BudgetService budgetService;
	 @Resource
	 private RuntimePageService runtimePageService;
	
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		List<ProjectTeam> htbzs = budgetService.find(" from ProjectTeam where 1=1 ");
        request.setAttribute("htbzs", JSONArray.toJSONString(htbzs));
		return prefix+"list";
	}
	@RequestMapping("/payList")
	public String payList(HttpServletRequest request){
		Arith air = new Arith();
		String gclid = request.getParameter("gclid");
		String type = request.getParameter("type");
		request.setAttribute("gclid", gclid);
		request.setAttribute("type", type);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		if("10".equals(type)){
			List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where volume_id='"+gclid+"'", SalaryVolume.class);
			if(gxs.size()>0){
				Salary gz = baseService.get(Salary.class, gxs.get(0).getSalaryID());
				if(Integer.valueOf(gz.getStatus())>=60&&Integer.valueOf(gz.getStatus())!=70){
					request.setAttribute("gz", JSONArray.toJSONString(gz));
					List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gz.getId()+"'", SalaryDetail.class);
					double zje = 0.0;
					for(SalaryDetail gzmx:gzmxs){
						zje =air.add(zje, Double.valueOf(gzmx.getActual()));
					}
					String sql = " select * from tbl_volumePay where volumeId='"+gclid+"' and payType='10' and status<>'40'";
					List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
					/*double kzf = zje;
					for(VolumePay zf:zfs){
						kzf = air.sub(kzf, Double.valueOf(zf.getPayMoney()));
					}*/
					double kzf = 0.0;
					for(VolumePay zf:zfs){
						kzf = air.add(kzf, Double.valueOf(zf.getPayMoney()));
					}
					request.setAttribute("zje", zje);
					request.setAttribute("kzf", kzf);
				}
				
			}else{
				String sql = " select * from tbl_volumePay where volumeId='"+gclid+"' and payType='10' and status<>'40'";
				List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
				ProjectVolume pv = baseService.get(ProjectVolume.class, gclid);
				double zje = 0.0;
				zje = Double.valueOf(pv.getFinalLabour());
				if(pv.getFinalDebit()!=null){
					zje = air.sub(zje, Double.valueOf(pv.getFinalDebit()));
				}
				/*double kzf = zje;
				for(VolumePay zf:zfs){
					kzf = air.sub(kzf, Double.valueOf(zf.getPayMoney()));
				}*/
				double kzf = 0.0;
				for(VolumePay zf:zfs){
					kzf = air.add(kzf, Double.valueOf(zf.getPayMoney()));
				}
				request.setAttribute("zje", zje);
				request.setAttribute("kzf", kzf);
			}
			return prefix+"payListRg";
		}
		return prefix+"payList";
	}
	@RequestMapping("/payListBGBL")
	public String payListBGBL(HttpServletRequest request){
		Arith air = new Arith();
		String gclid = request.getParameter("gclid");
		String type = request.getParameter("type");
		request.setAttribute("gclid", gclid);
		request.setAttribute("type", type);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix+"payListBGBL";
	}
	@RequestMapping("/addPayInfo")//非人工支付
	public String addPayInfo(HttpServletRequest request){
		Arith air = new Arith();
//		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		List<Worker> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_worker u ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("use", JSONArray.toJSONString(ul1));
		String gclid = request.getParameter("gclid");
		String type = request.getParameter("type");
		request.setAttribute("gclid", gclid);
		request.setAttribute("type", type);
		
		String zfid = request.getParameter("zfid");
		String lx = request.getParameter("lx");
		request.setAttribute("zfid", zfid);
		request.setAttribute("lx", lx);
		
		//算出总金额和可支付金额
		ProjectVolume pv = baseService.get(ProjectVolume.class, gclid);
		Labor ht = baseService.get(Labor.class, pv.getLaborID());
		request.setAttribute("bm", pv.getProjectName()+"--"+ht.getConstructionTeam());
		String sql = " select * from tbl_volumePay where volumeId='"+gclid+"' and payType='"+type+"' and status<>'40'";
		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
		double zje = 0.0;
		if("20".equals(type)){
			zje = Double.valueOf(pv.getFinalMech());
			if(pv.getFinalDebitjx()!=null){
				zje = air.sub(zje, Double.valueOf(pv.getFinalDebitjx()));
			}
		}else if("30".equals(type)){
			zje = Double.valueOf(pv.getFinalMat());
			if(pv.getFinalDebitcl()!=null){
				zje = air.sub(zje, Double.valueOf(pv.getFinalDebitcl()));
			}
		}else if("40".equals(type)){
			if(pv.getFinalPack()==null){
				zje=0.0;
			}else{
				zje = Double.valueOf(pv.getFinalPack());
			}
			
		}
		double kzf = zje;
		for(VolumePay zf:zfs){
			kzf = air.sub(kzf, Double.valueOf(zf.getPayMoney()));
		}
		request.setAttribute("zje", zje);
		request.setAttribute("kzf", kzf);
		
		return prefix+"addPayIndex";
	}
	
	@RequestMapping("/addPayInfoRg")//人工支付
	public String addPayInfoRg(HttpServletRequest request){
		Arith air = new Arith();
//		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		List<Worker> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_worker u ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("use", JSONArray.toJSONString(ul1));
		String gclid = request.getParameter("gclid");
		request.setAttribute("gclid", gclid);
		
		String gzid = request.getParameter("gzid");
		request.setAttribute("gzid", gzid);
		
		String zfid = request.getParameter("zfid");
		String lx = request.getParameter("lx");
		request.setAttribute("zfid", zfid);
		request.setAttribute("lx", lx);
		
		/*List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gzid+"'", SalaryDetail.class);
		double zje = 0.0;
		for(SalaryDetail gzmx:gzmxs){
			zje =air.add(zje, Double.valueOf(gzmx.getActual()));
		}*/
		//算出总金额和可支付金额
		ProjectVolume pv = baseService.get(ProjectVolume.class, gclid);
		double zje = 0.0;
		zje = Double.valueOf(pv.getFinalLabour());
		if(pv.getFinalDebit()!=null){
			zje = air.sub(zje, Double.valueOf(pv.getFinalDebit()));
		}
		
		
		String sql = " select * from tbl_volumePay where volumeId='"+gclid+"' and payType='10' and status<>'40'";
		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
		double kzf = zje;
		for(VolumePay zf:zfs){
			kzf = air.sub(kzf, Double.valueOf(zf.getPayMoney()));
		}
		request.setAttribute("zje", zje);
		request.setAttribute("kzf", kzf);
		Labor ht = baseService.get(Labor.class, pv.getLaborID());
		request.setAttribute("bm", pv.getProjectName()+"--"+ht.getConstructionTeam());
		
		
		return prefix+"addPayIndexRg";
	}
	@RequestMapping("/addPayInfoBGBL")//包工包料
	public String addPayInfoBGBL(HttpServletRequest request){
		Arith air = new Arith();
//		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		List<Worker> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_worker u ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("use", JSONArray.toJSONString(ul1));
		String gclid = request.getParameter("gclid");
		String type = request.getParameter("type");
		request.setAttribute("gclid", gclid);
		request.setAttribute("type", type);
		
		String zfid = request.getParameter("zfid");
		String lx = request.getParameter("lx");
		request.setAttribute("zfid", zfid);
		request.setAttribute("lx", lx);
		
		//算出总金额和可支付金额
		ProjectVolume pv = baseService.get(ProjectVolume.class, gclid);
		Labor ht = baseService.get(Labor.class, pv.getLaborID());
		request.setAttribute("bm", pv.getProjectName()+"--"+ht.getConstructionTeam());
		String sql = " select * from tbl_volumePay where volumeId='"+gclid+"' and status<>'40'";
		if(zfid!=null){
			sql += " and id <> '"+zfid+"'";
		}
		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
		double rgzje = 0.0;
		double jxzje = 0.0;
		double clzje = 0.0;
		
		rgzje = Double.valueOf(pv.getFinalLabour());
		if(pv.getFinalDebit()!=null){
			rgzje = air.sub(rgzje, Double.valueOf(pv.getFinalDebit()));
		}
		jxzje = Double.valueOf(pv.getFinalMech());
		if(pv.getFinalDebitjx()!=null){
			jxzje = air.sub(jxzje, Double.valueOf(pv.getFinalDebitjx()));
		}
		clzje = Double.valueOf(pv.getFinalMat());
		if(pv.getFinalDebitcl()!=null){
			clzje = air.sub(clzje, Double.valueOf(pv.getFinalDebitcl()));
		}
		
		double rgkzf = rgzje;
		double clkzf = clzje;
		double jxkzf = jxzje;

		for(VolumePay zf:zfs){
			rgkzf = air.sub(rgkzf, Double.valueOf(zf.getRgmoney()));
			clkzf = air.sub(clkzf, Double.valueOf(zf.getClmoney()));
			jxkzf = air.sub(jxkzf, Double.valueOf(zf.getJxmoney()));
		}
		request.setAttribute("rgzje", rgzje+"");
		request.setAttribute("rgkzf", rgkzf+"");
		request.setAttribute("clzje", clzje+"");
		request.setAttribute("clkzf", clkzf+"");
		request.setAttribute("jxzje", jxzje+"");
		request.setAttribute("jxkzf", jxkzf+"");
		return prefix+"addPayIndexBGBL";
	}
	
	@RequestMapping("/auidLook/{id}")//审核查看页
	public String auidLook(@PathVariable("id") String id,HttpServletRequest request){
		Arith air = new Arith(); 
		VolumePay zf = baseService.get(VolumePay.class, id);
		if("10".equals(zf.getPayType())){//人工
//			List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			List<Worker> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_worker u ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
			request.setAttribute("use", JSONArray.toJSONString(ul1));
			request.setAttribute("zfid", id);
			String gzid = "";
			List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where volume_id='"+zf.getVolumeId()+"'", SalaryVolume.class);
 			for(SalaryVolume gx:gxs){
 				gzid = gx.getSalaryID();
 			}
			
 			/*List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gzid+"'", SalaryDetail.class);
 			double zje = 0.0;
 			for(SalaryDetail gzmx:gzmxs){
 				zje =air.add(zje, Double.valueOf(gzmx.getActual()));
 			}*/
 			//算出总金额和可支付金额
 			ProjectVolume pv = baseService.get(ProjectVolume.class, zf.getVolumeId());
 			double zje = 0.0;
 			zje = Double.valueOf(pv.getFinalLabour());
 			if(pv.getFinalDebit()!=null){
 				zje = air.sub(zje, Double.valueOf(pv.getFinalDebit()));
 			}
			String sql = " select * from tbl_volumePay where volumeId='"+gzid+"' and payType='10' and status<>'40'";
			List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
			double kzf = zje;
			for(VolumePay z:zfs){
				kzf = air.sub(kzf, Double.valueOf(z.getPayMoney()));
			}
			request.setAttribute("zje", zje);
			request.setAttribute("kzf", kzf);
			request.setAttribute("lx", "look");
			return prefix+"addPayIndexRg";
		}else if("40".equals(zf.getPayType())){//包工包料
			List<Worker> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_worker u ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
			request.setAttribute("use", JSONArray.toJSONString(ul1));
			String gclid = zf.getVolumeId();
			String type =zf.getPayType();
			request.setAttribute("gclid", gclid);
			request.setAttribute("type", type);
			
			String zfid = id;
			request.setAttribute("zfid", zfid);
			request.setAttribute("lx", "look");
			//算出总金额和可支付金额
			ProjectVolume pv = baseService.get(ProjectVolume.class, gclid);
			Labor ht = baseService.get(Labor.class, pv.getLaborID());
			request.setAttribute("bm", pv.getProjectName()+"--"+ht.getConstructionTeam());
			String sql = " select * from tbl_volumePay where volumeId='"+gclid+"' and status<>'40'";
			if(zfid!=null){
				sql += " and id <> '"+zfid+"'";
			}
			List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
			double rgzje = 0.0;
			double jxzje = 0.0;
			double clzje = 0.0;
			
			rgzje = Double.valueOf(pv.getFinalLabour());
			if(pv.getFinalDebit()!=null){
				rgzje = air.sub(rgzje, Double.valueOf(pv.getFinalDebit()));
			}
			jxzje = Double.valueOf(pv.getFinalMech());
			if(pv.getFinalDebitjx()!=null){
				jxzje = air.sub(jxzje, Double.valueOf(pv.getFinalDebitjx()));
			}
			clzje = Double.valueOf(pv.getFinalMat());
			if(pv.getFinalDebitcl()!=null){
				clzje = air.sub(clzje, Double.valueOf(pv.getFinalDebitcl()));
			}
			
			double rgkzf = rgzje;
			double clkzf = clzje;
			double jxkzf = jxzje;
			for(VolumePay z:zfs){
				rgkzf = air.sub(rgkzf, Double.valueOf(z.getRgmoney()));
				clkzf = air.sub(clkzf, Double.valueOf(z.getClmoney()));
				jxkzf = air.sub(jxkzf, Double.valueOf(z.getJxmoney()));
			}
			request.setAttribute("rgzje", rgzje+"");
			request.setAttribute("rgkzf", rgkzf+"");
			request.setAttribute("clzje", clzje+"");
			request.setAttribute("clkzf", clkzf+"");
			request.setAttribute("jxzje", jxzje+"");
			request.setAttribute("jxkzf", jxkzf+"");
			return prefix+"addPayIndexBGBL";
		}else{
//			List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			List<Worker> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_worker u ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
			request.setAttribute("use", JSONArray.toJSONString(ul1));
			request.setAttribute("gclid", zf.getVolumeId());
			request.setAttribute("type", zf.getPayType());
			
			request.setAttribute("zfid", id);
			request.setAttribute("lx", "look");
			
			//算出总金额和可支付金额
			ProjectVolume pv = baseService.get(ProjectVolume.class, zf.getVolumeId());
			Labor ht = baseService.get(Labor.class, pv.getLaborID());
			request.setAttribute("bm", pv.getProjectName()+"--"+ht.getConstructionTeam());
			String sql = " select * from tbl_volumePay where volumeId='"+zf.getVolumeId()+"' and payType='"+zf.getPayType()+"' and status<>'40'";
			List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
			double zje = 0.0;
			if("20".equals(zf.getPayType())){
				zje = Double.valueOf(pv.getFinalMech());
				if(pv.getFinalDebitjx()!=null){
					zje = air.sub(zje, Double.valueOf(pv.getFinalDebitjx()));
				}
			}else if("30".equals(zf.getPayType())){
				zje = Double.valueOf(pv.getFinalMat());
				if(pv.getFinalDebitcl()!=null){
					zje = air.sub(zje, Double.valueOf(pv.getFinalDebitcl()));
				}
			}else if("40".equals(zf.getPayType())){
				if(pv.getFinalPack()==null){
					zje=0.0;
				}else{
					zje = Double.valueOf(pv.getFinalPack());
				}
			}
			double kzf = zje;
			for(VolumePay z:zfs){
				kzf = air.sub(kzf, Double.valueOf(z.getPayMoney()));
			}
			request.setAttribute("zje", zje);
			request.setAttribute("kzf", kzf);
			return prefix+"addPayIndex";
		}
		
	}
	
	
	@RequestMapping("/payMoney")
	public String payMoney(HttpServletRequest request){
		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
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
	@RequestMapping("/doPay")
	@ResponseBody
	public Result doPay(HttpServletRequest request,VolumePay zf){
		String gclid = request.getParameter("gclid");
		zf.setVolumeId(gclid);
		zf.setCreateDate(new Date());
		zf.setCreateId(SecurityUtil.getUserId());
		zf.setStatus("10");
		if("10".equals(zf.getPayType())){
			zf.setRgmoney(zf.getPayMoney());
		}else if("20".equals(zf.getPayType())){
			zf.setJxmoney(zf.getPayMoney());
		}else if("30".equals(zf.getPayType())){
			zf.setClmoney(zf.getPayMoney());
		}
		baseService.save(zf);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	/*@RequestMapping("/isHaveSalary")
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
	}*/
	@RequestMapping("/delPayInfo")
	@ResponseBody
	public Result delPayInfo(HttpServletRequest request){
		String id = request.getParameter("id");
		VolumePay zf = baseService.get(VolumePay.class, id);
		baseService.delete(zf);
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/updatePayInfo")
	@ResponseBody
	public Result updatePayInfo(HttpServletRequest request,VolumePay nzf){
		String zfid = request.getParameter("zfid");
		VolumePay zf = baseService.get(VolumePay.class, zfid);
		zf.setFzrId(nzf.getFzrId());
		zf.setPayMoney(nzf.getPayMoney());
		zf.setPayWay(nzf.getPayWay());
		zf.setSkf(nzf.getSkf());
		if("10".equals(zf.getPayType())){
			zf.setRgmoney(nzf.getPayMoney());
		}else if("20".equals(zf.getPayType())){
			zf.setJxmoney(nzf.getPayMoney());
		}else if("30".equals(zf.getPayType())){
			zf.setClmoney(nzf.getPayMoney());
		}else{
			zf.setRgmoney(nzf.getRgmoney());
			zf.setJxmoney(nzf.getJxmoney());
			zf.setClmoney(nzf.getClmoney());
		}
		baseService.update(zf);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
	@RequestMapping("/start")
	@ResponseBody
	public Result start(String id) {
		VolumePay zf = baseService.get(VolumePay.class, id);
		
		User user = SecurityUtil.getUser();

		zf.setStatus("20");

		baseService.update(zf);
		String zflx="";
		if("10".equals(zf.getPayType())){
			zflx = "人工费";
		}else if("20".equals(zf.getPayType())){
			zflx = "机械费";
		}else if("30".equals(zf.getPayType())){
			zflx = "材料费";
		}else if("40".equals(zf.getPayType())){
			zflx = "包工包料费";
		}
		String gclid = zf.getVolumeId();
		String qdmc = "";
		if("10".equals(zf.getPayType())){//人工
			List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where salary_id='"+gclid+"'", SalaryVolume.class);
			if(gxs.size()>0){
				ProjectVolume gcl = baseService.get(ProjectVolume.class, gxs.get(0).getVolumeID());
				qdmc = gcl.getProjectName();
			}
		}else{//机械和材料
			ProjectVolume gcl = baseService.get(ProjectVolume.class, gclid);
			qdmc = gcl.getProjectName();
		}

        String name =qdmc + " 支付" + zflx + "【审核】";

        // businessKey
        String businessKey = zf.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("volumePay", name, variables, user.getId(), businessKey);
    }
	//记账
	@RequestMapping("/dojz")
	@ResponseBody
	public Result dojz(HttpServletRequest request){
		Arith arith = new Arith();
		String id = request.getParameter("id");
		VolumePay bx = baseService.get(VolumePay.class, id);
		
		String xmid = "";
		String content = bx.getDepartment()+"-"+"工程量清单";
		if("10".equals(bx.getPayType())){
			content += "人工费";
		}else if("20".equals(bx.getPayType())){
			content += "机械费";
		}else if("30".equals(bx.getPayType())){
			content += "材料费";
		}else if("40".equals(bx.getPayType())){
			content += "包工包料费,其中人工费："+bx.getRgmoney()+"元，材料费："+bx.getClmoney()+"元，机械费："+bx.getJxmoney()+"元";
		}
		ProjectVolume gcl = baseService.get(ProjectVolume.class, bx.getVolumeId());
		
		xmid = gcl.getProjectID();
		
		List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+xmid+"'");
		
		User u = SecurityUtil.getUser();
		//账目明细
		ProAccountDet mx = new ProAccountDet();
		mx.setCreateDate(new Date());
		mx.setAbstracts(content);
		mx.setZmtype("2");
		mx.setOutMoney(bx.getPayMoney());
		mx.setAccounter(u.getName());
		mx.setAccounterId(u.getId());
		//mx.setAuditName(u.getName());
		//mx.setAuditId(u.getId());
		mx.setStatus("10");
		if(xmz.size()<=0){//无账本，先生成账本
			ProAccount zb = new ProAccount();
			zb.setProjectId(xmid);
			if("1".equals(xmid)){
				zb.setAccountName("公司账本");
			}else{
				zb.setAccountName(gcl.getProjectName());
			}
			
			baseService.save(zb);
			zb.setAllMoney(arith.sub(0.0,Double.valueOf(bx.getPayMoney()))+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}else{
			ProAccount zb = xmz.get(0);
			zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(bx.getPayMoney()))+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}
		bx.setIsjz("10");
		baseService.update(bx);
		
		//计算是否完全支付完
		String sql = " select * from tbl_volumePay where volumeId='"+gcl.getId()+"' and payType='"+bx.getPayType()+"' and status='30'";
   		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
		Double yzje = 0.0;
		for(VolumePay zf : zfs){
			yzje = arith.add(yzje, Double.valueOf(zf.getPayMoney()));
		}
		String type = bx.getPayType();
		double zje = 0.0;
		
		double rgzje = 0.0;
		double clzje = 0.0;
		double jxzje = 0.0;
		if("10".equals(type)){
			String gzid="";
			List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where volume_id='"+bx.getVolumeId()+"'", SalaryVolume.class);
 			for(SalaryVolume gx:gxs){
 				gzid = gx.getSalaryID();
 			}
			List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gzid+"'", SalaryDetail.class);
			for(SalaryDetail gzmx:gzmxs){
				zje =arith.add(zje, Double.valueOf(gzmx.getActual()));
			}
		}else if("20".equals(type)){
			zje = Double.valueOf(gcl.getFinalMech());
			if(gcl.getFinalDebitjx()!=null){
				zje = arith.sub(zje, Double.valueOf(gcl.getFinalDebitjx()));
			}
		}else if("30".equals(type)){
			zje = Double.valueOf(gcl.getFinalMat());
			if(gcl.getFinalDebitcl()!=null){
				zje = arith.sub(zje, Double.valueOf(gcl.getFinalDebitcl()));
			}
		}else if("40".equals(type)){//包工包料
			rgzje = Double.valueOf(gcl.getFinalLabour());
			if(gcl.getFinalDebit()!=null){
				rgzje = arith.sub(rgzje, Double.valueOf(gcl.getFinalDebit()));
			}
			
			clzje = Double.valueOf(gcl.getFinalMat());
			if(gcl.getFinalDebitcl()!=null){
				clzje = arith.sub(clzje, Double.valueOf(gcl.getFinalDebitcl()));
			}
			
			jxzje = Double.valueOf(gcl.getFinalMech());
			if(gcl.getFinalDebitjx()!=null){
				jxzje = arith.sub(jxzje, Double.valueOf(gcl.getFinalDebitjx()));
			}
		}
		if("40".equals(type)){//包工包料算法
			//计算是否完全支付完
			String bgblsql = " select * from tbl_volumePay where volumeId='"+gcl.getId()+"' and status='30'";
	   		List<VolumePay> bgblzfs = baseService.findBySql(bgblsql, VolumePay.class);
	   		double rgzfs =0.0;
	   		double clzfs = 0.0;
	   		double jxzfs = 0.0;
	   		for(VolumePay bgbl : bgblzfs){
	   			rgzfs =arith.add(rgzfs, Double.valueOf(bgbl.getRgmoney()));
	   			clzfs =arith.add(clzfs, Double.valueOf(bgbl.getClmoney()));
	   			jxzfs =arith.add(jxzfs, Double.valueOf(bgbl.getJxmoney()));
	   		}
	   		if(rgzfs>=rgzje){
	   			gcl.setLabourStatus("20");
	   		}else if(rgzfs>0&&rgzfs<rgzje){
	   			gcl.setLabourStatus("10");
	   		}else{
	   			gcl.setLabourStatus(null);
	   		}
	   		if(clzfs>=clzje){
	   			gcl.setMatStatus("20");
	   		}else if(clzfs>0&&clzfs<clzje){
	   			gcl.setMatStatus("10");
	   		}else{
	   			gcl.setMatStatus(null);
	   		}
	   		if(jxzfs>=jxzje){
	   			gcl.setMechStatus("20");
	   		}else if(jxzfs>0&&jxzfs<jxzje){
	   			gcl.setMechStatus("10");
	   		}else{
	   			gcl.setMechStatus(null);
	   		}
	   		baseService.update(gcl);
		}else{
			if(yzje>0&&zje>yzje){
   				if("10".equals(bx.getPayType())){
   					gcl.setLabourStatus("10");
                }else if("20".equals(bx.getPayType())){//机械
                	gcl.setMechStatus("10");
                }else if("30".equals(bx.getPayType())){//材料
                	gcl.setMatStatus("10");
                }
   				
   				baseService.update(gcl);
	   		}else if(zje<=yzje){
	   				if("10".equals(bx.getPayType())){
	   					gcl.setLabourStatus("20");
	                }else if("20".equals(bx.getPayType())){//机械
	                	gcl.setMechStatus("20");
	                }else if("30".equals(bx.getPayType())){//材料
	                	gcl.setMatStatus("20");
	                }
	   				baseService.update(gcl);
	   		}else{
	   				if("10".equals(bx.getPayType())){
	   					gcl.setLabourStatus(null);
	                }else if("20".equals(bx.getPayType())){//机械
	                	gcl.setMechStatus(null);
	                }else if("30".equals(bx.getPayType())){//材料
	                	gcl.setMatStatus(null);
	                }
	   				baseService.update(gcl);
	   		}
		}
		Result r = new Result();
		return r;
	}
	/**
	 * 判断是否存在工资
	 * @author wzh
	 * @创建时间 2019年6月2日 上午11:41:16
	 * @return
	 */
	@RequestMapping("/getGzInfo")
	@ResponseBody
	public Result getGzInfo(HttpServletRequest request){
		String id = request.getParameter("id");
		Result r = new Result();
		List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where volume_id='"+id+"'", SalaryVolume.class);
		if(gxs.size()>0){
			Salary gz = baseService.get(Salary.class, gxs.get(0).getSalaryID());
			if(gz!=null){
				r.setData(gz);
				r.setSuccess(false);
			}else{
				r.setSuccess(true);
			}
		}else{
			r.setSuccess(true);
		}
		return r;
	}
	/**
	 * 删除工资数据
	 * @author wzh
	 * @创建时间 2019年6月2日 下午12:16:26
	 * @return
	 */
	@RequestMapping("/delSalaryByGcl")
	@ResponseBody
	public Result delSalaryByGcl(HttpServletRequest request){
		String id = request.getParameter("id");
		Result r = new Result();
		List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where volume_id='"+id+"'", SalaryVolume.class);
		if(gxs.size()>0){
			Salary gz = baseService.get(Salary.class, gxs.get(0).getSalaryID());
			if(gz!=null){
				List<SalaryVolume> gx = baseService.findBySql("select * from tbl_salary_volume where salary_id='"+gxs.get(0).getSalaryID()+"'", SalaryVolume.class);
				for(SalaryVolume g :gx){//拿到所有工程量
					ProjectVolume gcl = baseService.get(ProjectVolume.class, g.getVolumeID());
					gcl.setLabourStatus(null);
					baseService.update(gcl);
					String sql = " select * from tbl_volumePay where volumeId='"+g.getVolumeID()+"' and payType='10' ";
					List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
					for(VolumePay zf :zfs){
						baseService.delete(zf);
					}
					baseService.delete(g);
				}
				List<SalaryDetail> mxs = baseService.find(" from SalaryDetail where salary_id='"+gz.getId()+"'");
				for(SalaryDetail mx :mxs){//删除工资明细
					baseService.delete(mx);
				}
				baseService.delete(gz);
			}
		}
		return r;
	}
}
