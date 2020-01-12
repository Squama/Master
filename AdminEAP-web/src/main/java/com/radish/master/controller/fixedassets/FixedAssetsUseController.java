/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.fixedassets;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
import com.radish.master.entity.fixedassets.FixedAssetsStkTx;
import com.radish.master.entity.fixedassets.FixedAssetsUse;
import com.radish.master.service.CommonService;
import com.radish.master.system.SpringUtil;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年1月11日    Create this file
 * </pre>
 * 
 */
@Controller
@RequestMapping("/fixedassets/use")
public class FixedAssetsUseController {

	@Resource
	private CommonService commonService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String assetsList(HttpServletRequest request) {
		request.setAttribute("userName", SecurityUtil.getUser().getName());
		return "fixedassets/use/list";
	}

	@RequestMapping(value = "/audit/list", method = RequestMethod.GET)
	public String auditList(HttpServletRequest request) {
		return "fixedassets/use/audit_list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String assetsAdd(HttpServletRequest request) {
		String deptID = SecurityUtil.getUser().getDeptId();
		Org org = commonService.get(Org.class, deptID);
		if (org == null) {
			request.setAttribute("deptID", "nondept");
			request.setAttribute("deptName", "nondept");
		} else {
			request.setAttribute("deptID", deptID);
			request.setAttribute("deptName", org.getName());
		}

		request.setAttribute("stkOptions", JSONArray.toJSONString(commonService.getAssetsCombobox("30")));

		return "fixedassets/use/edit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String assetsEdit(String id, HttpServletRequest request) {
		request.setAttribute("id", id);

		request.setAttribute("stkOptions", JSONArray.toJSONString(commonService.getAssetsCombobox("30")));

		return "fixedassets/use/edit";
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") String id, HttpServletRequest request) {
		request.setAttribute("id", id);

		return "fixedassets/use/detail";
	}

	@RequestMapping(value = "/doaudit/{id}", method = RequestMethod.GET)
	public String doAudit(@PathVariable("id") String id, HttpServletRequest request) {
		request.setAttribute("id", id);

		request.setAttribute("stkOptions", JSONArray.toJSONString(commonService.getAssetsCombobox("30")));

		return "fixedassets/use/do_audit";
	}

