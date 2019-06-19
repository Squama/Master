package com.radish.master.controller.workmanage;

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

import com.alibaba.fastjson.JSONArray;
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
import com.radish.master.entity.BgwjFile;
import com.radish.master.entity.workmanage.Car;
import com.radish.master.entity.workmanage.Caruse;
import com.radish.master.pojo.Options;
import com.radish.master.pojo.OptionsDept;
import com.radish.master.system.FileHelper;

@Controller
@RequestMapping("/cars")
public class CarController {
	String prefix = "workmanage/cars/";
	@Autowired
	private BaseService baseService;
	
	private static Logger logger= LoggerFactory.getLogger(UploaderController.class);
	private static final String uploaderPath=PropertiesUtil.getValue("qualityChecksFilePath")+"\\cars";
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
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		return prefix +"list";
	}
	
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		String ck = request.getParameter("ck");
		request.setAttribute("ck", ck);
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix +"addIndex";
	}
	
	@RequestMapping("/useindex")
	public String useindex(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		request.setAttribute("userid", SecurityUtil.getUserId());
		return prefix +"useindex";
	}
	@RequestMapping("/useMx")
	public String useMx(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		String carid= request.getParameter("carid");
		request.setAttribute("carid", carid);
		return prefix +"indexMx";
	}
	@RequestMapping("/useindexsp")
	public String useindexsp(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id from tbl_user u  where  u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("users", JSONArray.toJSONString(ul));
		return prefix +"useindexsp";
	}
	//使用添加
	@RequestMapping("/useadd")
	public String useadd(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		request.setAttribute("userid", SecurityUtil.getUserId());
		String ck = request.getParameter("ck");
		request.setAttribute("ck", ck);
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		//获取部门信息
		//List bm = baseService.findMapBySql("select id value, name data,pname pdata from v_depts", new Object[]{}, new Type[]{StringType.INSTANCE}, OptionsDept.class);
		//request.setAttribute("bms", JSONArray.toJSONString(bm));
		List<Car> cls = baseService.find(" from Car where isvalid='1'");//有效车辆
		for(Car cl:cls){
			cl.setDescs("");
		}
		request.setAttribute("cls", JSONArray.toJSONString(cls));
		return prefix +"useaddIndex";
	}
	@RequestMapping("/useauidt")
	public String useauidt(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		request.setAttribute("userid", SecurityUtil.getUserId());
		String ck = request.getParameter("ck");
		request.setAttribute("ck", ck);
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		//获取部门信息
		//List bm = baseService.findMapBySql("select id value, name data,pname pdata from v_depts", new Object[]{}, new Type[]{StringType.INSTANCE}, OptionsDept.class);
		//request.setAttribute("bms", JSONArray.toJSONString(bm));
		List<Car> cls = baseService.find(" from Car where isvalid='1'");//有效车辆
		for(Car cl:cls){
			cl.setDescs("");
		}
		request.setAttribute("cls", JSONArray.toJSONString(cls));
		return prefix +"useAuidt";
	}
	@RequestMapping("/save")
	@ResponseBody
	public Result save(Car cl,HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		String fileId = request.getParameter("fileId");
		User u = SecurityUtil.getUser();
		if(id==null){//保存
			cl.setIsvalid("1");
			cl.setStatus("10");
			baseService.save(cl);
			id = cl.getId();
		}else{
			Car c = baseService.get(Car.class, id);
			c.setBxtime(cl.getBxtime());
			c.setClxh(cl.getClxh());
			c.setDescs(cl.getDescs());
			c.setDriver(cl.getDriver());
			c.setNjtime(cl.getNjtime());
			c.setNumber(cl.getNumber());
			c.setQcwqtime(cl.getQcwqtime());
			c.setYsznstime(cl.getYsznstime());
			baseService.update(c);
		}
		r.setCode(id);
		
		if(fileId!=null&&fileId.length()>0){
			if(fileId.indexOf(",")<0){//单张
				BgwjFile wj = baseService.get(BgwjFile.class, fileId);
				wj.setFormId(id);
				baseService.update(wj);
			}else{//多文件
				String[] ids = fileId.split(",");
				for(int i =0;i<ids.length;i++){
					BgwjFile wj = baseService.get(BgwjFile.class, ids[i]);
					wj.setFormId(id);
					baseService.update(wj);
				}
			}
		}
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		List<BgwjFile> wjs = baseService.findMapBySql("select id  from tbl_officefile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, BgwjFile.class);
        String wjid = "";
		for(int i =0;i<wjs.size();i++){
        	if(i==wjs.size()-1){
        		wjid += wjs.get(i).getId();
        	}else {
        		wjid += wjs.get(i).getId()+",";
        	}
        }
		
		Car c = baseService.get(Car.class, id);
		r.setData(c);
		 r.setCode(wjid);
		return r;
		
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Car c = baseService.get(Car.class, id);
		c.setIsvalid("0");
		baseService.update(c);
		return r;
	}
	
	@RequestMapping("/saveuse")
	@ResponseBody
	public Result saveuse(Caruse cl,HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		String deptid = "";
		String dept = "";
		if(u.getDeptId()!=null){
			Org bm = baseService.get(Org.class, u.getDeptId());
			if(bm!=null){
				deptid=bm.getId();
				dept=bm.getName();
			}
		}
		Car cls = baseService.get(Car.class, cl.getCarid());//所选车辆信息
		if(id==null){//保存
			cl.setDriverid(u.getId());
			cl.setDriver(u.getName());
			cl.setNumber(cls.getNumber());
			cl.setStatus("10");
			cl.setDeptid(deptid);
			cl.setDeptname(dept);
			baseService.save(cl);
			id = cl.getId();
		}else{
			Caruse c = baseService.get(Caruse.class, id);
			c.setNumber(cls.getNumber());
			c.setCarid(cl.getCarid());
			c.setCchours(cl.getCchours());
			c.setCclcs(cl.getCclcs());
			c.setCcyy(cl.getCcyy());
			c.setDescs(cl.getDescs());
			c.setMdd(cl.getMdd());
			c.setCctime(cl.getCctime());
			c.setNumber(cls.getNumber());
			c.setSxry(cl.getSxry());
			cl.setDeptid(deptid);
			cl.setDeptname(dept);
			baseService.update(c);
		}
		r.setCode(id);
		return r;
	}
	@RequestMapping("/loaduse")
	@ResponseBody
	public Result loaduse(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Caruse c = baseService.get(Caruse.class, id);
		r.setData(c);
		return r;
	}
	@RequestMapping("/usedelete")
	@ResponseBody
	public Result usedelete(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Caruse c = baseService.get(Caruse.class, id);
		baseService.delete(c);
		return r;
	}
	@RequestMapping("/dosubmit")
	@ResponseBody
	public Result dosubmit(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Caruse c = baseService.get(Caruse.class, id);
		c.setStatus("20");
		baseService.update(c);
		return r;
	}
	@RequestMapping("/bmsubmit")
	@ResponseBody
	public Result bmsubmit(HttpServletRequest request,Caruse cl){
		Result r = new Result();
		String id = request.getParameter("id");
		String lx = request.getParameter("lx");
		Caruse c = baseService.get(Caruse.class, id);
		if("10".equals(lx)){//同意
			c.setStatus("30");
			c.setShr(SecurityUtil.getUser().getName());
		}else if("20".equals(lx)){//不同意
			c.setStatus("40");
			c.setShr(SecurityUtil.getUser().getName());
			c.setBhyy(cl.getBhyy());
		}
		baseService.update(c);
		return r;
	}
	
	@RequestMapping(value="/projectdetailfile", method = RequestMethod.GET)
    public String projectdetailfile(String id, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<BgwjFile> wjs = baseService.findMapBySql("select id  from tbl_officefile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, BgwjFile.class);
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
    	
        FileResult msg = new FileResult();
        String id = request.getParameter("id");
        ArrayList<Integer> arr = new ArrayList<>();
        //缓存当前的文件
        List<BgwjFile> fileList=new ArrayList<>();
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
                    BgwjFile sysFile=new BgwjFile();
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
    public FileResult getPreivewSettings(List<BgwjFile> fileList,HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (BgwjFile sysFile : fileList) {
            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
        	String dirPath = request.getContextPath() + "/cars/showImage?imageID=" + sysFile.getId();
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
    	BgwjFile sysFile=baseService.get(BgwjFile.class,id);
        String dirPath=request.getRealPath("/");
        FileUtil.delFile(uploaderPath+File.separator+sysFile.getSavedName());
        baseService.delete(sysFile);
        return new Result();
    }
    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
    public void downloadFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
    	BgwjFile sysfile = baseService.get(BgwjFile.class, id);

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
        List<BgwjFile> fileList=new ArrayList<>();
        if(!StrUtil.isEmpty(fileIds)) {
            String[] fileIdArr = fileIds.split(",");
            DetachedCriteria criteria = DetachedCriteria.forClass(BgwjFile.class);
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
    public FileResult getPreivewSettingsPreview(List<BgwjFile> fileList,HttpServletRequest request){
        FileResult fileResult=new FileResult();
        List<String> previews=new ArrayList<>();
        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
        //缓存当前的文件
        //String dirPath = request.getRealPath("/");
        String[] fileArr=new String[fileList.size()];
        int index=0;
        for (BgwjFile sysFile : fileList) {
            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
        	String dirPath = request.getContextPath() + "/cars/showImage?imageID=" + sysFile.getId();
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
        
    	BgwjFile item = baseService.get(BgwjFile.class, imageID);

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
	
}
