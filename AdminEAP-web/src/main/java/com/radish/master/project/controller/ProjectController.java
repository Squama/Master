/**
 * Copyright © 2017 周庆博和他的朋友们有限公司
 */
package com.radish.master.project.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.controller.UploaderController;
import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.util.SecurityUtil;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.service.ProjectService;
import com.radish.master.system.GUID;

/**
 * @author dongy
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {
    
    private static Logger logger= LoggerFactory.getLogger(ProjectController.class);
    
    @Resource
    private ProjectService projectService;
    
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
    public String list(){
        return "projectmanage/project/project_list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "projectmanage/project/project_add";
    }
	
    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Project project, HttpServletRequest request){
        project.setStatus("10");
        
        projectService.save(project);
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", project.getId());
        return new Result(true, map);
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/uploadfile",method = RequestMethod.POST)
    public String step2(Project project, HttpServletRequest request){
        request.setAttribute("id", project.getId());
        return "projectmanage/project/project_add_step2";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private Project getProject(String id) {

        return projectService.get(Project.class, id);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getimagefile")
    @ResponseBody
    private ProjectFileItem getImageFile(String id) {

        return projectService.get(ProjectFileItem.class, id);
    }
    
    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    @ResponseBody
    public FileResult uploadMultipleFile(@RequestParam(value = "file", required = false) MultipartFile[] files, String id,String fileField,
                                         HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        FileResult msg = new FileResult();

        ArrayList<Integer> arr = new ArrayList<>();
        //缓存当前的文件
        List<ProjectFileItem> fileList=new ArrayList<>();
        
        Project project = this.getProject(id);
        String batchNo = GUID.getTxNo();
        String methodStr = "set"+fileField.toUpperCase().substring(0, 1)+fileField.substring(1);
        Method method = Project.class.getMethod(methodStr,new Class[]{String.class});
        method.invoke(project, batchNo);
        
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
                    item.setBatchNo(batchNo);
                    item.setUploadUserID(SecurityUtil.getUserId());
                    item.setFileName(fileName);
                    item.setFilePath(projectFilePath+File.separator+fileName);
                    item.setFileSize(file.getSize());
                    item.setFileType(fileName.substring(fileName.lastIndexOf(".")+1));
                    item.setSourceName(sourceName);
                    
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
        projectService.update(project);
        FileResult preview=getPreivewSettings(fileList, request);
        msg.setInitialPreview(preview.getInitialPreview());
        msg.setInitialPreviewConfig(preview.getInitialPreviewConfig());
        msg.setFileIds(preview.getFileIds());
        return msg;
    }
    
    /**
     * 根据附件IDS 获取文件
     * @param fileIds
     * @param request
     * @return
     */
    @RequestMapping(value="/getFiles",method = RequestMethod.POST)
    @ResponseBody
    public FileResult getFiles(String fileIds,HttpServletRequest request){
        List<ProjectFileItem> fileList=new ArrayList<>();
        if(!StrUtil.isEmpty(fileIds)) {
            String[] fileIdArr = fileIds.split(",");
            DetachedCriteria criteria = DetachedCriteria.forClass(ProjectFileItem.class);
            criteria.add(Restrictions.in("id", fileIdArr));
            criteria.addOrder(Order.asc("createDateTime"));
            fileList = projectService.findByCriteria(criteria);
        }
        return getPreivewSettings(fileList,request);
    }
    
    /**
     * 回填已有文件的缩略图
     * @param fileList 文件列表
     * @param request
     * @return initialPreiview initialPreviewConfig fileIds
     */
    public FileResult getPreivewSettings(List<ProjectFileItem> fileList, HttpServletRequest request){
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
            previewConfig.setExtra(new FileResult.PreviewConfig.Extra(sysFile.getId()));
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
    
    @RequestMapping(value="/showImage",method = RequestMethod.GET)
    public String showImage(String imageID, HttpServletResponse response) throws IOException{
        
        ProjectFileItem item = this.getImageFile(imageID);

        byte[] fileBytes = projectService.getImage(item.getFilePath(), item.getFileName());
        
                
        if(fileBytes != null){
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(response.getOutputStream());
                bos.write(fileBytes);
            } catch (IOException e) {
                throw e;
            } finally {
                if (bos != null)
                    bos.close();
            }
        }
        
        return null;
    }
}
