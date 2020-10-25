package com.radish.master.controller.fixedassets;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.fixedassets.FixedAssetsPay;
import com.radish.master.entity.fixedassets.FixedAssetsPur;
import com.radish.master.service.CommonService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/fixedassets/pay")
public class FixedAssetsPayController {
	private String prefix="fixedassets/pay/";
	
	 @Resource
	 private BaseService baseService;
	 @Resource
	 private RuntimePageService runtimePageService;
	 @Resource
	    private CommonService commonService;
	 /**
	 * 固定资产支付查询页面
	 * @author wzh
	 * @创建时间 2020年10月24日 下午9:39:06
	 * @return
	 */
	 @RequestMapping("/list")
	 public String list(HttpServletRequest request){
	    request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
		return prefix+"list";
	 }
	 
	 @RequestMapping("/paylist")
	 public String paylist(HttpServletRequest request){
		request.setAttribute("purID",request.getParameter("purID"));
		request.setAttribute("payMaoney",request.getParameter("payMaoney"));
		request.setAttribute("pMoney",request.getParameter("pMoney"));
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix+"paylist";
	 }
	 @RequestMapping("/auditpaylist")
	 public String auditpaylist(HttpServletRequest request){
		request.setAttribute("purID",request.getParameter("purID"));
		return prefix+"auditpaylist";
	 }
	 
	 @RequestMapping("/auidLook/{id}")//审核查看页
	public String auidLook(@PathVariable("id") String id,HttpServletRequest request){
		 Arith air = new Arith();
		 FixedAssetsPay zf = baseService.get(FixedAssetsPay.class,id);
		 String purId = zf.getPurId();
		 String sql = " select * from tbl_fixedAssetsPay where purId='"+purId+"' and status='30'";
		 List<FixedAssetsPay> zfs = baseService.findBySql(sql, FixedAssetsPay.class);
		 
		 String zmsql = " select * from tbl_projectaccount ";
		 List<ProAccount> zm = baseService.findBySql(zmsql, ProAccount.class);
		 
		request.setAttribute("purId", purId);
		request.setAttribute("Accounts", JSONArray.toJSONString(zm));
		request.setAttribute("pMoney", request.getParameter("pMoney"));
		request.setAttribute("paytype", request.getParameter("paytype"));
		request.setAttribute("lx", request.getParameter("lx"));
		 
		 return prefix+"auditLook";
	 }
	 
	 /**
	 * 固定资产支付新增
	 * @author wzh
	 * @创建时间 2020年10月24日 下午9:39:06
	 * @return
	 */
	 @RequestMapping("/add")
	 public String add(HttpServletRequest request){
		 Arith air = new Arith();
		 String purId = request.getParameter("purId");
		 FixedAssetsPur pur = baseService.get(FixedAssetsPur.class, purId);
		 
		 String sql = " select * from tbl_fixedAssetsPay where purId='"+purId+"'";
		 List<FixedAssetsPay> zfs = baseService.findBySql(sql, FixedAssetsPay.class);
		 
		 String zmsql = " select * from tbl_projectaccount ";
		 List<ProAccount> zm = baseService.findBySql(zmsql, ProAccount.class);
		 
		 double zje = Double.valueOf(request.getParameter("pMoney"));
		 double kzf = zje;
		for(FixedAssetsPay zf:zfs){
			kzf = air.sub(kzf, Double.valueOf(zf.getPayMoney()));
		}
		request.setAttribute("zje", zje);
		request.setAttribute("kzf", kzf);
		request.setAttribute("purId", purId);
		request.setAttribute("Accounts", JSONArray.toJSONString(zm));
		request.setAttribute("pMoney", request.getParameter("pMoney"));
		request.setAttribute("paytype", pur.getFaType());
		request.setAttribute("lx", request.getParameter("lx"));
		return prefix+"add";
	 }
	 
	 
	 /**
	 * 固定资产支付 保存修改
	 * @author wzh
	 * @创建时间 2020年10月24日 下午9:39:06
	 * @return
	 */
	 @RequestMapping("/save")
	 @ResponseBody
	 public Result save(HttpServletRequest request,FixedAssetsPay zf){
		String purId = request.getParameter("purId");
		zf.setPurId(purId);
		zf.setCreateDate(new Date());
		zf.setCreateId(SecurityUtil.getUserId());
		zf.setStatus("10");
		baseService.save(zf);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	 }
	 
	 
	 /**
	 * 固定资产支付 获取数据
	 * @author wzh
	 * @创建时间 2020年10月24日 下午9:39:06
	 * @return
	 */
	 @RequestMapping("/load")
	 @ResponseBody
	 public Result load(HttpServletRequest request){
		String id = request.getParameter("id");
		FixedAssetsPay zf = baseService.get(FixedAssetsPay.class, id);
		Result r = new Result();
		r.setSuccess(true);
		r.setData(zf);
		return r;
	 }
	 
