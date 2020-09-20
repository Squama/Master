/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.fixedassets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.entity.fixedassets.FixedAssetsPur;
import com.radish.master.entity.fixedassets.FixedAssetsPurChannel;
import com.radish.master.entity.fixedassets.FixedAssetsPurTx;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.pojo.AssetsApplyVO;
import com.radish.master.pojo.RowEdit;
import com.radish.master.service.CommonService;
import com.radish.master.system.GUID;

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
@RequestMapping("/fixedassets/apply")
public class FixedAssetsApplyController {
    
    private static Logger logger = LoggerFactory.getLogger(FixedAssetsApplyController.class);
    
    @Resource
    private CommonService commonService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    private static final String projectFilePath = PropertiesUtil.getValue("projectFilePath");

    public static Map fileIconMap = new HashMap();

    static {
        fileIconMap.put("doc", "<i class='fa fa-file-word-o text-primary'></i>");
        fileIconMap.put("docx", "<i class='fa fa-file-word-o text-primary'></i>");
        fileIconMap.put("xls", "<i class='fa fa-file-excel-o text-success'></i>");
        fileIconMap.put("xlsx", "<i class='fa fa-file-excel-o text-success'></i>");
        fileIconMap.put("ppt", "<i class='fa fa-file-powerpoint-o text-danger'></i>");
        fileIconMap.put("pptx", "<i class='fa fa-file-powerpoint-o text-danger'></i>");
        fileIconMap.put("jpg", "<i class='fa fa-file-photo-o text-warning'></i>");
        fileIconMap.put("pdf", "<i class='fa fa-file-pdf-o text-danger'></i>");
        fileIconMap.put("zip", "<i class='fa fa-file-archive-o text-muted'></i>");
        fileIconMap.put("rar", "<i class='fa fa-file-archive-o text-muted'></i>");
        fileIconMap.put("default", "<i class='fa fa-file-o'></i>");
    }
    
    /**
     * 固定资产start
     */
    
