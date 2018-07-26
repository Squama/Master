package com.radish.master.service.impl;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.query.entity.QueryCondition;
import com.cnpc.framework.utils.CodeException;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.BudgetTx;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Project;
import com.radish.master.entity.PurchaseApplyAudit;
import com.radish.master.entity.project.BudgetLabour;
import com.radish.master.entity.project.BudgetMech;
import com.radish.master.pojo.Options;
import com.radish.master.service.BudgetService;
import com.radish.master.system.GUID;

@Service("budgetService")
public class BudgetServiceImpl extends BaseServiceImpl implements BudgetService {
    
    @Resource
    private RuntimePageService runtimePageService;
    
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    @Override
    public Budget getBudgetByNo(String budgetNo){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.get("from Budget where budgetNo=:budgetNo", params);
    }
    
    @Override
    public Budget getBudgetByProjectAndName(String projectID, String budgetName){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectID", projectID);
        params.put("budgetName", budgetName);
        return this.get("from Budget where projectID=:projectID AND budgetName=:budgetName", params);
    }

    @Override
    public Boolean checkTxUnique(String projectID, String regionID, String matNumber) {
        String hql = "from BudgetTx where projectID=:projectID AND regionID=:regionID AND matNumber=:matNumber";
        Map<String, Object> params = new HashMap<>();
        params.put("projectID", projectID);
        params.put("regionID", regionID);
        params.put("matNumber", matNumber);
        
        return this.get(hql, params) == null;
    }

