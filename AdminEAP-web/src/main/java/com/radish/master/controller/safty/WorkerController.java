/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.safty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.util.Streams;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cnpc.framework.base.controller.UploaderController;
import com.cnpc.framework.base.entity.Role;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserAvatar;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.pojo.AvatarResult;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.base.service.UploaderService;
import com.cnpc.framework.base.service.UserService;
import com.cnpc.framework.query.entity.QueryCondition;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.EncryptUtil;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.HtFile;
import com.radish.master.entity.PurchaseApplyAudit;
import com.radish.master.entity.WorkContract;
import com.radish.master.entity.common.JobRole;
import com.radish.master.entity.project.Worker;
import com.radish.master.entity.safty.CheckFileAQ;
import com.radish.master.entity.skillManage.PlanFile;
import com.radish.master.service.CommonService;
import com.radish.master.service.ProjectService;
import com.radish.master.system.FileHelper;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月8日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/worker")
public class WorkerController {
	@Autowired
	private BaseService baseService;
	@Resource
	private UserService userService;

	private final static String initPassword = "123456";
	
	private static Logger logger= LoggerFactory.getLogger(UploaderController.class);
	private static final String uploaderPath=PropertiesUtil.getValue("workContractPath");
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
    
    @Resource
    private CommonService commonService;
    
    @Resource
    private UploaderService uploaderService;
    
    @Resource
    private ProjectService projectService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return "safetyManage/worker/user_page_list";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    private String pageEdit(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "safetyManage/worker/user_page_edit";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/avatar")
    private String avatar(String userId, HttpServletRequest request) {
        request.setAttribute("userId", userId);
        return "safetyManage/worker/user_avatar";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/select")
    private String select(String userid, HttpServletRequest request) {
        request.setAttribute("leaderid", userid);
        return "safetyManage/worker/zy_select";
    }
    
    @RequestMapping("/avatarUpload")
    @ResponseBody
    public AvatarResult avatarUpload(String userId, HttpServletRequest httpRequest, HttpSession session) throws Exception {

        MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
        Map<String, MultipartFile> fileMap = request.getFileMap();
        String contentType = request.getContentType();
        if (contentType.indexOf("multipart/form-data") >= 0) {
            AvatarResult result = new AvatarResult();
            result.setAvatarUrls(new ArrayList());
            result.setSuccess(false);
            result.setMsg("Failure!");

            // 定义一个变量用以储存当前头像的序号
            int avatarNumber = 1;
            Worker worker = commonService.get(Worker.class, userId);
            if (worker == null) {
                worker = new Worker();
                worker.setName("new");
            }
            // 文件名
            String fileName = worker.getName() + "_" + (new Date()).getTime() + ".jpg";
            String relPath = PropertiesUtil.getValue("avatarPath");
            String dirPath = "";//request.getRealPath("/");

            String initParams = "";

            BufferedInputStream inputStream;
            BufferedOutputStream outputStream;
            for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); avatarNumber++) {
                File filePath = new File(dirPath + relPath);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                Map.Entry<String, MultipartFile> entry = it.next();
                MultipartFile mFile = entry.getValue();
                String fieldName = entry.getKey();
                Boolean isSourcePic = fieldName.equals("__source"); // 是否是原始图片域名称
                // 文件名，如果是本地或网络图片为原始文件名（不含扩展名）、如果是摄像头拍照则为 *FromWebcam
                // String name = fileItem.getName();
                // 当前头像基于原图的初始化参数（即只有上传原图时才会发送该数据），用于修改头像时保证界面的视图跟保存头像时一致，提升用户体验度。
                // 修改头像时设置默认加载的原图url为当前原图url+该参数即可，可直接附加到原图url中储存，不影响图片呈现。
                if (fieldName.equals("__initParams")) {
                    inputStream = new BufferedInputStream(mFile.getInputStream());
                    byte[] bytes = new byte[mFile.getInputStream().available()];
                    inputStream.read(bytes);
                    initParams = new String(bytes, "UTF-8");
                    inputStream.close();
                } else if (isSourcePic || fieldName.startsWith("__avatar")) {
                    String virtualPath = dirPath + relPath + "\\" + fileName;
                    if (avatarNumber > 1) {
                        fileName = avatarNumber + fileName;
                        virtualPath = dirPath + relPath + "\\" + fileName;
                    }
                    // 原始图片(file 域的名称：__source，如果客户端定义可以上传的话，可在此处理）。
                    if (isSourcePic) {
                        fileName = "source" + fileName;
                        virtualPath = dirPath + relPath + "\\" + fileName;
                        result.setSourceUrl(relPath + "/" + fileName);
                    }
                    // 头像图片(file 域的名称：__avatar1,2,3...)。
                    else {
                        result.getAvatarUrls().add(relPath + "/" + fileName);
                    }

                    inputStream = new BufferedInputStream(mFile.getInputStream());
                    outputStream = new BufferedOutputStream(new FileOutputStream(virtualPath.replace("/", "\\")));
                    Streams.copy(inputStream, outputStream, true);
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                    // 保存图片信息
                    result.setMsg(uploaderService.saveAvatar(userId, fileName, relPath + File.separator + fileName, dirPath));
                }

            }
            if (result.getSourceUrl() != null) {
                result.setSourceUrl(result.getSourceUrl() + initParams);
            }
            result.setSuccess(true);
            return result;
        }
        return null;
    }
    