    @RequestMapping(value="/assets/list",method = RequestMethod.GET)
    public String assetsList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        return "fixedassets/apply/assets_list";
    }
    
    @RequestMapping(value="/assetschannel/{id}",method = RequestMethod.GET)
    public String assetsChannel(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("purID", id);
        
        return "fixedassets/apply/activiti/assets_channel";
    }
    
    @RequestMapping(value="/contractdetail/{id}",method = RequestMethod.GET)
    public String contractDetail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("purID", id);
        
        return "fixedassets/apply/activiti/contract_detail";
    }
    
    @RequestMapping(value = "/assetschannel/rowedit", method = RequestMethod.POST)
    @ResponseBody
    public String singleEstimate(String action, HttpServletRequest request) throws Exception {
        String id = "";
        String field = "";
        String value = "";
        
        RowEdit re = new RowEdit();
        List<Object> list = new ArrayList<Object>();
        
        Enumeration<String> key = request.getParameterNames();
        while (key.hasMoreElements()) {
            String paramName = (String) key.nextElement();
            if ("action".equals(paramName)) {
                continue;
            }
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    int idIndexStart = paramName.indexOf("[");
                    int idIndexEnd = paramName.indexOf("]");
                    int fieldIndexStart = paramName.lastIndexOf("[");
                    int fieldIndexEnd = paramName.lastIndexOf("]");
                    id = paramName.substring(idIndexStart + 1, idIndexEnd);
                    field = paramName.substring(fieldIndexStart + 1, fieldIndexEnd);
                    value = paramValue;
                }
            }
        }

        FixedAssetsPurTx detail = commonService.get(FixedAssetsPurTx.class, id);

        Method set = detail.getClass().getMethod("set" + commonService.captureName(field), String.class);
        set.invoke(detail, value);

        commonService.save(detail);
        
        list.add(detail);
        re.setData(list);
        return JSONArray.toJSONString(re);
    }
    
    @RequestMapping(value = "/assets/start", method = RequestMethod.POST)
    @ResponseBody
    public Result assetsStart(String id) {
        FixedAssetsPur fixedAssetsPur = commonService.get(FixedAssetsPur.class, id);
        fixedAssetsPur.setStatus("11");
        fixedAssetsPur.setUpdateDateTime(new Date());
        
        commonService.update(fixedAssetsPur);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "请购固定资产，请购单名称：" + fixedAssetsPur.getName();
        
        //businessKey
        String businessKey = fixedAssetsPur.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("assetsApply", name, variables,
                user.getId(), businessKey);
    }
    
    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    @ResponseBody
    public FileResult uploadMultipleFile(@RequestParam(value = "file", required = false) MultipartFile[] files,
            String id, String fileField, HttpServletRequest request, HttpServletResponse response) throws IOException {

        FileResult msg = new FileResult();

        ArrayList<Integer> arr = new ArrayList<>();
        // 缓存当前的文件
        List<ProjectFileItem> fileList = new ArrayList<>();

        FixedAssetsPur fixedAssetsPur = commonService.get(FixedAssetsPur.class, id);

        String fileID = fixedAssetsPur.getFileID();
        if (StrUtil.isEmpty(fileID)) {
            fileID = GUID.getTxNo();
            fixedAssetsPur.setFileID(fileID);
        }

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];

            if (!file.isEmpty()) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    File dir = new File(projectFilePath);
                    if (!dir.exists())
                        dir.mkdirs();
                    // 这样也可以上传同名文件了
                    String filePrefixFormat = "yyyyMMddHHmmssS";
                    String sourceName = file.getOriginalFilename();
                    String fileName = DateUtil.format(new Date(), filePrefixFormat) + "_" + file.getOriginalFilename();
                    String filePath = dir.getAbsolutePath() + File.separator + fileName;
                    File serverFile = new File(filePath);
                    // 将文件写入到服务器
                    file.transferTo(serverFile);
                    ProjectFileItem item = new ProjectFileItem();
                    item.setBatchNo(fileID);
                    item.setUploadUserID(SecurityUtil.getUserId());
                    item.setFileName(fileName);
                    item.setFilePath(projectFilePath + File.separator + fileName);
                    item.setFileSize(file.getSize());
                    item.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
                    item.setSourceName(sourceName);

                    item.setUpdateDateTime(new Date());

                    commonService.save(item);
                    fileList.add(item);

                    logger.info("Server File Location=" + serverFile.getAbsolutePath());
                } catch (Exception e) {
                    logger.error(file.getOriginalFilename() + "上传发生异常，异常原因：" + e.getMessage());
                    arr.add(i);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }
            } else {
                arr.add(i);
            }
        }

        if (!arr.isEmpty()) {
            msg.setError("文件上传失败！");
            msg.setErrorkeys(arr);
        }
        commonService.update(fixedAssetsPur);
        FileResult preview = getPreivewSettings(fileList, id, fileField, request);
        msg.setInitialPreview(preview.getInitialPreview());
        msg.setInitialPreviewConfig(preview.getInitialPreviewConfig());
        msg.setFileIds(preview.getFileIds());
        return msg;
    }
    
    public FileResult getPreivewSettings(List<ProjectFileItem> fileList, String projectID, String fileField,
            HttpServletRequest request) {
        FileResult fileResult = new FileResult();
        List<String> previews = new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs = new ArrayList<>();
        // 缓存当前的文件
        String[] fileArr = new String[fileList.size()];
        int index = 0;
        for (ProjectFileItem sysFile : fileList) {
            // 上传后预览 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
            // 如果其他文件可预览txt、xml、html、pdf等 可在此配置
            String dirPath = request.getContextPath() + "/project/showImage?imageID=" + sysFile.getId();
            if ("jpg".equalsIgnoreCase(sysFile.getFileType()) || "jpeg".equalsIgnoreCase(sysFile.getFileType())
                    || "png".equalsIgnoreCase(sysFile.getFileType())) {
                previews.add("<img src='" + dirPath + "' class='file-preview-image kv-preview-data' "
                        + "style='width:auto;height:160px' alt='" + sysFile.getSourceName() + "' title='"
                        + sysFile.getSourceName() + "''>");
            } else {
                previews.add("<div class='kv-preview-data file-preview-other-frame'><div class='file-preview-other'>"
                        + "<span class='file-other-icon'>" + getFileIcon(sysFile.getSourceName())
                        + "</span></div></div>");
            }
            // 上传后预览配置
            FileResult.PreviewConfig previewConfig = new FileResult.PreviewConfig();
            previewConfig.setWidth("120px");
            previewConfig.setCaption(sysFile.getSourceName());
            previewConfig.setKey(sysFile.getId());
            previewConfig.setExtra(new FileResult.PreviewConfig.Extra(projectID, fileField));
            previewConfig.setSize(sysFile.getFileSize());
            previewConfigs.add(previewConfig);
            fileArr[index++] = sysFile.getId();
        }
        fileResult.setInitialPreview(previews);
        fileResult.setInitialPreviewConfig(previewConfigs);
        fileResult.setFileIds(StrUtil.join(fileArr));
        return fileResult;
    }
    
    public String getFileIcon(String fileName) {
        String ext = StrUtil.getExtName(fileName);
        return fileIconMap.get(ext) == null ? fileIconMap.get("default").toString() : fileIconMap.get(ext).toString();
    }
    
    @RequestMapping(value="/editchannel",method = RequestMethod.GET)
    public String editChannel(String id, HttpServletRequest request){
        request.setAttribute("purTxID", id);
        return "fixedassets/apply/activiti/edit_channel";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savechannel")
    @ResponseBody
    public Result saveChannel(FixedAssetsPurChannel fapc, HttpServletRequest request){
    	fapc.setUpdateDateTime(new Date());
    	fapc.setCreateDateTime(new Date());
        
        try {
            commonService.save(fapc);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, fapc);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/deletechannel")
    @ResponseBody
    public Result deleteChannel(String id, HttpServletRequest request){
    	FixedAssetsPurChannel tx = commonService.get(FixedAssetsPurChannel.class, id);
        commonService.delete(tx);
        return new Result(true, "success");
    }
    
    @RequestMapping(value="/gotochannel",method = RequestMethod.GET)
    public String gotoChannel(String id, HttpServletRequest request){
        request.setAttribute("purTxID", id);
        return "fixedassets/apply/activiti/choose_channel";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/choosechannel")
    @ResponseBody
    private Result chooseChannel(FixedAssetsPurChannel fapc, HttpServletRequest request) {
        FixedAssetsPurTx tx = commonService.get(FixedAssetsPurTx.class, fapc.getPurTxID());

        tx.setChannelID(fapc.getId());
        tx.setChannelName(fapc.getChannelName());
        tx.setPrice(fapc.getPrice());
        tx.setUpdateDateTime(new Date());

        commonService.save(tx);

        return new Result(true);
    }
    
    /** 固定资产end */
    
    
    /** 器具、工具start */
    @RequestMapping(value="/tool/list",method = RequestMethod.GET)
    public String toolList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        return "fixedassets/apply/tool_list";
    }
    
    @RequestMapping(value = "/tool/start", method = RequestMethod.POST)
    @ResponseBody
    public Result toolStart(String id) {
        FixedAssetsPur fixedAssetsPur = commonService.get(FixedAssetsPur.class, id);
        fixedAssetsPur.setStatus("21");
        fixedAssetsPur.setUpdateDateTime(new Date());
        
        commonService.update(fixedAssetsPur);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "请购器具、工具，请购单名称：" + fixedAssetsPur.getName();
        
        //businessKey
        String businessKey = fixedAssetsPur.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("toolApply", name, variables,
                user.getId(), businessKey);
    }
    
    /** 器具、工具end */
    
    /** 办公用品start */
    @RequestMapping(value="/office/list",method = RequestMethod.GET)
    public String officeList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        return "fixedassets/apply/office_list";
    }
    
    
    
    @RequestMapping(value = "/office/start", method = RequestMethod.POST)
    @ResponseBody
    public Result officeStart(String id) {
        FixedAssetsPur fixedAssetsPur = commonService.get(FixedAssetsPur.class, id);
        fixedAssetsPur.setStatus("31");
        fixedAssetsPur.setUpdateDateTime(new Date());
        
        commonService.update(fixedAssetsPur);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "请购办公用品，请购单名称：" + fixedAssetsPur.getName();
        
        //businessKey
        String businessKey = fixedAssetsPur.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("officeApply", name, variables,
                user.getId(), businessKey);
    }
    
    /** 办公用品end */
    public  String maxNum(){ 
		List<String> result = commonService.find("select max(mat.id) from com.radish.master.entity.review.MaxNumber mat");
		if(result.size()>0&&!"".equals(result.get(0))&&StringHelper.isNotEmpty(result.get(0))){
			String num = result.get(0);
			
			MaxNumber m = new MaxNumber();
			Integer newId = Integer.valueOf(num)+1;
			m.setId(newId+"");
			commonService.save(m);
			return num;
		}else{
			MaxNumber m = new MaxNumber();
			m.setId("1001");
			commonService.save(m);
			return "1001";
		}
	}
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String assetsAdd(String faType, HttpServletRequest request){
        String deptID = SecurityUtil.getUser().getDeptId();
        Org org = commonService.get(Org.class, deptID);
        if(org == null){
            request.setAttribute("deptID", "nondept");
            request.setAttribute("deptName", "nondept");
        }else{
            request.setAttribute("deptID", deptID);
            request.setAttribute("deptName", org.getName());
        }
        request.setAttribute("faType", faType);
        //编号
        String str =maxNum();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        
		String strs = sdf.format(new Date())+str;
		request.setAttribute("number", strs);
        return "fixedassets/apply/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String assetsEdit(String id,String faType, HttpServletRequest request){
        request.setAttribute("purID", id);
        request.setAttribute("faType", faType);
        /*String deptID = SecurityUtil.getUser().getDeptId();
        Org org = commonService.get(Org.class, deptID);
        if(org == null){
            request.setAttribute("deptID", "nondept");
            request.setAttribute("deptName", "nondept");
        }else{
            request.setAttribute("deptID", deptID);
            request.setAttribute("deptName", org.getName());
        }*/
        
        return "fixedassets/apply/edit";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("purID", id);
        
        return "fixedassets/apply/detail";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(FixedAssetsPur fap, HttpServletRequest request){
        fap.setStatus("10");
        fap.setUpdateDateTime(new Date());
        fap.setCreateDateTime(new Date());
        fap.setApplyer(SecurityUtil.getUser().getName());
        
        try {
            commonService.save(fap);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, fap);
    }
    @RequestMapping(method = RequestMethod.POST, value="/delete")
    @ResponseBody
    public Result delete(String id){
    	FixedAssetsPur sj = commonService.get(FixedAssetsPur.class, id);
        try {
            commonService.delete(sj);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/deletedetail")
    @ResponseBody
    public Result deleteDet(String id, HttpServletRequest request){
        FixedAssetsPurTx tx = commonService.get(FixedAssetsPurTx.class, id);
        commonService.delete(tx);
        return new Result(true, "success");
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/recentstk/save")
    @ResponseBody
    public Result recentstkSave(AssetsApplyVO vo, HttpServletRequest request){
        
        if("10".equals(vo.getFaType())){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("purID", vo.getPurID());
            List<FixedAssetsPurTx> list = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
            if(!list.isEmpty()){
                return new Result(false,"固定资产只能有一条明细");
            }
        }
        
        FixedAssetsStk stk = commonService.get(FixedAssetsStk.class, vo.getAssetsStkID());
        
        FixedAssetsPurTx tx = new FixedAssetsPurTx();
        tx.setStkID(vo.getAssetsStkID());
        tx.setPurID(vo.getPurID());
        tx.setName(stk.getName());
        tx.setEnglishName(stk.getEnglishName());
        tx.setModel(stk.getModel());
        tx.setNorm(stk.getNorm());
        tx.setUnit(stk.getUnit());
        tx.setPrice(stk.getPrice());
        tx.setQuantity(vo.getNum());
        tx.setVendor(stk.getVendor());
        tx.setUpdateDateTime(new Date());
        tx.setCreateDateTime(new Date());
        
        try {
            commonService.save(tx);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, tx);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/newstk/save")
    @ResponseBody
    public Result newSave(FixedAssetsPurTx tx, HttpServletRequest request){
        
        if("10".equals(tx.getFaType())){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("purID", tx.getPurID());
            List<FixedAssetsPurTx> list = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
            if(!list.isEmpty()){
                return new Result(false,"固定资产只能有一条明细");
            }
        }
        
        tx.setUpdateDateTime(new Date());
        tx.setCreateDateTime(new Date());
        
        try {
            commonService.save(tx);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, tx);
    }
    
    @RequestMapping(value="/addstock",method = RequestMethod.GET)
    public String addStock(String faType, String purID, HttpServletRequest request){
        
        request.setAttribute("faType", faType);
        request.setAttribute("purID", purID);
        request.setAttribute("stkOptions", JSONArray.toJSONString(commonService.getAssetsComboboxAndStk(faType)));
        
        return "fixedassets/apply/recent_stk";
    }
    
    @RequestMapping(value="/newstock",method = RequestMethod.GET)
    public String newStock(String faType, String purID, HttpServletRequest request){
        
        request.setAttribute("faType", faType);
        request.setAttribute("purID", purID);
        
        return "fixedassets/apply/new_stk";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getstkdet")
    @ResponseBody
    public FixedAssetsStk getStkDet(String stkID){
        return commonService.get(FixedAssetsStk.class, stkID);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getpur")
    @ResponseBody
    public FixedAssetsPur getPur(String purID){
        FixedAssetsPur fixedAssetsPur = commonService.get(FixedAssetsPur.class, purID);
        List<ProjectFileItem> fileList = new ArrayList<>();
        DetachedCriteria criteria = DetachedCriteria.forClass(ProjectFileItem.class);
        criteria.add(Restrictions.eq("batchNo", fixedAssetsPur.getFileID()));
        criteria.addOrder(Order.asc("createDateTime"));
        fileList = commonService.findByCriteria(criteria);
        if (fileList.size() > 0) {
            fixedAssetsPur.setFileID(fileList.get(0).getId());
        }
        
        return fixedAssetsPur;
    }
    
}
