package com.radish.master.controller.files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.cnpc.framework.base.controller.UploaderController;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.files.Writings;
import com.radish.master.entity.files.WritingsDept;
import com.radish.master.entity.files.WritingsFile;
import com.radish.master.entity.files.WritingsLook;
import com.radish.master.system.FileHelper;

@Controller
@RequestMapping("/writings")
public class WritingsController {
private String prefix ="workmanage/writings/";
	
	@Autowired
	private BaseService baseService;
	
	private static Logger logger= LoggerFactory.getLogger(UploaderController.class);
	private static final String uploaderPath=PropertiesUtil.getValue("qualityChecksFilePath")+"\\writings";
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
	 
	 @RequestMapping("/list")
	public String gsList(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx",lx);
		String useid = SecurityUtil.getUserId();
		request.setAttribute("userid", useid);
		return prefix +"list";
	}
	 
	 @RequestMapping("/add")
		public String add(HttpServletRequest request){
			String ck = request.getParameter("ck");
			String id=request.getParameter("id");
			/*if(id==null){
				String str =maxNum();
				String strs = "HT"+str;
				request.setAttribute("bh",strs);
				
			}*/
			
			request.setAttribute("ck",ck);
			request.setAttribute("id",id);
			
			return prefix +"addIndex";
		}
	 	
	 @RequestMapping("/save")
		@ResponseBody
		public Result save(HttpServletRequest request,Writings jd){
			Result r = new Result();
			String id = request.getParameter("id");
			String fileId = request.getParameter("fileId");
			User u = SecurityUtil.getUser();
			if(id==null){//保存
				jd.setCreate_time(new Date());
				jd.setCreate_name_ID(u.getId());
				jd.setCreate_name(u.getName());
				jd.setIssend("0");
				jd.setIsvalid("1");
				baseService.save(jd);
				r.setCode(jd.getId());
				id = jd.getId();
			}else{
				Writings j = baseService.get(Writings.class, id);
				j.setName(jd.getName());
				j.setNumber(jd.getNumber());
				j.setDescs(jd.getDescs());
				baseService.update(j);
				r.setCode(id);
			}
			if(fileId!=null&&fileId.length()>0){
				if(fileId.indexOf(",")<0){//单张
					WritingsFile wj = baseService.get(WritingsFile.class, fileId);
					wj.setFormId(id);
					baseService.update(wj);
				}else{//多文件
					String[] ids = fileId.split(",");
					for(int i =0;i<ids.length;i++){
						WritingsFile wj = baseService.get(WritingsFile.class, ids[i]);
						wj.setFormId(id);
						baseService.update(wj);
					}
				}
			}
			return r;
		}
	 
