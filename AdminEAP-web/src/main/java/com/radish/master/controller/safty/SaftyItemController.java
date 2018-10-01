package com.radish.master.controller.safty;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.controller.UploaderController;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.safty.CheckFileAQ;
import com.radish.master.entity.safty.CheckItemAQ;
import com.radish.master.entity.safty.CheckRecordAQ;
import com.radish.master.entity.safty.CheckRecordMbAQ;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.service.BudgetService;
import com.radish.master.service.SaftyItemService;

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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * Created by billJiang on 2017/6/19.
 * e-mail:475572229@qq.com  qq:475572229
 * 组织结构管理控制器
 */
@Controller
@RequestMapping("/checkRecordAQ")
public class SaftyItemController {

    @Resource
    private SaftyItemService saftyItemService;
    @Resource
    private BudgetService budgetService;
    @Autowired
	private BaseService baseService;
    
    private static Logger logger= LoggerFactory.getLogger(UploaderController.class);
	private static final String uploaderPath=PropertiesUtil.getValue("safetyManageFilePath")+"\\checkRecord";
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
    
    
    @RequestMapping(method = RequestMethod.GET, value = "/tree")
    public String list() {

        return "safetyManage/checkRecord/check_tree";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public String listIndex(HttpServletRequest request) {
    	request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "safetyManage/checkRecord/addIndex";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/addWj")
    public String addWj(HttpServletRequest request) {
    	List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		
		String str =maxNum();
		
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		String strs = "CR"+year+str;
		
		request.setAttribute("bh",strs);
		
		String fid = request.getParameter("fid");
		request.setAttribute("fid",fid);
        return "safetyManage/checkRecord/edit";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/scWj")
    public String scWj(HttpServletRequest request) {
    	
		
		String id = request.getParameter("id");
		request.setAttribute("id",id);
        return "safetyManage/checkRecord/query_file";
    }
    
    @RequestMapping("saveWj")
    @ResponseBody
    public Result saveWj(HttpServletRequest request,CheckRecordAQ cr){
    	Result r = new Result();
    	String fid = request.getParameter("fid");
    	User u = SecurityUtil.getUser();
    	String fileids = request.getParameter("fileId");
    	
    	
    	if(cr.getId()==null){
    		cr.setParent_ID(fid);
    		cr.setCreate_time(new Date());
    		cr.setCreate_name_ID(u.getId());
    		cr.setCreate_name(u.getName());
    		baseService.save(cr);
    		r.setCode(cr.getId());
    	}else{
    		CheckRecordAQ c = baseService.get(CheckRecordAQ.class, cr.getId());
    		c.setProid(cr.getProid());
    		baseService.update(c);
    		r.setCode(cr.getId());
    	}
    	r.setSuccess(true);
    	if(fileids!=null&&fileids.length()>0){
    		if(fileids.indexOf(",")<0){//单个
    			CheckFileAQ wj = baseService.get(CheckFileAQ.class, fileids);
    			wj.setFormId(r.getCode());
    			baseService.update(wj);
    		}else{//多个
    			String[] ids = fileids.split(",");
    			for(int i = 0;i<ids.length;i++){
    				CheckFileAQ wj = baseService.get(CheckFileAQ.class, ids[i]);
    				wj.setFormId(r.getCode());
    				baseService.update(wj);
    			}
    		}
    	}
    	
    	return r;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public List<CheckItemAQ> getAll() {

        String hql = "from CheckItemAQ order by levelCode asc";
        return saftyItemService.find(hql.toString());
    }

    /**
     * getTreeData 构造bootstrap-treeview格式数据
     *
     * @return
     */
    @RequestMapping(value = "/treeData", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeData() {
        return saftyItemService.getTreeData();
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CheckItemAQ get(@PathVariable("id") String id) {
    	CheckItemAQ mat = saftyItemService.get(CheckItemAQ.class, id);
        if (!StrUtil.isEmpty(mat.getParentId())) {
        	mat.setParentName(saftyItemService.get(CheckItemAQ.class, mat.getParentId()).getName());
        } else {
        	mat.setParentName("总根类");
        }
        return mat;
    }


    @RequestMapping(value = "/show/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result show(@PathVariable("id") String id) {
        return saftyItemService.getMatNames(id);
    }

    @RequestMapping(value = "/getList/{code}", method = RequestMethod.POST)
    @ResponseBody
    public List<CheckItemAQ> getMatsByCode(String code) {
        return saftyItemService.getMatsByCode(code);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(CheckItemAQ org) {

        org.setUpdateDateTime(new Date());
        saftyItemService.saveOrUpdate(org);
        if (!StrUtil.isEmpty(org.getParentId())) {
        	CheckItemAQ parent = saftyItemService.get(CheckItemAQ.class, org.getParentId());
        	saftyItemService.deleteCacheByKey(RedisConstant.CHECK_PRE + parent.getCode());
        }
        saftyItemService.deleteCacheByKey(RedisConstant.SAFTY_PRE + "tree");
        return new Result(true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        try {
        	CheckItemAQ org = saftyItemService.get(CheckItemAQ.class, id);
            if(saftyItemService.referByMaterial(org.getId())) {
               return new Result(false,"该物料分类被文件表引用，不可删除");
            }
            saftyItemService.delete(org);
            if (!StrUtil.isEmpty(org.getParentId())) {
            	CheckItemAQ parent = saftyItemService.get(CheckItemAQ.class, org.getParentId());
            	saftyItemService.deleteCacheByKey(RedisConstant.MAT_PRE + parent.getCode());
            }
            saftyItemService.deleteCacheByKey(RedisConstant.SAFTY_PRE + "tree");
            return new Result(true);
        } catch (Exception e) {
            return new Result(false, "该物料分类被文件表引用，不可删除");
        }
    }
    @RequestMapping("/deleteWj")
    @ResponseBody
    public Result deleteWj(String id,HttpServletRequest request) {
    	Result r = new Result();
    	
    	CheckRecordAQ cr = baseService.get(CheckRecordAQ.class, id);
    	
    	List<CheckFileAQ> wjs = baseService.findMapBySql("select id ,savedName from tbl_checkfileAQ where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CheckFileAQ.class);
    	for(CheckFileAQ fj : wjs){
            String dirPath=request.getRealPath("/");
            FileUtil.delFile(uploaderPath+File.separator+fj.getSavedName());
            baseService.delete(fj);
    	}
    	baseService.delete(cr);
    	r.setSuccess(true);
    	
    	return r;
    }
    
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
    
    @RequestMapping("/fileIndex")
	public String fileIndex(HttpServletRequest request){
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		
		return "safetyManage/checkRecord/fileIndex";
	}
	
	@RequestMapping(value="/projectdetailfile", method = RequestMethod.GET)
    public String projectdetailfile(String id, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<CheckFileAQ> wjs = baseService.findMapBySql("select id  from tbl_checkfileAQ where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CheckFileAQ.class);
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
        return "safetyManage/checkRecord/query_file";
    }
    
    @RequestMapping(value="/projectdetailfileMB", method = RequestMethod.GET)
    public String projectdetailfileMB(String fid, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<CheckRecordMbAQ> mbs = baseService.find("from CheckRecordMbAQ where parent_ID='"+fid+"'");
		String wjid = "";
		for(CheckRecordMbAQ mb :mbs){
			List<CheckFileAQ> wjs = baseService.findMapBySql("select id  from tbl_checkfileAQ where form_ID='"+mb.getId()+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CheckFileAQ.class);
	        
			for(int i =0;i<wjs.size();i++){
	        	if(wjs.size()==0){
	        		wjid += wjs.get(i).getId();
	        	}else {
	        		wjid += ","+wjs.get(i).getId();
	        	}
	        }
    	}
    	
		
        request.setAttribute("fields", wjid);
        request.setAttribute("id", fid);
        request.setAttribute("lx", "look");
        return "safetyManage/checkRecord/query_file";
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
    	String psid = request.getParameter("psid");
        FileResult msg = new FileResult();

        ArrayList<Integer> arr = new ArrayList<>();
        //缓存当前的文件
        List<CheckFileAQ> fileList=new ArrayList<>();
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
                    CheckFileAQ sysFile=new CheckFileAQ();
                    sysFile.setFileName(file.getOriginalFilename());
                    sysFile.setSavedName(savedName);
                    sysFile.setCreateDateTime(new Date());
                    sysFile.setUpdateDateTime(new Date());
                    sysFile.setCreateUserId(SecurityUtil.getUserId());
                    sysFile.setDeleted(0);
                    sysFile.setFileSize(file.getSize());
                    sysFile.setFilePath(uploaderPath+File.separator+savedName);
                    if(psid!=null)sysFile.setFormId(psid);
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
    public FileResult getPreivewSettings(List<CheckFileAQ> fileList,HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        String dirPath = request.getRealPath("/");
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (CheckFileAQ sysFile : fileList) {
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
    	CheckFileAQ sysFile=baseService.get(CheckFileAQ.class,id);
        String dirPath=request.getRealPath("/");
        FileUtil.delFile(uploaderPath+File.separator+sysFile.getSavedName());
        baseService.delete(sysFile);
        return new Result();
    }
    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
    public void downloadFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
    	CheckFileAQ sysfile = baseService.get(CheckFileAQ.class, id);

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
        List<CheckFileAQ> fileList=new ArrayList<>();
        if(!StrUtil.isEmpty(fileIds)) {
            String[] fileIdArr = fileIds.split(",");
            DetachedCriteria criteria = DetachedCriteria.forClass(CheckFileAQ.class);
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
    public FileResult getPreivewSettingsPreview(List<CheckFileAQ> fileList,HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        String dirPath = request.getRealPath("/");
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (CheckFileAQ sysFile : fileList) {
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
