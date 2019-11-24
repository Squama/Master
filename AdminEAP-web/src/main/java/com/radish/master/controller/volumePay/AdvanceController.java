package com.radish.master.controller.volumePay;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.Project;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseDet;
import com.radish.master.entity.volumePay.Advance;
import com.radish.master.entity.volumePay.Loans;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

/**
 * 预付款
 * @author wzh
 * @创建时间 2019年9月21日 上午10:17:08
 * @return
 */
@Controller
@RequestMapping("/advance")
public class AdvanceController {

	private String prefix="/VolumePay/advance/";
	
	@Resource
    private BudgetService budgetService;
	
	@Autowired
	private BaseService baseService;
	
	@Resource
	private RuntimePageService runtimePageService;
	
	@RequestMapping("/index")
	public String index(){
		return prefix + "index";
	}
	@RequestMapping("/addindex")
	public String addindex(HttpServletRequest request) throws UnsupportedEncodingException{
		String type = request.getParameter("type");
		String cgid = new String(request.getParameter("cgid").getBytes("iso-8859-1"), "utf-8");  
		request.setAttribute("type", type);
		request.setAttribute("cgid", cgid);
		String cg = request.getParameter("cg");
		request.setAttribute("cg", cg);
		
		//PurchaseDet cgmx = baseService.get(PurchaseDet.class, cgid);
		request.setAttribute("gys", cgid);
		
		/*//获得预付金额
		//List<String> yfje = baseService.find("select sum(yf.money) as yfje from com.radish.master.entity.volumePay.Advance yf where yf.status='40' and yf.channelName='"+mx.getChannelName()+"'");
		List<Map<String,Object>> yfje = baseService.findMapBySql("select ifnull(sum(yf.money),0) as yfje  from tbl_advance yf where yf.status='40' and yf.channelName='"+cgid+"'");
		//获得已抵扣总额
		//List<String> dkje = baseService.find("select sum(yf.dkmoney) as dkje from com.radish.master.entity.volumePay.ProjectPayDet yf where yf.status='30' and yf.channelName='"+mx.getChannelName()+"' ");
		List<Map<String,Object>> dkje = baseService.findMapBySql("select ifnull(sum(yf.dkmoney),0) as dkje  from tbl_projectPay_det yf where yf.status='30' and yf.channelName='"+cgid+"'");
		
		request.setAttribute("zyfk",  yfje.get(0).get("yfje"));
		request.setAttribute("ydk",  dkje.get(0).get("dkje"));*/
		
		return prefix + "addindex";
	}
	@RequestMapping("/dkindex")
	public String dkindex(HttpServletRequest request) throws UnsupportedEncodingException{
		String cgid = new String(request.getParameter("cgid").getBytes("iso-8859-1"), "utf-8"); 
		request.setAttribute("cgid", cgid);
		//PurchaseDet cgmx = baseService.get(PurchaseDet.class, cgid);
		request.setAttribute("gys", cgid);
		
		/*//获得预付金额
		//List<String> yfje = baseService.find("select sum(yf.money) as yfje from com.radish.master.entity.volumePay.Advance yf where yf.status='40' and yf.channelName='"+mx.getChannelName()+"'");
		List<Map<String,Object>> yfje = baseService.findMapBySql("select ifnull(sum(yf.money),0) as yfje  from tbl_advance yf where yf.status='40' and yf.channelName='"+cgid+"'");
		//获得已抵扣总额
		//List<String> dkje = baseService.find("select sum(yf.dkmoney) as dkje from com.radish.master.entity.volumePay.ProjectPayDet yf where yf.status='30' and yf.channelName='"+mx.getChannelName()+"' ");
		List<Map<String,Object>> dkje = baseService.findMapBySql("select ifnull(sum(yf.dkmoney),0) as dkje  from tbl_projectPay_det yf where yf.status='30' and yf.channelName='"+cgid+"'");
		
		request.setAttribute("zyfk",  yfje.get(0).get("yfje"));
		request.setAttribute("ydk",  dkje.get(0).get("dkje"));*/
		
		return prefix + "dkindex";
	}
	@RequestMapping("/loadyfdk")
	@ResponseBody
	public Map<String,Object> loadyfdk(HttpServletRequest request){
		String gys = request.getParameter("gys");
		//获得预付金额
		//List<String> yfje = baseService.find("select sum(yf.money) as yfje from com.radish.master.entity.volumePay.Advance yf where yf.status='40' and yf.channelName='"+mx.getChannelName()+"'");
		List<Map<String,Object>> yfje = baseService.findMapBySql("select ifnull(sum(yf.money),0) as yfje  from tbl_advance yf where yf.status='40' and yf.channelName='"+gys+"'");
		//获得已抵扣总额
		//List<String> dkje = baseService.find("select sum(yf.dkmoney) as dkje from com.radish.master.entity.volumePay.ProjectPayDet yf where yf.status='30' and yf.channelName='"+mx.getChannelName()+"' ");
		List<Map<String,Object>> dkje = baseService.findMapBySql("select ifnull(sum(yf.dkmoney),0) as dkje  from tbl_projectPay_det yf where yf.status='30' and yf.channelName='"+gys+"'");
		Map<String,Object>	m = new HashMap<String, Object>();
		m.put("zyfk", yfje.get(0).get("yfje"));
		m.put("ydk",dkje.get(0).get("dkje"));
		return m;
	}
	
	
	@RequestMapping("/addyfxx")
	public String  addyfxx(HttpServletRequest request) throws UnsupportedEncodingException{
		String yfid = request.getParameter("yfid");
		request.setAttribute("yfid", yfid);
		String cgid = new String(request.getParameter("cgid").getBytes("iso-8859-1"), "utf-8"); 
		request.setAttribute("cgid", cgid);
		//PurchaseDet cgmx = baseService.get(PurchaseDet.class, cgid);
		request.setAttribute("channelName", cgid);
		
		String cg = request.getParameter("cg");
		request.setAttribute("cg", cg);
		return prefix + "addpage";
	}
	//审批页
	@RequestMapping("/auidtMx/{id}")
	public String auidtMx(HttpServletRequest request,@PathVariable("id") String id){
		request.setAttribute("yfid", id);
		return prefix + "Auidtpage";
	}
	@RequestMapping("/zffs")
	public String zffs(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix +"zffs";
	}
	
