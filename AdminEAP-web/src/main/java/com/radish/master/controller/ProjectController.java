/**
 * Copyright © 2017 周庆博和他的朋友们有限公司
 */
package com.radish.master.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
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
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
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
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.entity.project.Notice;
import com.radish.master.entity.project.ProjectSub;
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.pojo.ProjectDetailVO;
import com.radish.master.service.ProjectService;
import com.radish.master.system.GUID;
import com.radish.master.system.TimeUtil;

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
    
    @RequestMapping(value="/query",method = RequestMethod.GET)
    public String query(){
        return "projectmanage/project/project_query_list";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ProjectDetailVO detail(@PathVariable("id") String id){
        Project project = this.getProject(id);
        ProjectDetailVO pdv = new ProjectDetailVO();
        
        if(project != null){
            pdv.setProjectCode(project.getProjectCode());
            pdv.setProjectName(project.getProjectName());
            pdv.setProjectManager(projectService.getManagerName(project.getProjectManager()).getData().toString());
            pdv.setSaftyFileField(projectService.getFileField(project.getSaftyFile()));
            pdv.setConstructionFileField(projectService.getFileField(project.getConstructionFile()));
            pdv.setBidsFileField(projectService.getFileField(project.getBidsFile()));
            pdv.setBidsWinFileField(projectService.getFileField(project.getBidsWinFile()));
            pdv.setBidsWinNoticeFileField(projectService.getFileField(project.getBidsWinNoticeFile()));
            pdv.setQualityFileField(projectService.getFileField(project.getQualityFile()));
            pdv.setCostFileField(projectService.getFileField(project.getCostFile()));
            pdv.setScheduleFileField(projectService.getFileField(project.getScheduleFile()));
        }
        
        return pdv;
    }
    
    @RequestMapping(value="/projectdetailfile", method = RequestMethod.GET)
    public String projectdetailfile(String fields, HttpServletRequest request){
        request.setAttribute("fields", fields);
        return "projectmanage/project/project_query_file";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
		List<Notice> p = projectService.findMapBySql(
				"select p.number number,p.projectName projectName from tbl_notice p where p.status='30'  and exists (select 1 from tbl_notice ps where ps.pid = p.id and ps.type='3')", 
				new Object[]{}, new Type[]{StringType.INSTANCE}, Notice.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
        return "projectmanage/project/project_add";
    }
	
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/project/project_edit";
    }
    
    @RequestMapping(value = "/show/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result show(@PathVariable("id") String id) {
        return projectService.getManagerName(id);
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id,HttpServletRequest request) {
        request.setAttribute("id", id);
        return "projectmanage/project/project_sub_detail";
    }
    
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Project project, HttpServletRequest request){
        
        
        if(StrUtil.isEmpty(project.getId())){
            project.setStatus("10");
            project.setUpdateDateTime(new Date());
            projectService.save(project);
            ProjectTeam pt = new ProjectTeam();
            pt.setProjectID(project.getId());
            pt.setProjectName(project.getProjectName());
            pt.setTeamCode("guanlirenyuanbanzi" + TimeUtil.getTimeStamp());
            pt.setTeamName("管理人员班子");
            pt.setStatus("30");
            projectService.save(pt);
        }else{
            Project oldProject = this.getProject(project.getId());
            oldProject.setProjectCode(project.getProjectCode());
            oldProject.setProjectName(project.getProjectName());
            oldProject.setProjectManager(project.getProjectManager());
            oldProject.setUpdateDateTime(new Date());
            oldProject.setJzmj(project.getJzmj());
            oldProject.setJzjg(project.getJzjg());
            oldProject.setCs(project.getCs());
            oldProject.setKgsj(project.getKgsj());
            oldProject.setJgsj(project.getJgsj());
            oldProject.setZzj(project.getZzj());
            oldProject.setGcdd(project.getGcdd());
            oldProject.setGczt(project.getGczt());
            projectService.update(oldProject);
        }
        
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", project.getId());
        return new Result(true, map);
    }
    
    @RequestMapping(value="/savesub")
    @ResponseBody
    public Result saveSub(ProjectSub projectSub, HttpServletRequest request){
        projectSub.setCreateDateTime(new Date());
        try {
            projectService.save(projectSub);
        } catch (Exception e) {
            return new Result(false);
        }
        
        return new Result(true);
    }
    
    @RequestMapping(value = "/deletesub/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteSub(@PathVariable("id") String id) {

        ProjectSub detail = projectService.get(ProjectSub.class, id);
        try {
            projectService.delete(detail);
        } catch (Exception e) {
            return new Result(false);
        }
        
        return new Result(true);
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/uploadfile",method = RequestMethod.POST)
    public String step2(Project project, HttpServletRequest request){
        request.setAttribute("id", project.getId());
        return "projectmanage/project/project_add_step2";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edituploadfile",method = RequestMethod.POST)
    public String editstep2(Project project, HttpServletRequest request){
        request.setAttribute("id", project.getId());
        return "projectmanage/project/project_edit_step2";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private Project getProject(String id) {
        return projectService.get(Project.class, id);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getStep2")
    @ResponseBody
    private Project getProjectStep2(String id) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Project project = projectService.get(Project.class, id);
        Field[] field = project.getClass().getDeclaredFields();
        for(int j=0 ; j<field.length ; j++){  
            String name = field[j].getName();
            if(name.toLowerCase().endsWith("file")){
                name = name.substring(0,1).toUpperCase()+name.substring(1);
                Method m = project.getClass().getMethod("get"+name);
                String value = (String) m.invoke(project);
                if(!StrUtil.isEmpty(value)){
                    List<ProjectFileItem> fileList=new ArrayList<>();
                    DetachedCriteria criteria = DetachedCriteria.forClass(ProjectFileItem.class);
                    criteria.add(Restrictions.eq("batchNo", value));
                    criteria.addOrder(Order.asc("createDateTime"));
                    fileList = projectService.findByCriteria(criteria);
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<fileList.size();i++){
                        sb.append(",");
                        sb.append(fileList.get(i).getId());
                    }
                    if(!fileList.isEmpty()){
                        value = sb.toString().substring(1);
                        Method s = project.getClass().getMethod("set"+name,new Class[]{String.class});
                        s.invoke(project, value);
                    }
                }
            }
        }
        return project;
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
        
        String methodStrGet = "get"+fileField.toUpperCase().substring(0, 1)+fileField.substring(1);
        Method methodGet = Project.class.getMethod(methodStrGet);
        String batchNo = (String) methodGet.invoke(project);
        if(StrUtil.isEmpty(batchNo)){
        	batchNo = GUID.getTxNo();
            String methodStr = "set"+fileField.toUpperCase().substring(0, 1)+fileField.substring(1);
            Method method = Project.class.getMethod(methodStr,new Class[]{String.class});
            method.invoke(project, batchNo);
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
                    item.setBatchNo(batchNo);
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
        projectService.update(project);
        FileResult preview=getPreivewSettings(fileList, id, fileField, request);
        msg.setInitialPreview(preview.getInitialPreview());
        msg.setInitialPreviewConfig(preview.getInitialPreviewConfig());
        msg.setFileIds(preview.getFileIds());
        return msg;
    }
    
    @RequestMapping(value="/getFieldIds",method = RequestMethod.POST)
    @ResponseBody
    public Result getFileIds(String batchId, HttpServletRequest request){
        List<ProjectFileItem> fileList=new ArrayList<>();
        DetachedCriteria criteria = DetachedCriteria.forClass(ProjectFileItem.class);
        criteria.add(Restrictions.eq("batchNo", batchId));
        criteria.addOrder(Order.asc("createDateTime"));
        fileList = projectService.findByCriteria(criteria);
        String strings[]=new String[fileList.size()];
        for(int i=0;i<fileList.size();i++){
            strings[i]=fileList.get(i).getId();
        }
        return new Result(true,strings,"获取成功");
    }
    
    /**
     * 根据附件IDS 获取文件
     * @param fileIds
     * @param request
     * @return
     */
    @RequestMapping(value="/getFiles",method = RequestMethod.POST)
    @ResponseBody
    public FileResult getFiles(String fileIds, String type, String projectID, String fileField, HttpServletRequest request){
        List<ProjectFileItem> fileList=new ArrayList<>();
        if(!StrUtil.isEmpty(fileIds)) {
            String[] fileIdArr = fileIds.split(",");
            DetachedCriteria criteria = DetachedCriteria.forClass(ProjectFileItem.class);
            criteria.add(Restrictions.in("id", fileIdArr));
            criteria.addOrder(Order.asc("createDateTime"));
            fileList = projectService.findByCriteria(criteria);
        }
        if(StrUtil.isEmpty(type)){
            return getPreivewSettings(fileList,projectID,fileField,request);
        }else{
            return getPreivewSettingsPreview(fileList,projectID,fileField,request);
        }
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
                /*previews.add("<a href='http://ow365.cn/?i=16723&furl=http://www.qhjr0971.com:6535/admineap/resources/common/test2.xls' target='view_window' id='123'></a> " +
                        "<script type='text/javascript'> " +
                        "   $('#123').click(); " +
                        "   $('#kvFileinputModal').remove(); " +
                        "</script> ");*/
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
    
    @RequestMapping(value="/deletefile",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(String projectID, String fileField, String key, HttpServletRequest request){
        return projectService.deleteFileItem(projectID, fileField, key);
    }
    
    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
    public void downloadFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        if(id.contains(".")){
            id = id.split(".")[0];
        }
        ProjectFileItem item = projectService.get(ProjectFileItem.class, id);

        InputStream is = null;
        OutputStream os = null;
        File file = null;
        try {
            // PrintWriter out = response.getWriter();
            if (item != null)
                file = new File(item.getFilePath());
            if (file != null && file.exists() && file.isFile()) {
                long filelength = file.length();
                is = new FileInputStream(file);
                // 设置输出的格式
                os = response.getOutputStream();
                response.setContentType("application/x-msdownload");
                response.setContentLength((int) filelength);
                response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(item.getSourceName().getBytes("GBK"),// 只有GBK才可以
                        "iso8859-1") + "\"");

                // 循环取出流中的数据
                byte[] b = new byte[4096];
                int len;
                while ((len = is.read(b)) > 0) {
                    os.write(b, 0, len);
                }
            } else {
                response.getWriter().println("<script>");
                response.getWriter().println(" modals.info('文件不存在!');");
                response.getWriter().println("</script>");
            }

        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
}
