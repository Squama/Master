/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.CodeException;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.BudgetTx;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Project;
import com.radish.master.entity.project.BudgetLabour;
import com.radish.master.entity.project.BudgetMech;
import com.radish.master.pojo.RowEdit;
import com.radish.master.service.BudgetService;
import com.radish.master.system.GUID;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月6日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/budget/estimate")
public class BudgetEstimateController {

    @Resource
    private BudgetService budgetService;
    @Resource
	private BaseService baseService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "budgetmanage/budgetestimate/budget_estimate_list";
    }
    
    @RequestMapping(value="/toestimate",method = RequestMethod.POST)
    public String toEstimate(String budgetNo, String sourceUrl, HttpServletRequest request){
        //Budget budget = budgetService.getBudgetByNo(budgetNo);
        
        request.setAttribute("budgetNo", budgetNo);
        request.setAttribute("sourceUrl", sourceUrl);
        request.setAttribute("materiels", JSONArray.toJSONString(budgetService.getMatMap()));
        
        return "budgetmanage/budgetestimate/budget_estimate";
    }
    
    @RequestMapping(value="/doestimate",method = RequestMethod.POST)
    @ResponseBody
    public Result doEstimate(String budgetNo, String[] choose, HttpServletRequest request) throws CodeException{
        List<BudgetImport> importList = budgetService.getBudgetImportList(choose);
        
        List<BudgetTx> txList = new ArrayList<BudgetTx>();
        
        BudgetTx btGroup = null;
        
        for(BudgetImport bi : importList){
            BudgetTx bt = new BudgetTx();
            BeanUtils.copyProperties(bi,bt);
            
            bt.setId(null);
            bt.setRegionCode(bi.getQuotaNo());
            bt.setRegionName(bi.getQuotaName());
            bt.setCreateDateTime(new Date());
            
            bi.setOrderNo(GUID.genTxNo(16));
            bi.setCol(null);
            
            bt.setOrderNo(bi.getOrderNo());
            bt.setCol(bi.getCol());
            
            txList.add(bt);
            
            btGroup = budgetService.getTxGroupByNo(bt.getQuotaGroup());
            
            if(btGroup == null || !btGroup.getQuotaGroup().equalsIgnoreCase(bt.getQuotaGroup())){
                btGroup = new BudgetTx();
                BudgetImport biGroup = budgetService.getGroupByNo(bi.getQuotaGroup());
                BeanUtils.copyProperties(biGroup,btGroup);
                btGroup.setId(null);
                btGroup.setRegionCode(biGroup.getQuotaNo());
                btGroup.setRegionName(biGroup.getQuotaName());
                btGroup.setIsGroup("1");
                
                txList.add(btGroup);
            }

        }
        
        budgetService.batchUpdate(importList);
        budgetService.batchSave(txList);
        
        return new Result(true);
    }
    
    @RequestMapping(value="/tomerge",method = RequestMethod.GET)
    public String toMerge(String budgetNo, String[] choose, HttpServletRequest request){
        List<BudgetImport> importList = budgetService.getBudgetImportList(choose);
        
        BigDecimal quantities = new BigDecimal("0");
        BigDecimal unitPrice = new BigDecimal("0");
        BigDecimal artificiality = new BigDecimal("0");
        BigDecimal materiels = new BigDecimal("0");
        BigDecimal mech = new BigDecimal("0");
        
        for(BudgetImport bi : importList){
            quantities = quantities.add(new BigDecimal(StrUtil.isEmpty(bi.getQuantities())?"0":bi.getQuantities()));
            unitPrice = unitPrice.add(new BigDecimal(StrUtil.isEmpty(bi.getUnitPrice())?"0":bi.getUnitPrice()));
            artificiality = artificiality.add(new BigDecimal(StrUtil.isEmpty(bi.getArtificiality())?"0":bi.getArtificiality()));
            materiels = materiels.add(new BigDecimal(StrUtil.isEmpty(bi.getMateriels())?"0":bi.getMateriels()));
            mech = mech.add(new BigDecimal(StrUtil.isEmpty(bi.getMech())?"0":bi.getMech()));

        }
        
        request.setAttribute("budgetNo", budgetNo);
        request.setAttribute("quantities", quantities.toPlainString());
        request.setAttribute("unitPrice", unitPrice.toPlainString());
        request.setAttribute("artificiality", artificiality.toPlainString());
        request.setAttribute("materiels", materiels.toPlainString());
        request.setAttribute("mech", mech.toPlainString());
        
        return "budgetmanage/budgetestimate/merge_estimate";
    }
    
    @RequestMapping(value="/mergeestimate",method = RequestMethod.POST)
    @ResponseBody
    public Result mergeEstimate(BudgetTx budgetTx, String[] choose, HttpServletRequest request) throws CodeException{
        List<BudgetImport> importList = budgetService.getBudgetImportList(choose);
        
        List<BudgetTx> txList = new ArrayList<BudgetTx>();
        
        BudgetTx btGroup = null;
        String orderNo = GUID.genTxNo(16);
        for(BudgetImport bi : importList){
            bi.setOrderNo(orderNo);
            bi.setCol(null);
            
            budgetTx.setProjectID(bi.getProjectID());
            
            if(btGroup == null){
            	btGroup = budgetService.getTxGroupByNo(bi.getQuotaGroup());
            }
            
            if(btGroup == null){
                btGroup = new BudgetTx();
                BudgetImport biGroup = budgetService.getGroupByNo(bi.getQuotaGroup());
                BeanUtils.copyProperties(biGroup,btGroup);
                btGroup.setId(null);
                btGroup.setRegionCode(biGroup.getQuotaNo());
                btGroup.setRegionName(biGroup.getQuotaName());
                btGroup.setIsGroup("1");
                
                txList.add(btGroup);
            }

        }
        
        budgetTx.setQuotaGroup(btGroup.getQuotaGroup());
        budgetTx.setOrderNo(orderNo);
        budgetTx.setCol(String.valueOf(importList.size()));
        budgetTx.setCreateDateTime(new Date());
        budgetTx.setIsGroup("0");
        
        txList.add(budgetTx);
        
        budgetService.batchUpdate(importList);
        budgetService.batchSave(txList);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/getgroup/{group}")
    @ResponseBody
    private Result getGroup(@PathVariable("group") String group, HttpServletRequest request) {
        BudgetTx bt = budgetService.getTxGroupByNo(group);
        return new Result(true, bt);
    }
    
    @RequestMapping(value="/rowedit",method = RequestMethod.POST)
    @ResponseBody
    public String singleEstimate(String action, HttpServletRequest request){
        request.setAttribute("id", action);
        Enumeration<String> key = request.getParameterNames();  
        while(key.hasMoreElements()){  
            System.out.println(key.nextElement());  
        } 
        RowEdit re = new RowEdit();
        List<Object> list = new ArrayList<Object>();
        list.add(budgetService.get(BudgetTx.class,"40280cac604dc2d501604dc4ef9c0003"));
        re.setData(list);
        System.out.println(JSONArray.toJSONString(re));
        return JSONArray.toJSONString(re);
    }
    
    @RequestMapping(value="/singleestimate",method = RequestMethod.GET)
    public String singleEstimate(HttpServletRequest request, String id){
        request.setAttribute("budgetTxID", id);
        
        return "budgetmanage/budgetestimate/single_estimate";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getbytxid")
    @ResponseBody
    private Result getBudgetByNo(String budgetTxID) {
        BudgetTx tx = budgetService.get(BudgetTx.class,budgetTxID);
        
        Project project = budgetService.get(Project.class, tx.getProjectID());
        
        Budget budget = budgetService.getBudgetByNo(tx.getBudgetNo());
        
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("budgetTx", tx);
        map.put("projectName", project.getProjectName());
        map.put("budgetName", budget.getBudgetName());
        
        return new Result(true, map);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result saveBudgetEstimate(BudgetEstimate budgetEstimate, HttpServletRequest request) {
        
        List<BudgetEstimate> list = budgetService.getBudgetEstimateCount(budgetEstimate.getBudgetTxID());
        if(list.size() >= 20){
            return new Result(false, "材料测算保存失败！请检查是否超过20条！");
        }
        
        budgetService.save(budgetEstimate);
        //汇总合价
        list = budgetService.getBudgetEstimateCount(budgetEstimate.getBudgetTxID());
        BigDecimal sum = new BigDecimal("0");
        for(BudgetEstimate be : list){
            BigDecimal quantity = new BigDecimal(be.getQuantity());
            BigDecimal price = new BigDecimal(be.getBudgetPrice());
            
            sum = sum.add(quantity.multiply(price));
        }
        
        BudgetTx bt = budgetService.get(BudgetTx.class, budgetEstimate.getBudgetTxID());
        
        bt.setMateriels(sum.toPlainString());
        
        budgetService.update(bt);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/savelabour")
    @ResponseBody
    private Result saveBudgetLabour(BudgetLabour budgetLabour, HttpServletRequest request) {
        budgetService.save(budgetLabour);
        
        List<BudgetLabour> list = budgetService.getBudgetLabourCount(budgetLabour.getBudgetTxID());
        //汇总合价
        BigDecimal sum = new BigDecimal("0");
        for(BudgetLabour be : list){
            BigDecimal quantity = new BigDecimal(be.getLabourQuantity());
            BigDecimal price = new BigDecimal(be.getForecastPrice());
            
            sum = sum.add(quantity.multiply(price));
        }
        
        BudgetTx bt = budgetService.get(BudgetTx.class, budgetLabour.getBudgetTxID());
        
        bt.setArtificiality(sum.toPlainString());
        
        budgetService.update(bt);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/savemech")
    @ResponseBody
    private Result saveBudgetMech(BudgetMech budgetMech, HttpServletRequest request) {
        budgetService.save(budgetMech);
        
        List<BudgetMech> list = budgetService.getBudgetMechCount(budgetMech.getBudgetTxID());
        //汇总合价
        BigDecimal sum = new BigDecimal("0");
        for(BudgetMech be : list){
            BigDecimal quantity = new BigDecimal(be.getMechQuantity());
            BigDecimal price = new BigDecimal(be.getMechPrice());
            
            sum = sum.add(quantity.multiply(price));
        }
        
        BudgetTx bt = budgetService.get(BudgetTx.class, budgetMech.getBudgetTxID());
        
        bt.setMech(sum.toPlainString());
        
        budgetService.update(bt);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/savepackage")
    @ResponseBody
    private Result saveBudgetPack(String budgetNo, String acreage, String univalent, HttpServletRequest request) {
        BudgetTx tx = budgetService.checkPack(budgetNo);
        if(tx != null){
            return new Result(false);
        }
        
        BigDecimal price;
        BigDecimal mianji = new BigDecimal(acreage);
        BigDecimal danjia = new BigDecimal(univalent);
        
        price = mianji.multiply(danjia);
        
        Budget budget = budgetService.getBudgetByNo(budgetNo);
        BudgetTx pack = new BudgetTx();
        pack.setBudgetNo(budget.getBudgetNo());
        pack.setProjectID(budget.getProjectID());
        pack.setRegionCode("包工包料");
        pack.setRegionName("包工包料");
        pack.setQuantities(acreage);
        pack.setUnitPrice(price.toPlainString());
        pack.setQuotaGroup("package");
        pack.setOrderNo("pack");
        
        BudgetTx packGroup = new BudgetTx();
        packGroup.setBudgetNo(budget.getBudgetNo());
        packGroup.setProjectID(budget.getProjectID());
        packGroup.setRegionName("包工包料");
        packGroup.setQuotaGroup("package");
        packGroup.setIsGroup("1");
        packGroup.setQuantities("");
        packGroup.setUnitPrice(price.toPlainString());
        packGroup.setArtificiality("");
        packGroup.setMateriels("");
        packGroup.setMech("");
        
        budgetService.save(pack);
        budgetService.save(packGroup);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    @ResponseBody
    private Result deleteBudgetEstimate(String id, HttpServletRequest request) {
        BudgetEstimate budgetEstimate = budgetService.get(BudgetEstimate.class, id);
        budgetService.delete(budgetEstimate);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/deletelabour")
    @ResponseBody
    private Result deleteBudgetLabour(String id, HttpServletRequest request) {
        BudgetLabour budgetLabour = budgetService.get(BudgetLabour.class, id);
        budgetService.delete(budgetLabour);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/deletemech")
    @ResponseBody
    private Result deleteBudgetMech(String id, HttpServletRequest request) {
        BudgetMech budgetMech = budgetService.get(BudgetMech.class, id);
        budgetService.delete(budgetMech);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/deletetx")
    @ResponseBody
    private Result deleteBudgetTx(String id, HttpServletRequest request) {
        BudgetTx tx = budgetService.get(BudgetTx.class, id);
        //获取import 的 list
        List<BudgetImport> importList = budgetService.getBudgetImportListByOrderNo(tx.getOrderNo());
        //list循环更新
        for(BudgetImport bi : importList){
            bi.setOrderNo(null);
        }
        
        budgetService.batchUpdate(importList);
        budgetService.delete(tx);
        //tx删除分组
        List<BudgetTx> list = budgetService.getBudgetTxListByGroup(tx.getBudgetNo(), tx.getQuotaGroup());
        if(list.size() == 1 && "1".equals(list.get(0).getIsGroup())){
            budgetService.delete(list.get(0));
        }
        
        return new Result(true);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value="/auditlist/{budgetNo}")
    private String toAudit(@PathVariable("budgetNo") String budgetNo, HttpServletRequest request) {
        request.setAttribute("budgetNo", budgetNo);
        return "budgetmanage/budgetactiviti/audit_list";
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value="/backtoedit/{budgetNo}")
    private String toEditAgain(@PathVariable("budgetNo") String budgetNo, HttpServletRequest request) {
        request.setAttribute("budgetNo", budgetNo);
        request.setAttribute("materiels", JSONArray.toJSONString(budgetService.getMatMap()));
        
        return "budgetmanage/budgetestimate/budget_estimate";
    }
    
    @VerifyCSRFToken
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String budgetNo) {
        return budgetService.startEstimateFlow(budgetService.getBudgetByNo(budgetNo),"estimateApprove");
    }
    
    @RequestMapping(value="/comparedetail",method = RequestMethod.GET)
    public String compareDetail(){
        return "budgetmanage/budgetestimate/budget_estimate_list";
    }
    
    @RequestMapping(value="/addMatIndex",method = RequestMethod.GET)
    public String addMatIndex(HttpServletRequest request){
    	String  budgetTxID = request.getParameter("budgetTxID");
    	String  projectID = request.getParameter("projectID");
    	String  matBudgetNo = request.getParameter("matBudgetNo");
    	request.setAttribute("budgetTxID", budgetTxID);
    	request.setAttribute("projectID", projectID);
    	request.setAttribute("matBudgetNo", matBudgetNo);
        return "budgetmanage/budgetestimate/addMatIndex";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/saveMat")
    @ResponseBody
    private Result saveMat(BudgetEstimate budgetEstimate, HttpServletRequest request,Materiel materiel) {
    	//先存物料
    	String ss1 = request.getParameter("ss1");
    	String ss2 = request.getParameter("ss2");
    	String ss3 = request.getParameter("ss3");
    	String ss4 = request.getParameter("ss4");
    	if(ss1!=null){
    		materiel.setParent_ID(ss1);
    	}
    	if(ss2!=null){
    		materiel.setParent_ID(ss2);
    	}
    	if(ss3!=null){
    		materiel.setParent_ID(ss3);
    	}
    	if(ss4!=null){
    		materiel.setParent_ID(ss4);
    	}
    	materiel.setCreate_time(new Date());
    	String mat_id = materiel.getParent_ID();
    	Mat m = baseService.get(Mat.class, mat_id);
    	String code = m.getCode();
    	String[] strs = code.split("_");
    	String str = strs[1];
    	//拿到当前数据库数据的最大值
    	List<String> num = baseService.find("select max(mat.reserve1) from com.radish.master.entity.Materiel mat");
    	if(num.isEmpty()||num.get(0)==null){
    		materiel.setMat_number(str+"100105");
    		materiel.setReserve1("100105");
    	}else{
    		String s= num.get(0);
    		int i;
    		if(StrUtil.isEmpty(s)){
    			i = 100105;
    		}else{
    			i = Integer.parseInt(s);
    			i++;
    		}
    		materiel.setMat_number(str+i);
    		materiel.setReserve1(i+"");
    	}
    	User u = SecurityUtil.getUser();
    	materiel.setCreate_name(u.getName());
    	baseService.save(materiel);
    	
    	//保存物料测算明细
        List<BudgetEstimate> list = budgetService.getBudgetEstimateCount(budgetEstimate.getBudgetTxID());
        if(list.size() >= 20){
            return new Result(false, "材料测算保存失败！请检查是否超过20条！");
        }
        
        budgetEstimate.setMatNumber(materiel.getMat_number());
        budgetEstimate.setMatName(materiel.getMat_name());
        budgetEstimate.setMatStandard(materiel.getMat_standard());
        budgetEstimate.setUnit(materiel.getUnit());
        budgetService.save(budgetEstimate);
        
        //汇总合价
        list = budgetService.getBudgetEstimateCount(budgetEstimate.getBudgetTxID());
        BigDecimal sum = new BigDecimal("0");
        for(BudgetEstimate be : list){
            BigDecimal quantity = new BigDecimal(be.getQuantity());
            BigDecimal price = new BigDecimal(be.getBudgetPrice());
            
            sum = sum.add(quantity.multiply(price));
        }
        
        BudgetTx bt = budgetService.get(BudgetTx.class, budgetEstimate.getBudgetTxID());
        
        bt.setMateriels(sum.toPlainString());
        
        budgetService.update(bt);
        
        return new Result(true);
    }
}