	@RequestMapping("/saveyfxx")
	@ResponseBody
	public Result saveyfxx(HttpServletRequest request,Advance yf){
		Result r = new Result();
		User u = SecurityUtil.getUser();
		yf.setSqr(u.getName());
		yf.setSqrid(u.getId());
		yf.setSqtime(new Date());
		yf.setIsdk("10");
		yf.setIsjz("10");
		yf.setStatus("10");
		yf.setSqmoney(yf.getMoney());
		/*PurchaseDet cgmx = baseService.get(PurchaseDet.class, yf.getPurdetid());
		yf.setMat(cgmx.getMatName());
		yf.setMatStandard(cgmx.getMatStandard());*/
		baseService.save(yf);
		return r;
	}
	@RequestMapping("/updateyfxx")
	@ResponseBody
	public Result updateyfxx(HttpServletRequest request,Advance yf){
		Result r = new Result();
		Advance y = baseService.get(Advance.class, yf.getId());
		y.setMoney(yf.getMoney());
		baseService.update(y);
		return r;
	}
	
	@RequestMapping("/delyfxx")
	@ResponseBody
	public Result delyfxx(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Advance yf = baseService.get(Advance.class, id);
		baseService.delete(yf);
		return r;
	}
	
	@RequestMapping("/loadyfxx")
	@ResponseBody
	public Result loadyfxx(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Advance yf = baseService.get(Advance.class, id);
		r.setData(yf);
		return r;
	}
	
	@RequestMapping("/dojz")
	@ResponseBody
	public Result dojz(HttpServletRequest request){
		Arith arith = new Arith();
		Result r = new Result();
		String id = request.getParameter("id");
		Advance yf = baseService.get(Advance.class, id);
		
		//PurchaseDet cgmx = baseService.get(PurchaseDet.class, yf.getPurdetid());
		Purchase cg = baseService.get(Purchase.class,yf.getPurdetid());
		String proid = cg.getProjectID();
		
		
		Project xm = baseService.get(Project.class,proid);
		List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+proid+"'");
		
		User u = SecurityUtil.getUser();
		//账目明细
		ProAccountDet mx = new ProAccountDet();
		mx.setCreateDate(yf.getPaytime());
		mx.setAbstracts(yf.getChannelName()+"(供应商预付款项支付)");
		mx.setZmtype("2");
		mx.setOutMoney(yf.getMoney());
		mx.setAccounter(u.getName());
		mx.setAccounterId(u.getId());
		//mx.setAuditName(u.getName());
		//mx.setAuditId(u.getId());
		mx.setStatus("10");
		if(xmz.size()<=0){//无账本，先生成账本
			ProAccount zb = new ProAccount();
			zb.setProjectId(proid);
			zb.setAccountName(xm.getProjectName());
			baseService.save(zb);
			zb.setAllMoney(arith.sub(0.0,Double.valueOf(yf.getMoney()))+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}else{
			ProAccount zb = xmz.get(0);
			zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(yf.getMoney()) )+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}
		yf.setIsjz("20");
		baseService.update(yf);
		return r;
	}
	
	@RequestMapping("/submit")
	@ResponseBody
	public Result submit(HttpServletRequest request,Loans jk){
		String id = request.getParameter("id");
		Advance yf = baseService.get(Advance.class, id);
		
		User user = SecurityUtil.getUser();
		yf.setStatus("30");
		String name ="供应商："+yf.getChannelName()+"预付款【审核】";

        // businessKey
        String businessKey = yf.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        runtimePageService.startProcessInstanceByKey("Advance", name, variables, user.getId(), businessKey);
			
		baseService.update(yf);
		Result r = new Result();
		return r;
	}
}
