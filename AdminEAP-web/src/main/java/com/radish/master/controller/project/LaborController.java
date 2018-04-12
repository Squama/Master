/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.controller.ProjectController;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.service.BudgetService;
import com.radish.master.service.ProjectService;
import com.radish.master.system.GUID;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年3月5日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/labor")
public class LaborController {
    
    private static Logger logger= LoggerFactory.getLogger(ProjectController.class);
    
    @Resource
    private ProjectService projectService;
    
    @Resource
    private BudgetService budgetService;

    private static final String projectFilePath=PropertiesUtil.getValue("projectFilePath");
    
    public static Map fileIconMap=new HashMap();
    
    static {
        fileIconMap.put("doc" ,"<i class='fa fa-file-word-o text-primary'></i>");
        fileIconMap.put("docx","<i class='fa fa-file-word-o text-primary'></i>");
        fileIconMap.put("xls" ,"<i class='fa fa-file-excel-o text-success'></i>");
        fileIconMap.put("xlsx","<i class='fa fa-file-excel-o text-success'></i>");
        fileIconMap.put("ppt" ,"<i class='fa fa-file-powerpoint-o text-danger'></i>");
        fileIconMap.put("pptx","<i class='fa fa-file-powerpoint-o text-danger'></i>");
        fileIconMap.put("jpg" ,"<i class='fa fa-file-photo-o text-warning'></i>");
        fileIconMap.put("pdf" ,"<i class='fa fa-file-pdf-o text-danger'></i>");
        fileIconMap.put("zip" ,"<i class='fa fa-file-archive-o text-muted'></i>");
        fileIconMap.put("rar" ,"<i class='fa fa-file-archive-o text-muted'></i>");
        fileIconMap.put("default" ,"<i class='fa fa-file-o'></i>");
    }
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
    	request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/labor/labor_list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        
        return "projectmanage/labor/labor_add";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/labor/labor_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/uploadfile",method = RequestMethod.POST)
    public String step2(Labor labor, HttpServletRequest request){
        request.setAttribute("id", labor.getId());
        return "projectmanage/labor/labor_add_step2";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edituploadfile",method = RequestMethod.POST)
    public String editstep2(Labor labor, HttpServletRequest request){
        request.setAttribute("id", labor.getId());
        return "projectmanage/labor/labor_edit_step2";
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Labor labor, HttpServletRequest request){
        if(StrUtil.isEmpty(labor.getId())){
            labor.setCreateDateTime(new Date());
            labor.setContractPrice("0");
            labor.setStatus("10");
            projectService.save(labor);
        }else{
            Labor oldLabor = projectService.get(Labor.class, labor.getId());
            oldLabor.setProjectName(labor.getProjectName());
            oldLabor.setConstructionManager(labor.getConstructionManager());
            oldLabor.setUpdateDateTime(new Date());
            projectService.update(oldLabor);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", labor.getId());
        return new Result(true, map);
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/savecontract")
    @ResponseBody
    public Result saveContract(Labor labor, HttpServletRequest request){
        
        Labor oldLabor = projectService.get(Labor.class, labor.getId());
        oldLabor.setContractPrice(labor.getContractPrice());
        projectService.update(oldLabor);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", labor.getId());
        return new Result(true, map);
    }
    
    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    @ResponseBody
    public FileResult uploadMultipleFile(@RequestParam(value = "file", required = false) MultipartFile[] files, String id, String fileField,
            HttpServletRequest request, HttpServletResponse response) throws IOException{

        FileResult msg = new FileResult();

        ArrayList<Integer> arr = new ArrayList<>();
        //缓存当前的文件
        List<ProjectFileItem> fileList=new ArrayList<>();
        
        Labor labor = projectService.get(Labor.class, id);
        
        String fileID = labor.getContractFile();
        if(StrUtil.isEmpty(fileID)){
            fileID = GUID.getTxNo();
            labor.setContractFile(fileID);
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
                    //这样也可以上传同名文件了
                    String filePrefixFormat="yyyyMMddHHmmssS";
                    String sourceName = file.getOriginalFilename();
                    String fileName=DateUtil.format(new Date(),filePrefixFormat)+"_"+file.getOriginalFilename();
                    String filePath=dir.getAbsolutePath() + File.separator + fileName;
                    File serverFile = new File(filePath);
                    //将文件写入到服务器
                    file.transferTo(serverFile);
                    ProjectFileItem item = new ProjectFileItem();
                    item.setBatchNo(fileID);
                    item.setUploadUserID(SecurityUtil.getUserId());
                    item.setFileName(fileName);
                    item.setFilePath(projectFilePath+File.separator+fileName);
                    item.setFileSize(file.getSize());
                    item.setFileType(fileName.substring(fileName.lastIndexOf(".")+1));
                    item.setSourceName(sourceName);
                    
                    item.setUpdateDateTime(new Date());
                    
                    projectService.save(item);
                    fileList.add(item);

                    logger.info("Server File Location=" + serverFile.getAbsolutePath());
                } catch (Exception e) {
                    logger.error(   file.getOriginalFilename()+"上传发生异常，异常原因："+e.getMessage());
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

        if(!arr.isEmpty()) {
            msg.setError("文件上传失败！");
            msg.setErrorkeys(arr);
        }
        projectService.update(labor);
        FileResult preview=getPreivewSettings(fileList, id, fileField, request);
        msg.setInitialPreview(preview.getInitialPreview());
        msg.setInitialPreviewConfig(preview.getInitialPreviewConfig());
        msg.setFileIds(preview.getFileIds());
        return msg;
    }
    
    /**
     * 回填已有文件的缩略图
     * @param fileList 文件列表
     * @param request
     * @return initialPreiview initialPreviewConfig fileIds
     */
    public FileResult getPreivewSettings(List<ProjectFileItem> fileList, String projectID, String fileField, HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (ProjectFileItem sysFile : fileList) {
            //上传后预览  该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
            String dirPath = request.getContextPath() + "/project/showImage?imageID=" + sysFile.getId();
            if("jpg".equalsIgnoreCase(sysFile.getFileType()) || "jpeg".equalsIgnoreCase(sysFile.getFileType()) || "png".equalsIgnoreCase(sysFile.getFileType())) {
                previews.add("<img src='"+dirPath+"' class='file-preview-image kv-preview-data' " +
                        "style='width:auto;height:160px' alt='" + sysFile.getSourceName() + "' title='" + sysFile.getSourceName() + "''>");
            }else{
                previews.add("<div class='kv-preview-data file-preview-other-frame'><div class='file-preview-other'>" +
                        "<span class='file-other-icon'>"+getFileIcon(sysFile.getSourceName())+"</span></div></div>");
            }
            //上传后预览配置
            FileResult.PreviewConfig previewConfig=new FileResult.PreviewConfig();
            previewConfig.setWidth("120px");
            previewConfig.setCaption(sysFile.getSourceName());
            previewConfig.setKey(sysFile.getId());
            previewConfig.setExtra(new FileResult.PreviewConfig.Extra(projectID, fileField));
            previewConfig.setSize(sysFile.getFileSize());
            previewConfigs.add(previewConfig);
            fileArr[index++]=sysFile.getId();
        }
        fileResult.setInitialPreview(previews);
        fileResult.setInitialPreviewConfig(previewConfigs);
        fileResult.setFileIds(StrUtil.join(fileArr));
        return fileResult;
    }
    
    public FileResult getPreivewSettingsPreview(List<ProjectFileItem> fileList, String projectID, String fileField, HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (ProjectFileItem sysFile : fileList) {
            //上传后预览  该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
            String dirPath = request.getContextPath() + "/project/showImage?imageID=" + sysFile.getId();
            if("jpg".equalsIgnoreCase(sysFile.getFileType()) || "jpeg".equalsIgnoreCase(sysFile.getFileType()) || "png".equalsIgnoreCase(sysFile.getFileType())) {
                previews.add("<img src='"+dirPath+"' class='file-preview-image kv-preview-data' " +
                        "style='width:auto;height:80px' alt='" + sysFile.getSourceName() + "' title='" + sysFile.getSourceName() + "''>");
            }else{
                previews.add("<div class='kv-preview-data file-preview-other-frame'><div class='file-preview-other'>" +
                        "<span class='file-other-icon'>"+getFileIcon(sysFile.getSourceName())+"</span></div></div>");
            }
            //上传后预览配置
            FileResult.PreviewConfig previewConfig=new FileResult.PreviewConfig();
            previewConfig.setWidth("80px");
            previewConfig.setCaption(sysFile.getSourceName());
            previewConfig.setKey(sysFile.getId());
            previewConfig.setExtra(new FileResult.PreviewConfig.Extra(projectID, fileField));
            previewConfig.setSize(sysFile.getFileSize());
            previewConfigs.add(previewConfig);
            fileArr[index++]=sysFile.getId();
        }
        fileResult.setInitialPreview(previews);
        fileResult.setInitialPreviewConfig(previewConfigs);
        fileResult.setFileIds(StrUtil.join(fileArr));
        return fileResult;
    }
    
    /**
     * 根据文件名获取icon
     * @param fileName 文件
     * @return
     */
    public String getFileIcon(String fileName){
        String ext= StrUtil.getExtName(fileName);
        return fileIconMap.get(ext)==null?fileIconMap.get("default").toString():fileIconMap.get(ext).toString();
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private Labor getProject(String id) {
        return projectService.get(Labor.class, id);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getStep2")
    @ResponseBody
    private Labor getLaborStep2(String id) {
        Labor labor = projectService.get(Labor.class, id);
        List<ProjectFileItem> fileList=new ArrayList<>();
        DetachedCriteria criteria = DetachedCriteria.forClass(ProjectFileItem.class);
        criteria.add(Restrictions.eq("batchNo", labor.getContractFile()));
        criteria.addOrder(Order.asc("createDateTime"));
        fileList = projectService.findByCriteria(criteria);
        if(fileList.size() > 0){
            labor.setContractFile(fileList.get(0).getId());
        }
        return labor;
    }
    
}
