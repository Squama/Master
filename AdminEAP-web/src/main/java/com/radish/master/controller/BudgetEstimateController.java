/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";
    
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
            
            btGroup = budgetService.getTxGroupByGroupAndNo(bi.getQuotaGroup(), bi.getBudgetNo());
            
            if(btGroup == null || !btGroup.getQuotaGroup().equalsIgnoreCase(bt.getQuotaGroup())){
                btGroup = new BudgetTx();
                BudgetImport biGroup = budgetService.getGroupByNo(bi.getId());
                BeanUtils.copyProperties(biGroup,btGroup);
                btGroup.setId(null);
                btGroup.setRegionCode(biGroup.getQuotaNo());
                btGroup.setRegionName(biGroup.getQuotaName());
                btGroup.setIsGroup("1");
                
                budgetService.save(btGroup);
            }

        }
        
        budgetService.batchUpdate(importList);
        budgetService.batchSave(txList);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/doimport")
    @ResponseBody
    public Result importEstimate(@RequestParam(value = "file", required = false) MultipartFile file, String budgetTxID, String projectID, String budgetNo) throws IOException{
        
        Workbook workbook = null;
        try{
            String fileName = file.getOriginalFilename();
            
            if(fileName.endsWith(XLS)){
                workbook = new HSSFWorkbook(file.getInputStream());
            }else if(fileName.endsWith(XLSX)){
                workbook = new XSSFWorkbook(file.getInputStream());
            }else{
                throw new CodeException("1101","文件不是excel格式");
            }
            
            Sheet sheet = workbook.getSheetAt(0);
            
            int rows = sheet.getLastRowNum();
            
            if(rows == 0){
                throw new CodeException("1101","表格中无数据");
            }  
            
            List<BudgetEstimate> matList = new ArrayList<BudgetEstimate>();
            List<BudgetLabour> labourList = new ArrayList<BudgetLabour>();
            List<BudgetMech> mechList = new ArrayList<BudgetMech>();
            
            int startLine = 0;
            //人工
            for(int i = 1; i<=rows; i++){
                Row row = sheet.getRow(i);
                
                if(row != null){
                    if( i == 1 && !"一".equals(getCellValue(row.getCell(0)))){
                        throw new CodeException("1101","表格中人工费第一行数据格式不正确");
                    }
                    
                    if( i == 1 && "一".equals(getCellValue(row.getCell(0)))){
                        continue;
                    }
                    
                    if("小计".equals(getCellValue(row.getCell(1)))){
                        startLine = i + 1;
                        break;
                    }
                    
                    BudgetLabour bi = new BudgetLabour();
                    
                    bi.setBudgetTxID(budgetTxID);
                    bi.setBudgetNo(budgetNo);
                    bi.setProjectID(projectID);
                    
                    String labourName = getCellValue(row.getCell(1));
                    bi.setLabourName(labourName);
                    
                    String labourQuantity = getCellValue(row.getCell(4));
                    bi.setLabourQuantity(labourQuantity);
                    
                    String forecastPrice = getCellValue(row.getCell(5));
                    bi.setForecastPrice(forecastPrice);
                    
                    
                    labourList.add(bi);
                }
            }
            
            //材料
            
            Map<String, String> matDistinctMap = new HashMap<String, String>();
            
            for(int i = startLine; i<=rows; i++){
                Row row = sheet.getRow(i);
                
                if(row != null){
                    if( i == startLine && !"二".equals(getCellValue(row.getCell(0)))){
                        throw new CodeException("1101","表格中材料费第一行数据格式不正确");
                    }
                    
                    if( i == startLine && "二".equals(getCellValue(row.getCell(0)))){
                        continue;
                    }
                    
                    if("小计".equals(getCellValue(row.getCell(1)))){
                        if("配比材料".equals(getCellValue(sheet.getRow(i + 1).getCell(1)))){
                            i = i + 1;
                            continue;
                        }else if("机械".equals(getCellValue(sheet.getRow(i + 1).getCell(1)))){
                            startLine = i + 1;
                            break;
                        }
                    }
                    
                    BudgetEstimate bi = new BudgetEstimate();
                    
                    bi.setBudgetTxID(budgetTxID);
                    bi.setBudgetNo(budgetNo);
                    bi.setProjectID(projectID);
                    
                    String name = getCellValue(row.getCell(1));
                    String standard = getCellValue(row.getCell(2));
                    String unit = getCellValue(row.getCell(3));
                    Materiel mat = budgetService.getEstimateImportMat(name, standard, unit);
                    
                    bi.setMatName(mat.getMat_name());
                    bi.setMatNumber(mat.getMat_number());
                    bi.setMatStandard(mat.getMat_standard());
                    bi.setUnit(mat.getUnit());
                    
                    String quantity = getCellValue(row.getCell(4));
                    bi.setQuantity(quantity);
                    
                    String budgetPrice = getCellValue(row.getCell(5));
                    bi.setBudgetPrice(budgetPrice);
                    
                    if(matDistinctMap.containsKey(bi.getMatNumber())){
                        throw new CodeException("1101","表格中材料费有重复物料，请检查【物料名称："+bi.getMatName()+"，物料规格："+bi.getMatStandard()+"】");
                    }else{
                        matDistinctMap.put(bi.getMatNumber(), bi.getMatName());
                    }
                    
                    matList.add(bi);
                }
            }
            
            //机械
            for(int i = startLine; i<=rows; i++){
                Row row = sheet.getRow(i);
                
                if(row != null){
                    if( i == startLine && !"四".equals(getCellValue(row.getCell(0)))){
                        throw new CodeException("1101","表格中机械费第一行数据格式不正确");
                    }
                    
                    if( i == startLine && "四".equals(getCellValue(row.getCell(0)))){
                        continue;
                    }
                    
                    if("小计".equals(getCellValue(row.getCell(1)))){
                        startLine = i + 1;
                        break;
                    }
                    
                    BudgetMech bi = new BudgetMech();
                    
                    bi.setBudgetTxID(budgetTxID);
                    bi.setBudgetNo(budgetNo);
                    bi.setProjectID(projectID);
                    
                    String mechName = getCellValue(row.getCell(1));
                    bi.setMechName(mechName);
                    
                    String mechQuantity = getCellValue(row.getCell(4));
                    bi.setMechQuantity(mechQuantity);
                    
                    String mechPrice = getCellValue(row.getCell(5));
                    bi.setMechPrice(mechPrice);
                    
                    
                    mechList.add(bi);
                }
            }
            
            baseService.batchSave(labourList);
            baseService.batchSave(matList);
            baseService.batchSave(mechList);
            
            //汇总合价
            List<BudgetEstimate> list = budgetService.getBudgetEstimateCount(budgetTxID);
            BigDecimal sumMat = new BigDecimal("0");
            for(BudgetEstimate be : list){
                BigDecimal quantity = new BigDecimal(be.getQuantity());
                BigDecimal price = new BigDecimal(be.getBudgetPrice());
                
                sumMat = sumMat.add(quantity.multiply(price));
            }
            
            List<BudgetLabour> listLabour = budgetService.getBudgetLabourCount(budgetTxID);
            BigDecimal sumLabour = new BigDecimal("0");
            for(BudgetLabour be : listLabour){
                BigDecimal quantity = new BigDecimal(be.getLabourQuantity());
                BigDecimal price = new BigDecimal(be.getForecastPrice());
                
                sumLabour = sumLabour.add(quantity.multiply(price));
            }
            
            List<BudgetMech> listMech = budgetService.getBudgetMechCount(budgetTxID);
            BigDecimal sumMech = new BigDecimal("0");
            for(BudgetMech be : listMech){
                BigDecimal quantity = new BigDecimal(be.getMechQuantity());
                BigDecimal price = new BigDecimal(be.getMechPrice());
                
                sumMech = sumMech.add(quantity.multiply(price));
            }
            
            BudgetTx bt = budgetService.get(BudgetTx.class, budgetTxID);
            
            bt.setMateriels(sumMat.toPlainString());
            bt.setArtificiality(sumLabour.toPlainString());
            bt.setMech(sumMech.toPlainString());
            BigDecimal sumall = new BigDecimal("0");
            sumall=sumall.add(sumMat).add(sumLabour).add(sumMech);
            bt.setUnitPrice(sumall.toPlainString());
            budgetService.update(bt);
            
        }catch (CodeException ce) {
            return new Result(false,ce.getMessage());
        }catch (Exception e) {
            return new Result(false,"导入失败");
        }finally{
            if(workbook != null){
                workbook.close();
            }
        }
        
        return new Result(true,file.getOriginalFilename());
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
            	btGroup = budgetService.getTxGroupByGroupAndNo(bi.getQuotaGroup(), bi.getBudgetNo());
            }
            
            if(btGroup == null){
                btGroup = new BudgetTx();
                BudgetImport biGroup = budgetService.getGroupByNo(bi.getId());
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
    
    @RequestMapping(value="/singleestimatequery",method = RequestMethod.GET)
    public String singleEstimateQuery(HttpServletRequest request, String id){
        request.setAttribute("budgetTxID", id);
        
        return "budgetmanage/budgetestimate/single_estimate_query";
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
        bt.setUnitPrice(getUnitPrice(bt));
        
        budgetService.update(bt);
        
        updateGroupInfo(bt.getQuotaGroup(), bt.getBudgetNo());
        
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
        bt.setUnitPrice(getUnitPrice(bt));
        
        budgetService.update(bt);
        
        updateGroupInfo(bt.getQuotaGroup(), bt.getBudgetNo());
        
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
        bt.setUnitPrice(getUnitPrice(bt));
        
        budgetService.update(bt);
        
        updateGroupInfo(bt.getQuotaGroup(), bt.getBudgetNo());
        
        return new Result(true);
    }
    
    private String getUnitPrice(BudgetTx bt){
        BigDecimal result = new BigDecimal("0");
        BigDecimal labour = new BigDecimal(StrUtil.isEmpty(bt.getArtificiality())?"0":bt.getArtificiality() );
        BigDecimal mat = new BigDecimal(StrUtil.isEmpty(bt.getMateriels())?"0":bt.getMateriels());
        BigDecimal mech = new BigDecimal(StrUtil.isEmpty(bt.getMech())?"0":bt.getMech());
        
        return result.add(labour.add(mat.add(mech))).toPlainString();
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
        List<BudgetEstimate> list = budgetService.getBudgetEstimateCount(budgetEstimate.getBudgetTxID());
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
        bt.setUnitPrice(getUnitPrice(bt));
        
        budgetService.update(bt);
        
        updateGroupInfo(bt.getQuotaGroup(), bt.getBudgetNo());
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/mark")
    @ResponseBody
    private Result mark(String id, HttpServletRequest request) {
        BudgetImport bi = budgetService.get(BudgetImport.class, id);
        bi.setIsPack("1");
        budgetService.update(bi);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/remark")
    @ResponseBody
    private Result reMark(String id, HttpServletRequest request) {
        BudgetImport bi = budgetService.get(BudgetImport.class, id);
        bi.setIsPack("0");
        budgetService.update(bi);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/deletelabour")
    @ResponseBody
    private Result deleteBudgetLabour(String id, HttpServletRequest request) {
        BudgetLabour budgetLabour = budgetService.get(BudgetLabour.class, id);
        budgetService.delete(budgetLabour);
        
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
        bt.setUnitPrice(getUnitPrice(bt));
        
        budgetService.update(bt);
        
        updateGroupInfo(bt.getQuotaGroup(), bt.getBudgetNo());
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/deletemech")
    @ResponseBody
    private Result deleteBudgetMech(String id, HttpServletRequest request) {
        BudgetMech budgetMech = budgetService.get(BudgetMech.class, id);
        budgetService.delete(budgetMech);
        
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
        bt.setUnitPrice(getUnitPrice(bt));
        
        budgetService.update(bt);
        
        updateGroupInfo(bt.getQuotaGroup(), bt.getBudgetNo());
        
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
        
        List<BudgetImport> list = budgetService.getBudgetImportList(budgetNo);
        
        BigDecimal sumUnit = new BigDecimal("0");
        BigDecimal sumLabour = new BigDecimal("0");
        BigDecimal sumMat = new BigDecimal("0");
        BigDecimal sumMech = new BigDecimal("0");
        
        for(BudgetImport bi : list){
        	if(!"1".equals(bi.getIsGroup())){
        		sumUnit = sumUnit.add(new BigDecimal(StrUtil.isEmpty(bi.getUnitPrice())?"0":bi.getUnitPrice()));
        		sumLabour = sumLabour.add(new BigDecimal(StrUtil.isEmpty(bi.getArtificiality())?"0":bi.getArtificiality()));
        		sumMat = sumMat.add(new BigDecimal(StrUtil.isEmpty(bi.getMateriels())?"0":bi.getMateriels()));
        		sumMech = sumMech.add(new BigDecimal(StrUtil.isEmpty(bi.getMech())?"0":bi.getMech()));
        	}
        }
        
        request.setAttribute("sumUnit", sumUnit.toPlainString());
        request.setAttribute("sumLabour", sumLabour.toPlainString());
        request.setAttribute("sumMat", sumMat.toPlainString());
        request.setAttribute("sumMech", sumMech.toPlainString());
        
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
    
    private String getCellValue(Cell cell){
        String value = "";
        
        if(cell != null){
            switch(cell.getCellType()){
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if(HSSFDateUtil.isCellDateFormatted(cell)){
                        Date date = cell.getDateCellValue();
                        if(date != null){
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        }else{
                            value = "";
                        }
                    }else{
                        value = new DecimalFormat("##.##").format(cell.getNumericCellValue());
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue() + "";
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    value = cell.getCellFormula() + "";
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    value = "非法字符";
                    break;
                default:
                    value = "未知类型";
                    break;
            }
        }
        return value.trim();
    }
    
    private void updateGroupInfo(String quotaGroup, String budgetNo){
        List<BudgetTx> list = budgetService.getBudgetTxListByGroup(budgetNo, quotaGroup);
        
        //合价
        BigDecimal unitPriceSum = new BigDecimal("0");
        //人工
        BigDecimal artificialitySum = new BigDecimal("0");
        //机械
        BigDecimal mechSum = new BigDecimal("0");
        //材料
        BigDecimal materielsSum = new BigDecimal("0");
        
        BudgetTx groupBudgetTx = null;
        
        for(BudgetTx tx : list){
            if("1".equals(tx.getIsGroup())){
                groupBudgetTx = tx;
                continue;
            }
            unitPriceSum = unitPriceSum.add(new BigDecimal(StrUtil.isEmpty(tx.getUnitPrice())?"0":tx.getUnitPrice()));
            artificialitySum = artificialitySum.add(new BigDecimal(StrUtil.isEmpty(tx.getArtificiality())?"0":tx.getArtificiality()));
            mechSum = mechSum.add(new BigDecimal(StrUtil.isEmpty(tx.getMech())?"0":tx.getMech()));
            materielsSum = materielsSum.add(new BigDecimal(StrUtil.isEmpty(tx.getMateriels())?"0":tx.getMateriels()));
        }
        
        if(groupBudgetTx != null){
            groupBudgetTx.setUnitPrice(unitPriceSum.toPlainString());
            groupBudgetTx.setArtificiality(artificialitySum.toPlainString());
            groupBudgetTx.setMech(mechSum.toPlainString());
            groupBudgetTx.setMateriels(materielsSum.toPlainString());
            
            budgetService.update(groupBudgetTx);
        }
        
    }
    
}