    @RequestMapping(value="/showImage",method = RequestMethod.GET)
    public String showImage(String imageID, HttpServletResponse response) throws IOException{
        
        UserAvatar item = commonService.get(UserAvatar.class, imageID);

        byte[] fileBytes = projectService.getImage(item.getSrc(), item.getName());
        
                
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
    
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result saveWorker(Worker worker, HttpServletRequest request) {
    	String fileId = request.getParameter("fileId");
    	String id = worker.getId();
        if (StrUtil.isEmpty(worker.getId())) {
        	//判断身份证是否存在
        	List<Worker> wks = commonService.find(" from Worker where identification_number='"+worker.getIdentificationNumber()+"'");
        	if(wks.size()>0){
        		return new Result(false,"该身份证已存在！不可重复添加");
        	}
        	worker.setIsleader("否");
            commonService.save(worker);
            id = worker.getId();
        } else {
        	//判断身份证是否存在
        	List<Worker> wks = commonService.find(
        			" from Worker where identification_number='"+worker.getIdentificationNumber()+"' "
        					+ " and id<>'"+worker.getId()+"'");
        	if(wks.size()>0){
        		return new Result(false,"该身份证已存在！不可重复添加");
        	}
            Worker oldWorker=commonService.get(Worker.class, worker.getId());
            SpringUtil.copyPropertiesIgnoreNull(worker, oldWorker);
            oldWorker.setUpdateDateTime(new Date());
            commonService.update(oldWorker);
        }
        if(fileId!=null&&fileId.length()>0){
			if(fileId.indexOf(",")<0){//单张
				HtFile wj = commonService.get(HtFile.class, fileId);
				wj.setFormId(id);
				commonService.update(wj);
			}else{//多文件
				String[] ids = fileId.split(",");
				for(int i =0;i<ids.length;i++){
					HtFile wj = commonService.get(HtFile.class, ids[i]);
					wj.setFormId(id);
					commonService.update(wj);
				}
			}
		}
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private Result getWorker(String id) {
    	Result r = new Result();
    	Worker work = commonService.get(Worker.class, id);
    	List<HtFile> wjs = baseService.findMapBySql("select id  from tbl_usercontractfile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, HtFile.class);
    	 String wjid = "";
			for(int i =0;i<wjs.size();i++){
	        	if(i==wjs.size()-1){
	        		wjid += wjs.get(i).getId();
	        	}else {
	        		wjid += wjs.get(i).getId()+",";
	        	}
	        }
		 r.setData(work);
		 r.setCode(wjid);
    	return r;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
    @ResponseBody
    private Result deleteWorker(@PathVariable("id") String id) {

        Worker worker = commonService.get(Worker.class, id);
        try {
            commonService.delete(worker);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(true);
    }
    /**
     * 多文件上传，用于uploadAsync=false(同步多文件上传使用)
     * @param files
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    @ResponseBody
    public FileResult uploadMultipleFile(@RequestParam(value = "file", required = false) MultipartFile[] files,
                                         HttpServletRequest request, HttpServletResponse response) throws IOException {

        FileResult msg = new FileResult();

        ArrayList<Integer> arr = new ArrayList<>();
        //缓存当前的文件
        List<HtFile> fileList=new ArrayList<>();
        String dirPath = request.getRealPath("/");
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];

            if (!file.isEmpty()) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    File dir = new File(uploaderPath);
                    if (!dir.exists())
                        dir.mkdirs();
                    //这样也可以上传同名文件了
                    String filePrefixFormat="yyyyMMddHHmmssS";
                    String savedName=DateUtil.format(new Date(),filePrefixFormat)+"_"+file.getOriginalFilename();
                    String filePath=dir.getAbsolutePath() + File.separator + savedName;
                    File serverFile = new File(filePath);
                    //将文件写入到服务器
                    //FileUtil.copyInputStreamToFile(file.getInputStream(),serverFile);
                    file.transferTo(serverFile);
                    HtFile sysFile=new HtFile();
                    sysFile.setFileName(file.getOriginalFilename());
                    sysFile.setSavedName(savedName);
                    sysFile.setCreateDateTime(new Date());
                    sysFile.setUpdateDateTime(new Date());
                    sysFile.setCreateUserId(SecurityUtil.getUserId());
                    sysFile.setDeleted(0);
                    sysFile.setFileSize(file.getSize());
                    sysFile.setFilePath(uploaderPath+File.separator+savedName);
                    baseService.save(sysFile);
                    fileList.add(sysFile);
                    /*preview.add("<div class=\"file-preview-other\">\n" +
                            "<span class=\"file-other-icon\"><i class=\"fa fa-file-o text-default\"></i></span>\n" +
                            "</div>");*/

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

        if(arr.size() > 0) {
            msg.setError("文件上传失败！");
            msg.setErrorkeys(arr);
        }
        FileResult preview=getPreivewSettings(fileList,request);
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
    public FileResult getPreivewSettings(List<HtFile> fileList,HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        //String dirPath = request.getRealPath("/");
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (HtFile sysFile : fileList) {
            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
        	String dirPath = request.getContextPath() + "/workcontract/showImage?imageID=" + sysFile.getId();
            if(FileUtil.isImage(uploaderPath+File.separator+sysFile.getSavedName())) {
                previews.add("<img src='" + dirPath+ "' class='file-preview-image kv-preview-data' " +
                        "style='width:auto;height:80px' alt='" + sysFile.getFileName() + " title='" + sysFile.getFileName() + "''>");
            }else{
                previews.add("<div class='kv-preview-data file-preview-other-frame'><div class='file-preview-other'>" +
                        "<span class='file-other-icon'>"+getFileIcon(sysFile.getFileName())+"</span></div></div>");
            }
            //上传后预览配置
            FileResult.PreviewConfig previewConfig=new FileResult.PreviewConfig();
            previewConfig.setWidth("120px");
            previewConfig.setCaption(sysFile.getFileName());
            previewConfig.setKey(sysFile.getId());
            // previewConfig.setUrl(request.getContextPath()+"/file/delete");
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
    
    @RequestMapping(value="/deletefile",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(String id, HttpServletRequest request){
    	HtFile sysFile=baseService.get(HtFile.class,id);
        FileUtil.delFile(uploaderPath+File.separator+sysFile.getSavedName());
        baseService.delete(sysFile);
        return new Result();
    }
    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
    public void downloadFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
    	HtFile sysfile = baseService.get(HtFile.class, id);

        InputStream is = null;
        OutputStream os = null;
        File file = null;
        try {
            // PrintWriter out = response.getWriter();
            if (sysfile != null)
                file = new File(sysfile.getFilePath());
            if (file != null && file.exists() && file.isFile()) {
                long filelength = file.length();
                is = new FileInputStream(file);
                // 设置输出的格式
                os = response.getOutputStream();
                response.setContentType("application/x-msdownload");
                response.setContentLength((int) filelength);
                response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(sysfile.getFileName().getBytes("GBK"),// 只有GBK才可以
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
    /**
     * 根据附件IDS 获取文件
     * @param fileIds
     * @param request
     * @return
     */
    @RequestMapping(value="/getFiles",method = RequestMethod.POST)
    @ResponseBody
    public FileResult getFiles(String fileIds, String type, HttpServletRequest request){
        List<HtFile> fileList=new ArrayList<>();
        if(!StrUtil.isEmpty(fileIds)) {
            String[] fileIdArr = fileIds.split(",");
            DetachedCriteria criteria = DetachedCriteria.forClass(HtFile.class);
            criteria.add(Restrictions.in("id", fileIdArr));
            criteria.addOrder(Order.asc("createDateTime"));
            fileList = baseService.findByCriteria(criteria);
        }
        if(StrUtil.isEmpty(type)){
            return getPreivewSettings(fileList,request);
        }else{
            return getPreivewSettingsPreview(fileList,request);
        }
        
    }
    public FileResult getPreivewSettingsPreview(List<HtFile> fileList,HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        //String dirPath = request.getRealPath("/");
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (HtFile sysFile : fileList) {
            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
        	String dirPath = request.getContextPath() + "/workcontract/showImage?imageID=" + sysFile.getId();
            if(FileUtil.isImage(uploaderPath+File.separator+sysFile.getSavedName())) {
                previews.add("<img src='" + dirPath+ "' class='file-preview-image kv-preview-data' " +
                        "style='width:auto;height:80px' alt='" + sysFile.getFileName() + " title='" + sysFile.getFileName() + "''>");
            }else{
                previews.add("<div class='kv-preview-data file-preview-other-frame'><div class='file-preview-other'>" +
                        "<span class='file-other-icon'>"+getFileIcon(sysFile.getFileName())+"</span></div></div>");
            }
            //上传后预览配置
            FileResult.PreviewConfig previewConfig=new FileResult.PreviewConfig();
            previewConfig.setWidth("60px");
            previewConfig.setCaption(sysFile.getFileName());
            previewConfig.setKey(sysFile.getId());
            // previewConfig.setUrl(request.getContextPath()+"/file/delete");
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
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/setLeader/{userid}")
    @ResponseBody
    private Result setLeader(@PathVariable("userid") String userid) {
        Worker worker = commonService.get(Worker.class, userid);
        try {
            //查询是否存在身份证对应的登录账号
			List<User> users = baseService.findMapBySql("select id  from tbl_user where login_name='"+worker.getIdentificationNumber()+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			if(users.size()>0){
				return new Result(false,"已存在身份证对应登录账号，请联系管理员处理！");
			}
        	//新建账号 
			User u= new User();
			u.setName(worker.getName());
			u.setLoginName(worker.getIdentificationNumber());
			u.setIdentificationNumber(worker.getIdentificationNumber());
			u.setTelphone(worker.getMobile());
			u.setMobile(worker.getMobile());
			u.setFileId(userid);
			// 设置初始密码
            u.setPassword(EncryptUtil.getPassword(initPassword, u.getLoginName()));
            u.setZzStatus("0");
            String userId = userService.save(u).toString();
            //关联班组长角色
            List<Role> RoleList = baseService.find("from Role where name = '班组长'");
            for(Role jobRole : RoleList){
            	UserRole ur = new UserRole();
        		ur.setUser(u);
        		ur.setRoleId(jobRole.getId());
        		baseService.save(ur);
            }
        	//修改组长标志
            worker.setIsleader("是");
            worker.setLeaderid(userid);
            baseService.update(worker);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(true,"设置成功，登陆账号："+worker.getIdentificationNumber()+",登录密码：123456");
    }
    @RequestMapping(method = RequestMethod.POST, value = "/deleteLeader/{userid}/{isdelete}")
    @ResponseBody
    private Result deleteLeader(@PathVariable("userid") String userid,@PathVariable("isdelete") String isdelete) {
    	Worker worker = commonService.get(Worker.class, userid);
    	//先查询是否存在组员
    	List<Worker> zys = baseService.find("from Worker where leaderid = '"+userid+"' and id<>leaderid");
    	if(zys.size()>0&&"no".equals(isdelete)){
    		return new Result(false,null,"名下还存在"+zys.size()+"组员，是否确认删除","-2");
    	}
    	for(Worker zy :zys){
    		zy.setLeaderid(null);
    	}
    	baseService.batchUpdate(zys);
    	 //删除用户
        List<User> us = baseService.find("from User where loginName = '"+worker.getIdentificationNumber()+"'");
    	if(us.size()>0){
    		User u = us.get(0);
    		//删除user和权限
    		List<UserRole> r = baseService.findBySql("select * from tbl_user_role  where userID='" + u.getId()+ "'", UserRole.class);
            for (UserRole rol : r) {
           	 	baseService.delete(rol);
            }
    		baseService.delete(u);
    	}
       
    	//修改worker表标注
    	worker.setIsleader("否");
        worker.setLeaderid(null);
    	baseService.update(worker);
    	return new Result(true,"操作成功");
    }
    @RequestMapping(method = RequestMethod.POST, value = "/saveBd")
    @ResponseBody
    private Result saveBd(String ids,String leaderid){
    	String[] idss = ids.split(",");
    	for(String id :idss){
    		Worker worker = commonService.get(Worker.class, id);
    		worker.setLeaderid(leaderid);
    		baseService.update(worker);
    	}
    	return new Result(true,"绑定成功");
    }
    @RequestMapping(method = RequestMethod.POST, value = "/saveJb")
    @ResponseBody
    private Result saveJb(String ids){
    	String[] idss = ids.split(",");
    	for(String id :idss){
    		Worker worker = commonService.get(Worker.class, id);
    		worker.setLeaderid(null);
    		baseService.update(worker);
    	}
    	return new Result(true,"解绑成功");
    }
}
