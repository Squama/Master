package com.radish.master.controller.volumePay;

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
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
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
				if("60".equals(gz.getStatus())){
					request.setAttribute("gz", JSONArray.toJSONString(gz));
					List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gz.getId()+"'", SalaryDetail.class);
					double zje = 0.0;
					for(SalaryDetail gzmx:gzmxs){
						zje =air.add(zje, Double.valueOf(gzmx.getActual()));
					}
					String sql = " select * from tbl_volumePay where volumeId='"+gz.getId()+"' and payType='10' and status<>'40'";
					List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
					double kzf = zje;
					for(VolumePay zf:zfs){
						kzf = air.sub(kzf, Double.valueOf(zf.getPayMoney()));
					}
					request.setAttribute("zje", zje);
					request.setAttribute("kzf", kzf);
				}
				
			}
			return prefix+"payListRg";
		}
		return prefix+"payList";
	}
	@RequestMapping("/addPayInfo")//非人工支付
	public String addPayInfo(HttpServletRequest request){
		Arith air = new Arith();
		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
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
		List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("use", JSONArray.toJSONString(ul1));
		String gclid = request.getParameter("gclid");
		request.setAttribute("gclid", gclid);
		
		String gzid = request.getParameter("gzid");
		request.setAttribute("gzid", gzid);
		
		String zfid = request.getParameter("zfid");
		String lx = request.getParameter("lx");
		request.setAttribute("zfid", zfid);
		request.setAttribute("lx", lx);
		
		List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gzid+"'", SalaryDetail.class);
		double zje = 0.0;
		for(SalaryDetail gzmx:gzmxs){
			zje =air.add(zje, Double.valueOf(gzmx.getActual()));
		}
		String sql = " select * from tbl_volumePay where volumeId='"+gzid+"' and payType='10' and status<>'40'";
		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
		double kzf = zje;
		for(VolumePay zf:zfs){
			kzf = air.sub(kzf, Double.valueOf(zf.getPayMoney()));
		}
		request.setAttribute("zje", zje);
		request.setAttribute("kzf", kzf);
		ProjectVolume pv = baseService.get(ProjectVolume.class, gclid);
		Labor ht = baseService.get(Labor.class, pv.getLaborID());
		request.setAttribute("bm", pv.getProjectName()+"--"+ht.getConstructionTeam());
		
		
		return prefix+"addPayIndexRg";
	}
	
	@RequestMapping("/auidLook/{id}")//审核查看页
	public String auidLook(@PathVariable("id") String id,HttpServletRequest request){
		Arith air = new Arith(); 
		VolumePay zf = baseService.get(VolumePay.class, id);
		if("10".equals(zf.getPayType())){//人工
			List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("use", JSONArray.toJSONString(ul1));
			request.setAttribute("zfid", id);
			String gzid = zf.getVolumeId();
			
			
			List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gzid+"'", SalaryDetail.class);
			double zje = 0.0;
			for(SalaryDetail gzmx:gzmxs){
				zje =air.add(zje, Double.valueOf(gzmx.getActual()));
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
		}else{
			List<User> ul1 = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
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
}