	@RequestMapping(value = "/doreturn/{id}", method = RequestMethod.GET)
	public String doReturn(@PathVariable("id") String id, HttpServletRequest request) {
		request.setAttribute("id", id);

		request.setAttribute("stkOptions", JSONArray.toJSONString(commonService.getAssetsCombobox("30")));

		return "fixedassets/use/do_return";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save")
	@ResponseBody
	public Result save(FixedAssetsUse fau, HttpServletRequest request) {
		fau.setStatus("10");
		fau.setUpdateDateTime(new Date());
		fau.setCreateDateTime(new Date());
		fau.setOperator(SecurityUtil.getUser().getName());

		String deptID = SecurityUtil.getUser().getDeptId();
		Org org = commonService.get(Org.class, deptID);
		if (org == null) {
			fau.setUsedDeptID("nondept");
			fau.setUsedDeptName("nondept");
		} else {
			fau.setUsedDeptID(deptID);
			fau.setUsedDeptName(org.getName());
		}

		FixedAssetsStk stk = commonService.get(FixedAssetsStk.class, fau.getStkID());
		fau.setName(stk.getName());
		fau.setEnglishName(stk.getEnglishName());
		fau.setModel(stk.getModel());
		fau.setBelongedStock("总库");
		fau.setNorm(stk.getNorm());
		fau.setUnit(stk.getUnit());
		fau.setPrice(stk.getPrice());
		fau.setVendor(stk.getVendor());
		fau.setFaType(stk.getFaType());

		try {
			if (StrUtil.isEmpty(fau.getId())) {
				commonService.save(fau);
			} else {
				FixedAssetsUse oldFixedAssetsUse = commonService.get(FixedAssetsUse.class, fau.getId());

				SpringUtil.copyPropertiesIgnoreNull(fau, oldFixedAssetsUse);

				oldFixedAssetsUse.setUpdateDateTime(new Date());

				commonService.update(oldFixedAssetsUse);
			}

		} catch (Exception e) {
			return new Result(false, "保存失败，请联系系统管理员！");
		}
		return new Result(true, fau);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getuse")
	@ResponseBody
	public FixedAssetsUse getPur(String id) {
		FixedAssetsUse fixedAssetsUse = commonService.get(FixedAssetsUse.class, id);
		return fixedAssetsUse;
	}

	@RequestMapping(value = "/audit", method = RequestMethod.POST)
	@ResponseBody
	public Result singleEstimate(FixedAssetsUse useForm, HttpServletRequest request) throws Exception {

		FixedAssetsUse use = commonService.get(FixedAssetsUse.class, useForm.getId());

		FixedAssetsStk stk = commonService.get(FixedAssetsStk.class, use.getStkID());

		if (Double.valueOf(use.getQuantity()) > Double.valueOf(stk.getQuantityAvl())) {
			return new Result(false, "库存不足！");
		}

		BigDecimal oldQuantity = new BigDecimal(stk.getQuantity());
		BigDecimal oldQuantityAvl = new BigDecimal(stk.getQuantityAvl());
		BigDecimal increment = new BigDecimal(use.getQuantity());

		stk.setQuantity(oldQuantity.subtract(increment).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
		stk.setQuantityAvl(oldQuantityAvl.subtract(increment).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());

		FixedAssetsStkTx stkTx = new FixedAssetsStkTx();
		stkTx.setFixedAssetsID(stk.getId());
		stkTx.setOperation("20");// 领用
		stkTx.setAmount(use.getQuantity());
		stkTx.setBalance(stk.getQuantityAvl());
		stkTx.setPrice(stk.getPrice());
		stkTx.setRemark("领用审核通过");
		stkTx.setOperator(SecurityUtil.getUser().getLoginName());
		stkTx.setOperateTime(new Date());
		stkTx.setSourceTxID(use.getId());

		use.setStatus("20");
		if ("20".equals(useForm.getNeedReturn())) {
			use.setStatus("30");
			use.setQuantityReturned("0");
			use.setQuantityReturning("0");
			stkTx.setNeedReturn("20");
		}

		commonService.save(stk);
		commonService.save(stkTx);
		commonService.save(use);

		return new Result(true, "审核完成");
	}

	@RequestMapping(value = "/returnsubmit", method = RequestMethod.POST)
	@ResponseBody
	public Result returnSubmit(FixedAssetsUse useForm, HttpServletRequest request) throws Exception {

		FixedAssetsUse use = commonService.get(FixedAssetsUse.class, useForm.getId());

		use.setQuantityReturning(useForm.getQuantityReturning());
		use.setStatus("40");

		commonService.update(use);

		return new Result(true, "已提交");
	}

	@RequestMapping(value = "/auditreturn", method = RequestMethod.POST)
	@ResponseBody
	public Result auditReturn(String id, HttpServletRequest request) throws Exception {

		FixedAssetsUse use = commonService.get(FixedAssetsUse.class, id);

		FixedAssetsStk stk = commonService.get(FixedAssetsStk.class, use.getStkID());

		BigDecimal oldQuantity = new BigDecimal(stk.getQuantity());
		BigDecimal oldQuantityAvl = new BigDecimal(stk.getQuantityAvl());
		BigDecimal returnedQuantity = new BigDecimal(use.getQuantityReturned());
		BigDecimal increment = new BigDecimal(use.getQuantityReturning());

		stk.setQuantity(oldQuantity.add(increment).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
		stk.setQuantityAvl(oldQuantityAvl.add(increment).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
		use.setQuantityReturned(returnedQuantity.add(increment).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());

		FixedAssetsStkTx stkTx = new FixedAssetsStkTx();
		stkTx.setFixedAssetsID(stk.getId());
		stkTx.setOperation("30");// 领用
		stkTx.setAmount(use.getQuantityReturning());
		stkTx.setBalance(stk.getQuantityAvl());
		stkTx.setPrice(stk.getPrice());
		stkTx.setRemark("归还审核通过");
		stkTx.setOperator(SecurityUtil.getUser().getLoginName());
		stkTx.setOperateTime(new Date());
		stkTx.setSourceTxID(use.getId());

		use.setStatus("50");

		if (returnedQuantity.add(increment).compareTo(new BigDecimal(use.getQuantity())) == -1) {
			use.setStatus("35");
		}

		commonService.save(stk);
		commonService.save(stkTx);
		commonService.save(use);

		return new Result(true, "审核完成");
	}

}
