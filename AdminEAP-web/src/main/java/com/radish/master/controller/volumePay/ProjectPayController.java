package com.radish.master.controller.volumePay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.Position.Bias;

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
import com.cnpc.framework.utils.CodeException;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Instock;
import com.radish.master.entity.InstockDet;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.Project;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.skillManage.PlanFile;
import com.radish.master.entity.volumePay.InstockChannel;
import com.radish.master.entity.volumePay.InstockPay_Rt;
import com.radish.master.entity.volumePay.ProjectPay;
import com.radish.master.entity.volumePay.ProjectPayDet;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;
import com.radish.master.system.GUID;

@Controller
@RequestMapping("/projectpay")
public class ProjectPayController {
	private String prefix="/VolumePay/ProjectPay/";
	private Arith ari;
	@Resource
    private BudgetService budgetService;
	@Resource
	private RuntimePageService runtimePageService;
	 
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix +"addIndex";
	}
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix +"list";
	}
	
	@RequestMapping("/paymx")
	public String paymx(HttpServletRequest request){
		String payid = request.getParameter("payid");
		request.setAttribute("payid",payid);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix +"paymx";
	}
	@RequestMapping("/auidt/{id}")//审核查看页
	public String auidLook(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("payid",id);
		return prefix+"auidLook";
		
	}
	@RequestMapping("/auidtMx/{id}")//审核查看页
	public String auidtMx(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("payid",id);
		return prefix+"auidLookMx";
		
	}
	@RequestMapping("/editPay")
	public String editPay(HttpServletRequest request){
		String mxid = request.getParameter("mxid");
		request.setAttribute("mxid",mxid);
		return prefix +"editPay";
	}
	
	@RequestMapping("/doJs")
	@ResponseBody
	public Result doJs(HttpServletRequest request,ProjectPay fk) throws CodeException{
		Result r = new Result();
		String pid = fk.getProjectId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<InstockChannel> rks = baseService.find(" from InstockChannel where projectId='"+pid+"'");
		if(rks.size()<=0){
			r.setMessage("此项目暂无可付款的入库信息，无法生成付款明细");
			r.setSuccess(false);
			return r;
		}
		//按结束时间排序
		List<ProjectPay> fks = baseService.find(" from ProjectPay where projectId='"+pid+"' order by enddate desc");
		if(fks.size()>0){//开始时间为所有记录最大的结束时间,不然属于第一次支付，无开始时间
			if(fks.get(0).getEnddate().after(fk.getEnddate())){//上次支付结束时间 在 这次支付结束时间之后
				String scsj = sdf.format(fks.get(0).getEnddate());
				r.setMessage("此次结束小于上次结束时间，请重新选择截止时间。（上次截至时间："+scsj+"）");
				r.setSuccess(false);
				return r;
			}
			fk.setStartdate(fks.get(0).getEnddate());
			if(!"50".equals(fks.get(0).getStatus())){//如果上一条未审批完成，不能生成新数据
				r.setMessage("此项目最新付款数据还未完成，不能生成新的付款数据");
				r.setSuccess(false);
				return r;
			}
		}
		fk.setStatus("10");
		//拿到指定开始时间到结束时间内的数据
		
		String sql = " from InstockChannel where projectId='"+pid+"' ";
		if(fk.getStartdate()!=null){
			sql += " and indate > '"+sdf.format(fk.getStartdate())+"' and indate <= '"+sdf.format(fk.getEnddate())+"' ";
		}else{
			sql += " and indate  > '1990-01-01' and indate <= '"+sdf.format(fk.getEnddate())+"' ";
		}
		List<InstockChannel> sjrks = baseService.find(sql);
		
		
		//将上一次未付完的明细加入到这次
		List<ProjectPayDet> mxs = new ArrayList<ProjectPayDet>();
		String gyss = "";
		if(fks.size()>0){
			String scfkid = fks.get(0).getId();
			List<ProjectPayDet> scmxs = baseService.find(" from ProjectPayDet where projectPayId='"+scfkid+"'");
			for(ProjectPayDet scmx : scmxs){
				if(scmx.getQm()!=null && Double.valueOf(scmx.getQm())>0){
					ProjectPayDet mx = new ProjectPayDet();
					mx.setId(GUID.genTxNo(16));
					mx.setChannelId(scmx.getChannelId());
					mx.setChannelName(scmx.getChannelName());
					mx.setQc(scmx.getQm());//上次期末放入这次期初
					mx.setBq("0");
					mx.setFk("0");
					mx.setQm(scmx.getQm());//未付款，无本期的期末等于期初
					
					mxs.add(mx);
					gyss += scmx.getChannelName()+"/";//方便查看本期是否有相同供应商
				}
			}
		}
		
		//加入本期新增(供应商名字相同则相加，无相同则新增)
		for(int j = 0;j<sjrks.size();j++){//时间段入库的入库单
			InstockChannel sjrk = sjrks.get(j);
			if(gyss.indexOf(sjrk.getSupplier()+"/")>=0){//已存在
				for(int i = 0;i<mxs.size();i++){
					if(sjrk.getSupplier().equals(mxs.get(i).getChannelName())){//名称相同
						
						//本期相加
						mxs.get(i).setBq(ari.add(Double.valueOf(sjrk.getZj()), Double.valueOf(mxs.get(i).getBq()))+"");
						//期末相加
						mxs.get(i).setQm(ari.add(Double.valueOf(sjrk.getZj()), Double.valueOf(mxs.get(i).getQm()))+"");
						//插入关系表
						InstockPay_Rt gx = new InstockPay_Rt();
						gx.setInstockDetId(sjrk.getMxid());
						gx.setPayDetId(mxs.get(i).getId());
						baseService.save(gx);
						
						if(mxs.get(i).getUseFact().indexOf((sjrk.getMatName()+"("+sjrk.getMatStandard()+")材料款"))<0){
							//用途增加
							mxs.get(i).setUseFact(mxs.get(i).getUseFact()+","+sjrk.getMatName()+"("+sjrk.getMatStandard()+")材料款");
						
						}
					}
				}
			}else{//未存在，新增
				ProjectPayDet mx = new ProjectPayDet();
				mx.setId(GUID.genTxNo(16));
				mx.setChannelId(sjrk.getQdid());
				mx.setChannelName(sjrk.getSupplier());
				mx.setQc("0");//上次期末放入这次期初
				mx.setBq(sjrk.getZj());
				mx.setFk("0");
				mx.setQm(sjrk.getZj());//未付款，无本期的期末等于期初
				mx.setUseFact(sjrk.getMatName()+"("+sjrk.getMatStandard()+")材料款");
				mxs.add(mx);
				gyss += sjrk.getSupplier()+"/";//方便查看本期是否有相同供应商
				
				//插入关系表
				InstockPay_Rt gx = new InstockPay_Rt();
				gx.setInstockDetId(sjrk.getMxid());
				gx.setPayDetId(mx.getId());
				baseService.save(gx);
			}
		}
		baseService.save(fk);
		for (ProjectPayDet mx : mxs) {
			mx.setProjectPayId(fk.getId());
			mx.setStatus("10");
			mx.setFkfs("10");
			//放入抵扣金额
			//获得预付金额
			List<Map<String,Object>> yfje = baseService.findMapBySql("select ifnull(sum(yf.money),0) as yfje  from tbl_advance yf where yf.status='40' and yf.channelName='"+mx.getChannelName()+"'");
			//获得已抵扣总额
			List<Map<String,Object>> dkje = baseService.findMapBySql("select ifnull(sum(yf.dkmoney),0) as dkje  from tbl_projectPay_det yf where yf.status='30' and yf.channelName='"+mx.getChannelName()+"'");
			Double yf = 0.0;
			Double dk = 0.0;
			if(yfje!=null&&yfje.size()>0&&yfje.get(0).get("yfje")!=null){
				yf = Double.valueOf(yfje.get(0).get("yfje").toString());
			}
			if(dkje!=null&&dkje.size()>0&&dkje.get(0).get("dkje")!=null){
				dk = Double.valueOf(dkje.get(0).get("dkje").toString());
			}
			Double syje = ari.sub(yf, dk);
			if(syje>0){//抵扣>本期---本期   抵扣<本期 -----抵扣
				Double bq = ari.add(Double.valueOf(mx.getQc()), Double.valueOf(mx.getBq()));;
				if(syje>=bq){
					mx.setDkmoney(bq.toString());
					mx.setQm("0");
				}else{
					mx.setDkmoney(syje.toString());
					mx.setQm(ari.sub(bq, syje)+"");
				}
			}else{
				mx.setDkmoney("0");
			}
			baseService.save(mx);
		}
		r.setSuccess(true);
		r.setMessage("成功生成明细");
		r.setCode(fk.getId());
		return r;
	}
	
	@RequestMapping("/start")
	@ResponseBody
	public Result start(String id) {
		ProjectPay zf = baseService.get(ProjectPay.class, id);
		Project p = baseService.get(Project.class, zf.getProjectId());
		zf.setStatus("20");
		User user = SecurityUtil.getUser();
		baseService.update(zf);
		

        String name =p.getProjectName()+"采购商款项【审核】";

        // businessKey
        String businessKey = zf.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("projectPay", name, variables, user.getId(), businessKey);
    }
	@RequestMapping("/getZf")
	@ResponseBody
	public Map<String,Object> getZf(String id){
		Map<String,Object> m = new HashMap<String,Object>();
		ProjectPay zf = baseService.get(ProjectPay.class, id);
		m.put("zf", zf);
		String projectId = zf.getProjectId();
		//查询对应的账目余额
		List<ProAccount> zms = baseService.find(" from ProAccount where projectId='"+projectId+"'");
		if(zms.size()>0){
			m.put("ye", "账目余额："+zms.get(0).getAllMoney()+"元");
		}else{
			m.put("ye", "未生成账目信息");
		}
		return m;
	}
	@RequestMapping("/getMx")
	@ResponseBody
	public ProjectPayDet getMx(String mxid){
		ProjectPayDet zf = baseService.get(ProjectPayDet.class, mxid);
		return zf;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(String id){
		Result r = new Result();
		ProjectPay zf = baseService.get(ProjectPay.class, id);
		List<ProjectPayDet> scmxs = baseService.find(" from ProjectPayDet where projectPayId='"+id+"'");
		for(ProjectPayDet mx : scmxs){
			baseService.delete(mx);
		}
		baseService.delete(zf);
		return r;
	}
	
	@RequestMapping("/editMxInfo")
	@ResponseBody
	public Result editMxInfo(String id,ProjectPayDet mx){
		Result r = new Result();
		
		ProjectPayDet zf = baseService.get(ProjectPayDet.class, id);
		zf.setContent(mx.getContent());
		Double je = ari.add(Double.valueOf(zf.getQc()), Double.valueOf(zf.getBq()));
		je = ari.sub(je, Double.valueOf(zf.getDkmoney()));
		if(ari.sub(je, Double.valueOf(mx.getFk()))<0){
			r.setSuccess(false);
			r.setMessage("付款金额过多，请重新输入");
			return r;
		}
		zf.setQm(ari.sub(je, Double.valueOf(mx.getFk()))+"");
		zf.setFk(mx.getFk());
		baseService.update(zf);
		
		return r;
	}
	
	@RequestMapping("/startZf")
	@ResponseBody
	public Result startZf(String id) {
		ProjectPayDet mx = baseService.get(ProjectPayDet.class, id);
		ProjectPay zf = baseService.get(ProjectPay.class, mx.getProjectPayId());
		Project p = baseService.get(Project.class, zf.getProjectId());
		mx.setStatus("50");
		User user = SecurityUtil.getUser();
		baseService.update(mx);
		

        String name =p.getProjectName()+"|"+mx.getChannelName()+"【采购商支付审核】";

        // businessKey
        String businessKey = mx.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("projectPayMx", name, variables, user.getId(), businessKey);
    }
	@RequestMapping("/getZfMxSh")
	@ResponseBody
	public Map<String, Object> getZfMxSh(String id){
		Map<String, Object> m = new HashMap<String, Object>();
		ProjectPayDet mx = baseService.get(ProjectPayDet.class, id);
		ProjectPay zf = baseService.get(ProjectPay.class, mx.getProjectPayId());
		
		m.put("projectId", zf.getProjectId());
		m.put("gys", mx.getChannelName());
		m.put("je", mx.getFk());
		m.put("fkfs", mx.getFkfs());
		m.put("qm", mx.getQm());
		m.put("qc", mx.getQc());
		m.put("dkmoney", mx.getDkmoney());
		//获得预付金额
		//List<String> yfje = baseService.find("select sum(yf.money) as yfje from com.radish.master.entity.volumePay.Advance yf where yf.status='40' and yf.channelName='"+mx.getChannelName()+"'");
		List<Map<String,Object>> yfje = baseService.findMapBySql("select ifnull(sum(yf.money),0) as yfje  from tbl_advance yf where yf.status='40' and yf.channelName='"+mx.getChannelName()+"'");
		//获得已抵扣总额
		//List<String> dkje = baseService.find("select sum(yf.dkmoney) as dkje from com.radish.master.entity.volumePay.ProjectPayDet yf where yf.status='30' and yf.channelName='"+mx.getChannelName()+"' ");
		List<Map<String,Object>> dkje = baseService.findMapBySql("select ifnull(sum(yf.dkmoney),0) as dkje  from tbl_projectPay_det yf where yf.status='30' and yf.channelName='"+mx.getChannelName()+"'");
		
		m.put("zyfk", yfje.get(0).get("yfje"));
		m.put("ydk",dkje.get(0).get("dkje"));
		return m;
	}
	
	@RequestMapping("/saveFkfs")
	@ResponseBody
	public Result saveFkfs(HttpServletRequest request,String fkfs,String dkmoney,String id){
		Result r = new Result();
		ProjectPayDet mx = baseService.get(ProjectPayDet.class, id);
		mx.setFkfs(fkfs);
		//mx.setDkmoney(dkmoney);
		baseService.update(mx);
		return r;
	}
	@RequestMapping("/EditFjsl")
	@ResponseBody
	public Result EditFjsl(HttpServletRequest request){
		Result r = new Result();
		String fjsl = request.getParameter("fjsl");
		String zfid = request.getParameter("zfid");
		ProjectPayDet mx = baseService.get(ProjectPayDet.class, zfid);
		mx.setFjsl(fjsl);
		baseService.update(mx);
		return r;
	}
	
	@RequestMapping("/doJz")
	@ResponseBody
	public Result doJz(HttpServletRequest request){
		Arith arith = new Arith();
		Result r = new Result();
		String id =request.getParameter("id");
		ProjectPayDet zfmx = baseService.get(ProjectPayDet.class, id);
		ProjectPay zf = baseService.get(ProjectPay.class, zfmx.getProjectPayId());
		Project xm = baseService.get(Project.class,zf.getProjectId());
		List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+zf.getProjectId()+"'");
		
		Double zfje = Double.valueOf(zfmx.getFk());
		User u = SecurityUtil.getUser();
		//账目明细
		ProAccountDet mx = new ProAccountDet();
		mx.setCreateDate(new Date());
		mx.setAbstracts(zfmx.getChannelName()+"(供应商款项)");
		mx.setZmtype("2");
		mx.setOutMoney(zfje+"");
		mx.setAccounter(u.getName());
		mx.setAccounterId(u.getId());
		//mx.setAuditName(u.getName());
		//mx.setAuditId(u.getId());
		mx.setStatus("10");
		if(xmz.size()<=0){//无账本，先生成账本
			ProAccount zb = new ProAccount();
			zb.setProjectId(zf.getProjectId());
			zb.setAccountName(xm.getProjectName());
			baseService.save(zb);
			zb.setAllMoney(arith.sub(0.0,zfje)+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}else{
			ProAccount zb = xmz.get(0);
			zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),zfje)+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}
		zfmx.setIsrz("20");
		baseService.update(zfmx);
		return r;
		
	}
	/**
	 * 根据输入的百分比统一设置支付明细的本期付款金额
	 */
	@RequestMapping("/doJsAllMx")
	@ResponseBody
	public Result doJsAllMx(HttpServletRequest request,Double bfb){
		Result r= new Result();
		String pid = request.getParameter("pid");
		List<ProjectPayDet> scmxs = baseService.find(" from ProjectPayDet where projectPayId='"+pid+"'");
		for(ProjectPayDet zf : scmxs){
			Double je = ari.add(Double.valueOf(zf.getQc()), Double.valueOf(zf.getBq()));
			je = ari.sub(je, Double.valueOf(zf.getDkmoney()));
			if(je>0){
				zf.setFk(ari.mul(bfb, je)+"");
			}else{
				zf.setFk("0");
			}
			
			zf.setQm(ari.sub(je, Double.valueOf(zf.getFk()))+"");
			baseService.update(zf);
		}
		r.setSuccess(true);
		return r;
	}
}
