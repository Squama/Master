package com.radish.master.controller.doCount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.core.util.ArrayUtils;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.doCount.CostAll;
import com.radish.master.entity.doCount.CostAllST;
import com.radish.master.entity.doCount.InAndOutDet;
import com.radish.master.pojo.BudgetCount;
import com.radish.master.pojo.BudgetCountDetail;
import com.radish.master.pojo.MatInAndOut;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/budgetCount")
public class BudgetCountlController {
	private String prefix="/doCount/budgetCount/";
	
	private Arith ari;
	
	@Resource
    private BudgetService budgetService;
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix +"budgetCount";
	}
	@RequestMapping("/detail")
	public String detail(HttpServletRequest request){
		request.setAttribute("budgetNo",request.getParameter("budgetNo"));
		return prefix +"detail";
	}
	
	@RequestMapping("/getList")
	@ResponseBody
	public List<BudgetCount> getList(String projectId,String projectSubId){
		String sql = "select *  from v_budgetCount where project_id='"+projectId+"'"
				+ " and project_sub_id='"+projectSubId+"'";
		List<BudgetCount> mxs = baseService.findMapBySql(sql, new Object[]{}, new Type[]{}, BudgetCount.class);
		return mxs;
	}
	
	@RequestMapping("/getDetailList")
	@ResponseBody
	public List<BudgetCountDetail> getDetailList(String budgetNo){
		String sql = "select *  from v_budgetCountDetail where budget_no='"+budgetNo+"' "
				+ " order by quota_group asc, order_no desc";
		List<BudgetCountDetail> mxs = baseService.findMapBySql(sql, new Object[]{}, new Type[]{}, BudgetCountDetail.class);
		return mxs;
	}
}
