package com.radish.master.controller.qualityCheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.controller.UploaderController;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.qualityCheck.CheckFile;
import com.radish.master.entity.qualityCheck.CheckRecord;
import com.radish.master.entity.qualityCheck.LettersLook;
import com.radish.master.entity.qualityCheck.ProEditLetters;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.service.BudgetService;

@Controller
@RequestMapping("/editletters")
public class ProEditLettersController {
	private String prefix="QualityChecks/proEditLetters/";
	
	@Autowired
	private BaseService baseService;
	@Resource
    private BudgetService budgetService;
	
	public  String maxNum(){ 
		List<String> result = baseService.find("select max(mat.id) from com.radish.master.entity.review.MaxNumber mat");
		if(result.size()>0&&!"".equals(result.get(0))&&StringHelper.isNotEmpty(result.get(0))){
			String num = result.get(0);
			
			MaxNumber m = new MaxNumber();
			Integer newId = Integer.valueOf(num)+1;
			m.setId(newId+"");
			baseService.save(m);
			return num;
		}else{
			MaxNumber m = new MaxNumber();
			m.setId("1001");
			baseService.save(m);
			return "1001";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/addIndex")
	public String listIndex(HttpServletRequest request) {
    	request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
    	
    	String str =maxNum();
		
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		String strs = year+str;
		
		request.setAttribute("bh",strs);
        return prefix+"addIndex";
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public String list(HttpServletRequest request) {
    	request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
    	
        return prefix+"list_index";
    }
	@RequestMapping(method = RequestMethod.GET, value = "/lookList")
	public String lookList(HttpServletRequest request) {
    	request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
    	
        return prefix+"list_look";
    }
	
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,ProEditLetters hj){
		String fileId = request.getParameter("fileId");
		User u = SecurityUtil.getUser();
		Result r = new Result();
		hj.setCreate_time(new Date());
		hj.setCreate_name_ID(u.getId());
		hj.setCreate_name(u.getName());
		baseService.save(hj);
		if(fileId==null){
			r.setSuccess(true);
			return r;
		}
		if(fileId.indexOf(",")<0){
			CheckFile wj = baseService.get(CheckFile.class, fileId);
			wj.setFormId(hj.getId());
			baseService.update(wj);
		}else{
			String[] fid = fileId.split(",");
			for(int i=0;i<fid.length;i++){
				CheckFile wj = baseService.get(CheckFile.class, fid[i]);
				wj.setFormId(hj.getId());
				baseService.update(wj);
			}
		}
		return r;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteWj(String id,HttpServletRequest request) {
    	Result r = new Result();
    	
    	ProEditLetters cr = baseService.get(ProEditLetters.class, id);
    	
    	List<CheckFile> wjs = baseService.findMapBySql("select id ,savedName from tbl_checkfile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CheckFile.class);
    	for(CheckFile fj : wjs){
            String dirPath=request.getRealPath("/");
            FileUtil.delFile(uploaderPath+File.separator+fj.getSavedName());
            baseService.delete(fj);
    	}
    	baseService.delete(cr);
    	r.setSuccess(true);
    	
    	return r;
    }
	@RequestMapping("/getLooker")
    @ResponseBody
    public Result getLooker(HttpServletRequest request,String id){
		List<LettersLook> cks = baseService.findMapBySql("select *  from tbl_proEditLettersLook where lettersid='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, LettersLook.class);
		Result r = new Result();
		if(cks.size()==0){
			r.setCode("暂无人员查看");
			return r;
		}
		String str = "查阅人员：";
		for(LettersLook ck :cks){
			str +=ck.getUsername()+"("+ck.getLookTime()+"),";
			
		}
		r.setCode(str);
		return r;
	}
	
	
	private static Logger logger= LoggerFactory.getLogger(UploaderController.class);
	private static final String uploaderPath=PropertiesUtil.getValue("qualityChecksFilePath")+"\\letters";
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
	
		
		@RequestMapping(value="/projectdetailfile", method = RequestMethod.GET)
	    public String projectdetailfile(String id, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			List<CheckFile> wjs = baseService.findMapBySql("select id  from tbl_checkfile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CheckFile.class);
	        String wjid = "";
			for(int i =0;i<wjs.size();i++){
	        	if(i==wjs.size()-1){
	        		wjid += wjs.get(i).getId();
	        	}else {
	        		wjid += wjs.get(i).getId()+",";
	        	}
	        }
			
	        request.setAttribute("fields", wjid);
	        request.setAttribute("id", id);
	        return prefix+"query_file";
	    }
		
		@RequestMapping(value="/projectdetailfilelook", method = RequestMethod.GET)
	    public String projectdetailfilelook(String id, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			List<CheckFile> wjs = baseService.findMapBySql("select id  from tbl_checkfile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CheckFile.class);
	        String wjid = "";
			for(int i =0;i<wjs.size();i++){
	        	if(i==wjs.size()-1){
	        		wjid += wjs.get(i).getId();
	        	}else {
	        		wjid += wjs.get(i).getId()+",";
	        	}
	        }
			
	        request.setAttribute("fields", wjid);
	        request.setAttribute("id", id);
	        User u = SecurityUtil.getUser();
	        List<LettersLook> cks = baseService.findMapBySql("select *  from tbl_proEditLettersLook where lettersid='"+id+"' and userid='"+u.getId()+"' ", new Object[]{}, new Type[]{StringType.INSTANCE}, LettersLook.class);
	        if(cks.size()==0){
	        	LettersLook ck = new LettersLook();
	        	ck.setUserid(u.getId());
	        	ck.setUsername(u.getName());
	        	ck.setLettersid(id);
	        	ck.setLookTime(new Date());
	        	baseService.save(ck);
	        }else{
	        	LettersLook ck = cks.get(0);
	        	ck.setLookTime(new Date());
	        	baseService.update(ck);
	        }
	        
	        return prefix+"query_filelook";
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
	        String id = request.getParameter("id");
	        ArrayList<Integer> arr = new ArrayList<>();
	        //缓存当前的文件
	        List<CheckFile> fileList=new ArrayList<>();
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
	                    CheckFile sysFile=new CheckFile();
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
	    public FileResult getPreivewSettings(List<CheckFile> fileList,HttpServletRequest request){
	        FileResult fileResult=new FileResult();
	        List<String> previews=new ArrayList<>();
	        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
	        //缓存当前的文件
	        String dirPath = request.getRealPath("/");
	        String[] fileArr=new String[fileList.size()];
	        int index=0;
	        for (CheckFile sysFile : fileList) {
	            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
	            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
	            if(FileUtil.isImage(uploaderPath+File.separator+sysFile.getSavedName())) {
	                previews.add("<img src='." + sysFile.getFilePath().replace(File.separator, "/") + "' class='file-preview-image kv-preview-data' " +
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
	    public Result delete(String id, HttpServletRequest request){
	    	CheckFile sysFile=baseService.get(CheckFile.class,id);
	        String dirPath=request.getRealPath("/");
	        FileUtil.delFile(uploaderPath+File.separator+sysFile.getSavedName());
	        baseService.delete(sysFile);
	        return new Result();
	    }
	    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
	    public void downloadFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
	    	CheckFile sysfile = baseService.get(CheckFile.class, id);

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
	        List<CheckFile> fileList=new ArrayList<>();
	        if(!StrUtil.isEmpty(fileIds)) {
	            String[] fileIdArr = fileIds.split(",");
	            DetachedCriteria criteria = DetachedCriteria.forClass(CheckFile.class);
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
	    public FileResult getPreivewSettingsPreview(List<CheckFile> fileList,HttpServletRequest request){
	        FileResult fileResult=new FileResult();
	        List<String> previews=new ArrayList<>();
	        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
	        //缓存当前的文件
	        String dirPath = request.getRealPath("/");
	        String[] fileArr=new String[fileList.size()];
	        int index=0;
	        for (CheckFile sysFile : fileList) {
	            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
	            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
	            if(FileUtil.isImage(uploaderPath+File.separator+sysFile.getSavedName())) {
	                previews.add("<img src='." + sysFile.getFilePath().replace(File.separator, "/") + "' class='file-preview-image kv-preview-data' " +
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
}