	 /**
	 * 固定资产支付 删除
	 * @author wzh
	 * @创建时间 2020年10月24日 下午9:39:06
	 * @return
	 */
	 @RequestMapping("/delete")
	 @ResponseBody
	 public Result delete(HttpServletRequest request){
		String id = request.getParameter("id");
		FixedAssetsPay zf = baseService.get(FixedAssetsPay.class, id);
		baseService.delete(zf);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	 }
	 
	 /**
	 * 固定资产支付 提交审核
	 * @author wzh
	 * @创建时间 2020年10月24日 下午9:39:06
	 * @return
	 */

		@RequestMapping("/start")
		@ResponseBody
		public Result start(String id) {
			FixedAssetsPay zf = baseService.get(FixedAssetsPay.class, id);
			
			User user = SecurityUtil.getUser();

			zf.setStatus("20");

			baseService.update(zf);
			String qdmc ="";
			if("10".equals(zf.getPayType())){
				qdmc = "固定资产";
			}else if("20".equals(zf.getPayType())){
				qdmc = "器具工具";
			}else if("30".equals(zf.getPayType())){
				qdmc = "办公用品";
			}
			
	        String name =qdmc + " 支付：" +zf.getContent()+ "【审核】";

	        // businessKey
	        String businessKey = zf.getId();

	        // 配置流程变量
	        Map<String, Object> variables = new HashMap<>();
	        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
	        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
	        variables.put("taskName", name);

	        // 启动流程
	        return runtimePageService.startProcessInstanceByKey("FixPay", name, variables, user.getId(), businessKey);
	    }
	 /**
	 * 固定资产支付 记账
	 * @author wzh
	 * @创建时间 2020年10月24日 下午9:39:06
	 * @return
	 */
		//记账
		@RequestMapping("/dojz")
		@ResponseBody
		public Result dojz(HttpServletRequest request){
			Arith arith = new Arith();
			String id = request.getParameter("id");
			FixedAssetsPay bx = baseService.get(FixedAssetsPay.class, id);
			
			String content = "";
			if("10".equals(bx.getPayType())){
				content += "固定资产";
			}else if("20".equals(bx.getPayType())){
				content += "器具工具";
			}else if("30".equals(bx.getPayType())){
				content += "办公用品";
			}
			content= content+"支付："+bx.getContent();
			ProAccount zb = baseService.get(ProAccount.class, bx.getAccountId());
			
			
			User u = SecurityUtil.getUser();
			//账目明细
			ProAccountDet mx = new ProAccountDet();
			mx.setCreateDate(new Date());
			mx.setAbstracts(content);
			mx.setZmtype("2");
			mx.setOutMoney(bx.getPayMoney());
			mx.setAccounter(u.getName());
			mx.setAccounterId(u.getId());
			mx.setStatus("10");
			zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(bx.getPayMoney()))+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
			bx.setIsjz("10");
			baseService.update(bx);
			Result r = new Result();
			return r;
		}
}