    @Override
    public Result startFlow(Budget budget, String processDefinitionKey) {
        budget.setStatus("20");
        budget.setUpdateDateTime(new Date());
        
        this.update(budget);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "申请预算：" + budget.getBudgetName() + "，所属项目：" + budget.getProjectID();
        
        //businessKey
        String businessKey = budget.getBudgetNo();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, budget.getBudgetNo());
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables,
                user.getId(), businessKey);
    }

    @Override
    public Integer importExcel(MultipartFile file, Budget budget) throws Exception {
        Workbook workbook = null;
        try{
            String fileName = file.getOriginalFilename();
            
            if(fileName.endsWith(XLS)){
                workbook = new HSSFWorkbook(file.getInputStream());
            }else if(fileName.endsWith(XLSX)){
                workbook = new XSSFWorkbook(file.getInputStream());
            }else{
                throw new CodeException("文件不是excel格式");
            }
            
            Sheet sheet = workbook.getSheet("Sheet1");
            
            int rows = sheet.getLastRowNum();
            
            if(rows == 0){
                throw new CodeException("请填写数据");
            }  
            
            List<BudgetImport> list = new ArrayList<BudgetImport>();
            
            String group = "";
            
            for(int i = 4; i<rows; i++){
                Row row = sheet.getRow(i);
                if(row != null){
                    BudgetImport bi = new BudgetImport();
                    
                    String quotaNo = getCellValue(row.getCell(0));
                    
                    
                    if(StrUtil.isEmpty(quotaNo)){
                        bi.setId(GUID.genTxNo(16, true));
                        if(group != bi.getId()){
                            group = bi.getId();
                        }
                        bi.setIsGroup("1");
                    } else if(quotaNo.matches("\\d+$")){
                        if(group != quotaNo){
                            group = quotaNo;
                        }
                        bi.setQuotaNo(quotaNo);
                        bi.setIsGroup("1");
                    } else{
                        bi.setQuotaNo(quotaNo);
                        bi.setIsGroup("0"); 
                    }
                    
                    String quotaName = getCellValue(row.getCell(1));
                    bi.setQuotaName(quotaName);
                    
                    String units = getCellValue(row.getCell(2));
                    bi.setUnits(units);
                    
                    String quantities = getCellValue(row.getCell(3));
                    bi.setQuantities(quantities);
                    
                    String price = getCellValue(row.getCell(4));
                    bi.setPrice(price);
                    
                    String unitPrice = getCellValue(row.getCell(9));
                    bi.setUnitPrice(unitPrice);
                    
                    String artificiality = getCellValue(row.getCell(10));
                    bi.setArtificiality(artificiality);
                    
                    String materiels = getCellValue(row.getCell(11));
                    bi.setMateriels(materiels);
                    
                    String mech = getCellValue(row.getCell(12));
                    bi.setMech(mech);
                    
                    String materielsUnitPrice = getCellValue(row.getCell(13));
                    bi.setMaterielsUnitPrice(materielsUnitPrice);
                    
                    bi.setQuotaGroup(group);
                    bi.setBudgetNo(budget.getBudgetNo());
                    bi.setProjectID(budget.getProjectID());
                    
                    list.add(bi);
                }
            }
            
            this.batchSave(list);
            
            budget.setCreateDateTime(new Date());
            budget.setOperateTime(new Date());
            budget.setOperator(SecurityUtil.getUserId());
            budget.setStatus("10");
            
            this.save(budget);
            
            return rows - 1;
        }catch(Exception e){
            throw new CodeException("导入失败");
        }finally{
            if(workbook != null){
                workbook.close();
            }
        }
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

    @Override
    public BudgetImport getGroupByNo(String group) {
        BudgetImport bi = this.get(BudgetImport.class, group);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("quotaGroup", bi.getQuotaGroup());
        params.put("budgetNo", bi.getBudgetNo());
        return this.get("from BudgetImport where isGroup = '1' AND quotaGroup=:quotaGroup AND budgetNo=:budgetNo", params);
    }
    
    @Override
    public BudgetImport getGroupByGroupAndNo(String group, String budgetNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("quotaGroup", group);
        params.put("budgetNo", budgetNo);
        return this.get("from BudgetImport where isGroup = '1' AND quotaGroup=:quotaGroup AND budgetNo=:budgetNo", params);
    }
    
    @Override
    public List<Options> getProjectCombobox() {
        return this.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<BudgetImport> getBudgetImportList(String[] importIDs) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < importIDs.length; i++){
            sb.append("'");
            sb.append(importIDs[i]);
            sb.append("'");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        params.put("importIDs", importIDs);
        return this.find("from BudgetImport where id in (:importIDs)", params);
    }
    
    @Override
    public List<BudgetImport> getBudgetImportListByOrderNo(String orderNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderNo", orderNo);
        return this.find("from BudgetImport where orderNo =:orderNo", params);
    }
    
    @Override
    public List<BudgetTx> getBudgetTxListByGroup(String budgetNo, String group) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("quotaGroup", group);
        params.put("budgetNo", budgetNo);
        return this.find("from BudgetTx where budgetNo=:budgetNo AND quotaGroup =:quotaGroup", params);
    }
    
    @Override
    public BudgetTx getTxGroupByNo(String group) {
        BudgetTx bt = this.get(BudgetTx.class, group);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("quotaGroup", bt.getQuotaGroup());
        params.put("budgetNo", bt.getBudgetNo());
        return this.get("from BudgetTx where isGroup = '1' AND quotaGroup=:quotaGroup AND budgetNo=:budgetNo", params);
    }
    
    @Override
    public BudgetTx getTxGroupByGroupAndNo(String group, String budgetNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("quotaGroup", group);
        params.put("budgetNo", budgetNo);
        return this.get("from BudgetTx where isGroup = '1' AND quotaGroup=:quotaGroup AND budgetNo=:budgetNo", params);
    }

    @Override
    public BudgetTx checkPack(String budgetNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.get("from BudgetTx where budgetNo=:budgetNo AND orderNo='pack'", params);
    }
    
    @Override
    public Map<String, Materiel> getMatMap() {
        List<Materiel> list = this.find("from Materiel where isValid = 1");
        Map<String, Materiel> map = new HashMap<String, Materiel>();
        for(Materiel mat : list){
            map.put(mat.getMat_number(), mat);
        }
        return map;
    }

    @Override
    public Result startEstimateFlow(Budget budget, String processDefinitionKey) {
        budget.setStatus("20");
        budget.setUpdateDateTime(new Date());
        
        this.update(budget);
        
        Project project = this.get(Project.class, budget.getProjectID());
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "申请测算审批：" + budget.getBudgetName() + "，所属项目：" + project.getProjectName();
        
        //businessKey
        String businessKey = budget.getBudgetNo();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, budget.getBudgetNo());
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables,
                user.getId(), businessKey);
    }
    
    @Override
	public List<PurchaseApplyAudit> getQuantityAuditList(QueryCondition condition, PageInfo pageInfo) {

		String id = null;
		if (condition != null) {
			id = condition.getConditionMap().get("id").toString();
		}

		return this.getQuantityListMap(id);
	}

	@Override
	public List<PurchaseApplyAudit> getAmountAuditList(QueryCondition condition, PageInfo pageInfo) {
		String id = null;
		if (condition != null) {
			id = condition.getConditionMap().get("id").toString();
		}

		return this.getAmountListMap(id);
	}

	@Override
	public List<PurchaseApplyAudit> getQuantityListMap(String purchaseID) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ORI.id, ORI.budget_no, ORI.region_name, ORI.mat_number, ORI.mat_name, ORI.mat_standard, ORI.budget, COST.cost, PUR.apply FROM	 ");
		sb.append("(SELECT BE.id,BT.budget_no,BT.region_code,BT.region_name,BE.mat_number,BE.mat_name,BE.mat_standard,BE.quantity budget FROM ");
		sb.append("tbl_budget_tx BT,tbl_budget_estimate BE WHERE BT.id = BE.budget_tx_id AND BT.budget_no=? AND BT.region_code IS NOT NULL) ORI  ");
		sb.append("LEFT JOIN");
		sb.append("(SELECT PD.id,P.budget_no,PD.region_id,PD.mat_number,PD.mat_name,PD.mat_standard,SUM(PD.quantity) apply FROM tbl_purchase P, ");
		sb.append("tbl_purchase_det PD WHERE PD.purchase_id = P.id AND P.budget_no = ? AND P.status IN ( '20', '30' ) ");
		sb.append("GROUP BY P.budget_no,PD.mat_number,PD.mat_name,PD.mat_standard,PD.region_id) PUR ON PUR.budget_no = ORI.budget_no  ");
		sb.append("AND PUR.region_id = ORI.region_code  AND PUR.mat_number = ORI.mat_number  ");
		sb.append("LEFT JOIN ");
		sb.append("(SELECT P.budget_no,PD.region_id,PD.mat_number,PD.mat_name,PD.mat_standard,SUM(PD.quantity) cost FROM tbl_purchase P,tbl_purchase_det PD ");
		sb.append("WHERE PD.purchase_id = P.id AND P.budget_no = ? AND P.status IN ('40', '50', '60')  ");
		sb.append("GROUP BY P.budget_no,PD.mat_number,PD.mat_name,PD.mat_standard,PD.region_id) COST ON COST.budget_no = ORI.budget_no   ");
		sb.append("AND COST.region_id = ORI.region_code  AND COST.mat_number = ORI.mat_number  ORDER BY ORI.region_code ");

		return this.findMapBySql(sb.toString(), new Object[] { purchaseID, purchaseID, purchaseID },
				new Type[] { StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE }, null);
	}

	@Override
	public List<PurchaseApplyAudit> getAmountListMap(String purchaseID) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ORI.id, ORI.budget_no, ORI.region_name, ORI.budget, COST.cost, PUR.apply FROM ");
		sb.append("(SELECT id, budget_no, region_code, region_name, unit_price AS budget ");
		sb.append("FROM tbl_budget_tx WHERE budget_no = ? AND region_code is not null) ORI ");
		sb.append("LEFT JOIN");
		sb.append("(SELECT PD.id,P.budget_no, PD.region_id, SUM(PD.quantity*PD.price) apply ");
		sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
		sb.append("WHERE PD.purchase_id = P.id AND P.budget_no = ? AND P.status IN ('20', '30')");
		sb.append("GROUP BY P.budget_no, PD.region_id) PUR ");
		sb.append("ON PUR.budget_no = ORI.budget_no AND PUR.region_id = ORI.region_code  ");
		sb.append("LEFT JOIN");
		sb.append("(SELECT PD.id,P.budget_no, PD.region_id, SUM(PD.quantity*PD.price) cost  ");
		sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
		sb.append("WHERE PD.purchase_id = P.id AND P.budget_no = ? AND P.status IN ('40', '50', '60') ");
		sb.append("GROUP BY P.budget_no, PD.region_id) COST ");
		sb.append("ON COST.budget_no = ORI.budget_no AND COST.region_id = ORI.region_code ");
		sb.append("ORDER BY ORI.region_code ");

		return this.findMapBySql(sb.toString(), new Object[] {purchaseID, purchaseID, purchaseID },
				new Type[] {StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE }, null);
	}

    @Override
    public List<BudgetTx> getBudgetTxList(String budgetNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.find("from BudgetTx where budgetNo = :budgetNo", params);
    }

    @Override
    public List<BudgetImport> getBudgetImportList(String budgetNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.find("from BudgetImport where budgetNo = :budgetNo", params);
    }

    @Override
    public List<BudgetEstimate> getBudgetEstimateList(String budgetNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.find("from BudgetEstimate where budgetNo = :budgetNo", params);
    }
    
    @Override
    public List<BudgetEstimate> getBudgetEstimateCount(String budgetTxID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetTxID", budgetTxID);
        return this.find("from BudgetEstimate where budgetTxID = :budgetTxID", params);
    }
    
    @Override
    public List<BudgetLabour> getBudgetLabourCount(String budgetTxID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetTxID", budgetTxID);
        return this.find("from BudgetLabour where budgetTxID = :budgetTxID", params);
    }
    
    @Override
    public List<BudgetMech> getBudgetMechCount(String budgetTxID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetTxID", budgetTxID);
        return this.find("from BudgetMech where budgetTxID = :budgetTxID", params);
    }

    @Override
    public Materiel getEstimateImportMat(String name, String standard, String unit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("standard", standard);
        Materiel mat = this.get("from Materiel where mat_name=:name AND mat_standard=:standard", params);
        if(mat == null){
            mat = new Materiel();
            mat.setCreate_time(new Date());
            mat.setMat_name(name);
            mat.setMat_number("CS"+GUID.getTxNo16());
            mat.setMat_standard(standard);
            mat.setUnit(unit);
            mat.setParent_ID("4028828e645e868401649c473716005f");
            mat.setIsValid("1");
            
            this.save(mat);
        }
        return mat;
    }
    
}
