/**
 * 
 */
package com.radish.master.controller.workmanage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.CodeException;
import com.radish.master.entity.wechat.Attendance;
import com.radish.master.entity.wechat.Wen;


/**
 * @author tonyd
 *
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
	
	@Autowired
	private BaseService baseService;
	
	private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "workmanage/attendance/list";
    }
	
    @RequestMapping(value="/detail/{name}",method = RequestMethod.GET)
    public String detail(@PathVariable("name") String name,HttpServletRequest request){
        request.setAttribute("name", name);
        return "workmanage/attendance/attend";
    }
    
    @RequestMapping(value="/import",method = RequestMethod.GET)
    public String toImport(){
        return "workmanage/attendance/import";
    }
    
    @RequestMapping(value="/importwen",method = RequestMethod.GET)
    public String toImportWen(){
        return "workmanage/attendance/import_wen";
    }
	
    @RequestMapping(method = RequestMethod.POST, value = "/doimport")
    @ResponseBody
    public Result importExcel(@RequestParam(value = "file", required = false) MultipartFile file, String month) throws IOException{
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
    	
    	try{
    		Date date = format.parse(month);
    	}catch (Exception e) {
    		return new Result(false,"日期格式不正确!");
		}
    	
    	
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
            
            Sheet sheet = workbook.getSheet("上下班打卡_月报");
            
            int rows = sheet.getLastRowNum();
            
            if(rows == 0){
                throw new CodeException("考勤表格中无数据");
            }  
            
            List<Attendance> list = new ArrayList<Attendance>();
            
            String group = "";
            
            for(int i = 4; i<=rows; i++){
                Row row = sheet.getRow(i);
                if(row != null){
                	Attendance bi = new Attendance();
                    
                    String monthA = getCellValue(row.getCell(0)).replaceAll("-", "");
                    if(!monthA.equals(month)){
                    	throw new CodeException("考勤表格中包含非所选月份数据!");
                    }
                    bi.setMonth(monthA);
                    
                    String name = getCellValue(row.getCell(1));
                    bi.setName(name);
                    
                    String account = getCellValue(row.getCell(2));
                    bi.setAccount(account);
                    
                    String suppose = getCellValue(row.getCell(5));
                    bi.setSuppose(suppose);
                    
                    String normal = getCellValue(row.getCell(6));
                    bi.setNormal(normal);
                    
                    String exception = getCellValue(row.getCell(7));
                    bi.setException(exception);
                    
                    String early = getCellValue(row.getCell(8));
                    bi.setEarly(early);
                    
                    String late = getCellValue(row.getCell(9));
                    bi.setLate(late);
                    
                    String completion = getCellValue(row.getCell(10));
                    bi.setCompletion(completion);
                    
                    String total = getCellValue(row.getCell(11));
                    bi.setTotal(total);
                    
                    list.add(bi);
                }
            }
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("month", month);
            List<Attendance> attendanceList = baseService.find("from Attendance where month =:month", params);
            for(Attendance tx : attendanceList){
                baseService.delete(tx);
            }
            
            
            baseService.batchSave(list);
            
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
    
    @RequestMapping(method = RequestMethod.POST, value = "/doimportwen")
    @ResponseBody
    public Result importExcelWen(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
        
        
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
                throw new CodeException("表格中无数据");
            }  
            
            List<Wen> list = new ArrayList<Wen>();
            
            String group = "";
            
            for(int i = 0; i<=rows; i++){
                Row row = sheet.getRow(i);
                if(row != null){
                    Wen bi = new Wen();
                    
                    String bingli = getCellValue(row.getCell(0));
                    bi.setBingli(bingli);

                    String bingan = getCellValue(row.getCell(1));
                    bi.setBingan(bingan);
                    
                    if("1409761".equals(bingan)){
                        System.out.println("that's it");
                    }
                    
                    String sex = getCellValue(row.getCell(2));
                    bi.setSex(sex);
                    
                    String age = getCellValue(row.getCell(3));
                    bi.setAge(age);
                    
                    String getdate = getCellValue(row.getCell(4));
                    bi.setGetdate(getdate);
                    
                    String zhenduan = getCellValue(row.getCell(5));
                    bi.setZhenduan(zhenduan);
                    
                    String jielun = getCellValue(row.getCell(6));
                    bi.setJielun(jielun);
                    
                    String shuju = "nan";
                    String zuida = "nan";
                    int cmIndex = jielun.indexOf("cm");
                    
                    int index = -1;
                    int xiuzheng = 0;
                    
                    if(cmIndex == -1){
                        shuju = "0";
                        zuida = "0";
                        index = 1;
                    }
                    
                    if(index == -1){
                        index = jielun.indexOf("直径");
                        xiuzheng = 2;
                    }
                    
                    if(index == -1){
                        index = jielun.indexOf("最大径");
                        xiuzheng = 3;
                    }
                    
                    if(index == -1){
                        index = jielun.indexOf("肿瘤大小");
                        xiuzheng = 4;
                        cmIndex = jielun.indexOf("cm", index);
                        shuju = jielun.substring(index + xiuzheng, cmIndex);
                        String[] array = shuju.split("×");
                        String temp = "-1";
                        for(int k=0;k<array.length;k++) {
                            for(int j=i+1;j<array.length;j++) {
                                if(Integer.valueOf(array[i])>=Integer.valueOf(array[j])) {
                                    temp = array[i];
                                    array[i] = array[j];
                                    array[j] = temp;
                                }
                            }
                        }
                        zuida = array[0];
                    }
                    
                    if(index == -1){
                        index = jielun.indexOf("大小");
                        xiuzheng = 2;
                        cmIndex = jielun.indexOf("cm", index);
                        shuju = jielun.substring(index + xiuzheng, cmIndex);
                        String[] array = shuju.split("×");
                        String temp = "-1";
                        for(int k=0;k<array.length;k++) {
                            for(int j=i+1;j<array.length;j++) {
                                if(Integer.valueOf(array[i])>=Integer.valueOf(array[j])) {
                                    temp = array[i];
                                    array[i] = array[j];
                                    array[j] = temp;
                                }
                            }
                        }
                        zuida = array[0];
                    }
                    
                    if("nan".equals(shuju)){
                       System.out.println(bingan);
                       System.out.println(index);
                       System.out.println(xiuzheng);
                       System.out.println(cmIndex);
                       System.out.println("----------------------");
                       if(cmIndex < index){
                           shuju = "有多个数值";
                       }else{
                           shuju =  jielun.substring(index + xiuzheng, cmIndex); 
                       }
                    }
                    
                    if("nan".equals(zuida)){
                        zuida = shuju;
                    }
                    
                    bi.setShuju(shuju);
                    bi.setZuida(zuida);
                    
                    String biaoben = getCellValue(row.getCell(7));
                    bi.setBiaoben(biaoben);
                    
                    String baogao = getCellValue(row.getCell(8));
                    bi.setBaogao(baogao);
                    
                    list.add(bi);
                }
            }
            
            baseService.batchSave(list);
            
        }catch (CodeException ce) {
            return new Result(false,ce.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"导入失败");
        }finally{
            if(workbook != null){
                workbook.close();
            }
        }
        
        return new Result(true,file.getOriginalFilename());
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
}
