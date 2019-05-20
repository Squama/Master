package com.radish.master.controller.volumePay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.project.Salary;
import com.radish.master.service.CommonService;
import com.radish.master.system.Arith;

/**
 * 工资税金支付
 * @author wzh
 * @创建时间 2019年5月20日 下午2:23:13
 * @return
 */
@Controller
@RequestMapping("/taxpay")
public class TaxPayController {
	String prefix = "/VolumePay/Taxpay/";
	@Autowired
	private BaseService baseService;
	
	@Resource
	private RuntimePageService runtimePageService;
	@Resource
    private CommonService commonService;

	
	@RequestMapping("/addindex")
	public String addindex(HttpServletRequest request){
		//request.setAttribute("jkr", SecurityUtil.getUserId());
		request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
		return prefix+"addindex";
	}
	@RequestMapping("/chooseZffs")
	public String chooseZffs(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix +"zffs";
	}
	@RequestMapping("/auidtMx/{id}")
	public String auidtMx(HttpServletRequest request,@PathVariable("id") String id){
		request.setAttribute("id", id);
		return prefix+"auidtMx";
	}
	
	@RequestMapping("/start")
	@ResponseBody
	public Result start(String id) {
		Salary zf = baseService.get(Salary.class, id);
		
		User user = SecurityUtil.getUser();

		zf.setSjstatus("20");

		baseService.update(zf);
		String zflx="";
		if("10".equals(zf.getType())){
			zflx = "专业作业班组班组";
		}else if("20".equals(zf.getType())){
			zflx = "管理人员";
		}else if("30".equals(zf.getType())){
			zflx = "点工班组";
		}else if("40".equals(zf.getType())){
			zflx = "机关人员";
		}

        String name = zflx + " 工资单税金支付【审核】";

        // businessKey
        String businessKey = zf.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("taxPay", name, variables, user.getId(), businessKey);
    }
	@RequestMapping("/dotrue")
	@ResponseBody
	public Result dotrue(String id) {
		Result r = new Result();
		Salary zf = baseService.get(Salary.class, id);
		zf.setSjstatus("50");
		baseService.update(zf);
		return r;
	}
	//记账
	@RequestMapping("/dojz")
	@ResponseBody
	public Result dojz(HttpServletRequest request){
		Arith arith = new Arith();
		String id = request.getParameter("id");
		Salary j = baseService.get(Salary.class, id);
		String je = request.getParameter("je");
		String xmid = "";
		String content = "";
		if("40".equals(j.getType())){//公司账本 机关人员工资
			xmid = "1";
			content="机关人员工资税金支付";
		}else{//项目账本 其他类型人员工资
			xmid = j.getProjectID();
			if("10".equals(j.getType())){
				content="专业作业班组工资税金支付";
			}else if("20".equals(j.getType())){
				content="管理人员工资税金支付";
			}else if("30".equals(j.getType())){
				content="点工班组工资税金支付";
			}else if("50".equals(j.getType())){
				content="门卫机修工资税金支付";
			}
		}
		
		List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+xmid+"'");
		
		User u = SecurityUtil.getUser();
		//账目明细
		ProAccountDet mx = new ProAccountDet();
		mx.setCreateDate(new Date());
		mx.setAbstracts(content);
		mx.setZmtype("2");
		mx.setOutMoney(je);
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
				zb.setAccountName(j.getProjectName());
			}
			
			baseService.save(zb);
			zb.setAllMoney(arith.sub(0.0,Double.valueOf(je))+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}else{
			ProAccount zb = xmz.get(0);
			zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(je) )+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}
		j.setSjisjz("10");
		baseService.update(j);
		Result r = new Result();
		return r;
	}
	
}