	   @RequestMapping("/load")
		@ResponseBody
		public Result load(String id){
			List<WritingsFile> wjs = baseService.findMapBySql("select id  from tbl_writtingsfile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, WritingsFile.class);
	        String wjid = "";
			for(int i =0;i<wjs.size();i++){
	        	if(i==wjs.size()-1){
	        		wjid += wjs.get(i).getId();
	        	}else {
	        		wjid += wjs.get(i).getId()+",";
	        	}
	        }
			Writings  jd= baseService.get(Writings.class, id);
			 Result r = new Result();
			 r.setData(jd);
			 r.setCode(wjid);
			return r;
		}
	   @RequestMapping("/delete")
		@ResponseBody
		public Result delete(String id,HttpServletRequest request){
			Result r= new Result();
			Writings jd =  baseService.get(Writings.class, id);
			List<WritingsFile> wjs = baseService.find(" from WritingsFile where form_ID='"+id+"'");
			for(WritingsFile tp:wjs){
				String dirPath=request.getRealPath("/");
		        FileUtil.delFile(uploaderPath+File.separator+tp.getSavedName());
				baseService.delete(tp);
			}
			
			baseService.delete(jd);
			return r;
		}
	 	
	 	@RequestMapping(value="/projectdetailfile", method = RequestMethod.GET)
	    public String projectdetailfile(String id, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			List<WritingsFile> wjs = baseService.findMapBySql("select id  from tbl_writtingsfile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, WritingsFile.class);
	        String wjid = "";
			for(int i =0;i<wjs.size();i++){
	        	if(i==wjs.size()-1){
	        		wjid += wjs.get(i).getId();
	        	}else {
	        		wjid += wjs.get(i).getId()+",";
	        	}
	        }
			request.setAttribute("lx", request.getParameter("lx"));
	        request.setAttribute("fields", wjid);
	        request.setAttribute("id", id);
	        return prefix+"query_file";
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
	    	
	    	String type = request.getParameter("type");
	        FileResult msg = new FileResult();
	        String id = request.getParameter("id");
	        ArrayList<Integer> arr = new ArrayList<>();
	        //缓存当前的文件
	        List<WritingsFile> fileList=new ArrayList<>();
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
	                    WritingsFile sysFile=new WritingsFile();
	                    sysFile.setFileName(file.getOriginalFilename());
	                    sysFile.setSavedName(savedName);
	                    sysFile.setCreateDateTime(new Date());
	                    sysFile.setUpdateDateTime(new Date());
	                    sysFile.setCreateUserId(SecurityUtil.getUserId());
	                    sysFile.setDeleted(0);
	                    sysFile.setFileSize(file.getSize());
	                    sysFile.setFilePath(uploaderPath+File.separator+savedName);
	                    sysFile.setFormId(id);
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
	    public FileResult getPreivewSettings(List<WritingsFile> fileList,HttpServletRequest request){
	        FileResult fileResult=new FileResult();
	        List<String> previews=new ArrayList<>();
	        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
	        //缓存当前的文件
	        String[] fileArr=new String[fileList.size()];
	        int index=0;
	        for (WritingsFile sysFile : fileList) {
	            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
	            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
	        	String dirPath = request.getContextPath() + "/writings/showImage?imageID=" + sysFile.getId();
	            if(FileUtil.isImage(uploaderPath+File.separator+sysFile.getSavedName())) {
	                previews.add("<img src='" + dirPath+ "' class='file-preview-image kv-preview-data' " +
	                        "style='width:auto;height:160px' alt='" + sysFile.getFileName() + " title='" + sysFile.getFileName() + "''>");
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
	    public Result deletefile(String id, HttpServletRequest request){
	    	WritingsFile sysFile=baseService.get(WritingsFile.class,id);
	        String dirPath=request.getRealPath("/");
	        FileUtil.delFile(uploaderPath+File.separator+sysFile.getSavedName());
	        baseService.delete(sysFile);
	        return new Result();
	    }
	    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
	    public void downloadFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
	    	WritingsFile sysfile = baseService.get(WritingsFile.class, id);

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
	        List<WritingsFile> fileList=new ArrayList<>();
	        if(!StrUtil.isEmpty(fileIds)) {
	            String[] fileIdArr = fileIds.split(",");
	            DetachedCriteria criteria = DetachedCriteria.forClass(WritingsFile.class);
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
	    public FileResult getPreivewSettingsPreview(List<WritingsFile> fileList,HttpServletRequest request){
	        FileResult fileResult=new FileResult();
	        List<String> previews=new ArrayList<>();
	        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
	        //缓存当前的文件
	        //String dirPath = request.getRealPath("/");
	        String[] fileArr=new String[fileList.size()];
	        int index=0;
	        for (WritingsFile sysFile : fileList) {
	            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
	            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
	        	String dirPath = request.getContextPath() + "/writings/showImage?imageID=" + sysFile.getId();
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
	    @RequestMapping(value="/showImage",method = RequestMethod.GET)
	    public String showImage(String imageID, HttpServletResponse response) throws IOException{
	        
	    	WritingsFile item = baseService.get(WritingsFile.class, imageID);

	        byte[] fileBytes = getImage(item.getFilePath(), item.getFileName());
	        
	                
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
	    public byte[] getImage(String path, String name) {
	        try {

	            // 调接口写读文件
	            return FileHelper.showImageFile(name, path);
	        } catch (Exception e) {
	            logger.error("", e);
	        }

	        return new byte[0];
	    }
	    /**
	     * 选择发送弹框
	     */
	    @RequestMapping("/dosendPage")
		public String dosendPage(HttpServletRequest request){
			String id=request.getParameter("id");
			
			request.setAttribute("id",id);
			
			return prefix +"dosendPage";
		}
	    /**
	     * 查看已读人员列表
	     */
	    @RequestMapping("/lookers")
		public String lookers(HttpServletRequest request){
			String id=request.getParameter("id");
			
			request.setAttribute("id",id);
			
			return prefix +"lookers";
		}
	   /**
	    * 发送按钮
	    */
	    @RequestMapping("/dosend")
	    @ResponseBody
	    public Result dosend(HttpServletRequest request){
	    	Result r = new Result();
	    	String fjid = request.getParameter("fjid");
	    	Writings wj = baseService.get(Writings.class, fjid);
	    	
	    	String bmids = request.getParameter("bmids");
	    	if(bmids.indexOf(",")<0){//单选
	    		Org bm = baseService.get(Org.class, bmids);
	    		WritingsDept jsbm = new WritingsDept();
	    		jsbm.setDeptid(bmids);
	    		jsbm.setWritingid(fjid);
	    		baseService.save(jsbm);
	    		List<User> us = baseService.find(" from User where deptId='"+bmids+"'");
	    		for(User u :us){
	    			WritingsLook l = new WritingsLook();
	    			l.setIslook("0");
	    			l.setLookid(u.getId());
	    			l.setLookname(u.getName());
	    			l.setDeptName(bm.getName());
	    			l.setCreateDate(new Date());
	    			l.setWritingid(fjid);
	    			baseService.save(l);
	    		}
	    		
	    	}else{//多选
	    		String[] ids = bmids.split(",");
	    		for(int i =0;i<ids.length;i++){
	    			String bmid = ids[i];
	    			Org bm = baseService.get(Org.class, bmid);
		    		WritingsDept jsbm = new WritingsDept();
		    		jsbm.setDeptid(bmid);
		    		jsbm.setWritingid(fjid);
		    		baseService.save(jsbm);
		    		List<User> us = baseService.find(" from User where deptId='"+bmid+"'");
		    		for(User u :us){
		    			WritingsLook l = new WritingsLook();
		    			l.setIslook("0");
		    			l.setLookid(u.getId());
		    			l.setLookname(u.getName());
		    			l.setDeptName(bm.getName());
		    			l.setCreateDate(new Date());
		    			l.setWritingid(fjid);
		    			baseService.save(l);
		    		}
	    		}
	    	}
	    	wj.setIssend("1");
	    	baseService.update(wj);
	    	r.setSuccess(true);
	    	return r;
	    }
	    /**
	     * 收文查看页面
	     */
	    @RequestMapping("/swlook")
		public String swlook(HttpServletRequest request){
	    	String useid = SecurityUtil.getUserId();
	    	request.setAttribute("userid", useid);
			return prefix +"swlook";
		}
	    @RequestMapping("/addlook")
		public String addlook(HttpServletRequest request){
			String ck = request.getParameter("ck");
			String id=request.getParameter("id");
			
			request.setAttribute("ck",ck);
			request.setAttribute("id",id);
			//编辑查看状态
			String lookid = request.getParameter("lookid");
			WritingsLook l = baseService.get(WritingsLook.class, lookid);
			l.setIslook("1");
			l.setCreateDate(new Date());
			baseService.update(l);
			
			return prefix +"addIndex";
		}
}
