package com.radish.master.service.impl;


import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.tool.message.entity.MessageGroup;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetImport;
import com.radish.master.pojo.Options;
import com.radish.master.service.BudgetService;
import com.radish.master.system.CodeException;
import com.radish.master.system.GUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
                    }else{
                        bi.setQuotaNo(quotaNo);
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
            
            this.save(budget);
            
            return rows - 1;
        }catch(Exception e){
            throw new CodeException(e.getMessage());
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
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("quotaGroup", group);
        return this.get("from BudgetImport where quotaNo IS NULL AND quotaGroup=:quotaGroup", params);
    }

    @Override
    public List<Options> getProjectCombobox() {
        return this.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
        
    }
    
}
