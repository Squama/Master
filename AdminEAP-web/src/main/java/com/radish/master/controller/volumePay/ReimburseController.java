package com.radish.master.controller.volumePay;

import java.io.BufferedOutputStream;
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
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
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
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.Project;
import com.radish.master.entity.files.CwwjFile;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.entity.volumePay.Reimburse;
import com.radish.master.entity.volumePay.ReimburseDet;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;
import com.radish.master.system.FileHelper;

@Controller
@RequestMapping("/reimburse")
public class ReimburseController {
	String prefix = "/VolumePay/Reimburse/";
	
	@Autowired
	private BaseService baseService;
	@Resource
    private BudgetService budgetService;
	@Resource
	private RuntimePageService runtimePageService;
	
	
	private static Logger logger= LoggerFactory.getLogger(UploaderController.class);
	private static final String uploaderPath=PropertiesUtil.getValue("qualityChecksFilePath")+"\\reimburse";
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
	
	@RequestMapping("/addindex")
	public String addindex(HttpServletRequest request){
		request.setAttribute("jkr", SecurityUtil.getUserId());
		return prefix+"index";
	}
	@RequestMapping("/bmshindex")
	public String bmshindex(HttpServletRequest request){
		User u = SecurityUtil.getUser();
		String deptid = u.getDeptId();
		request.setAttribute("deptid", deptid);
		return prefix+"bmshindex";
	}
	//财务付款页
	@RequestMapping("/cwfkindex")
	public String cwfkindex(HttpServletRequest request){
		request.setAttribute("projects", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix+"cwfkindex";
	}
	@RequestMapping("/cwlook")
	public String cwlook(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String  id = request.getParameter("id");
		String lx = request.getParameter("lx");
		request.setAttribute("id", id);
		request.setAttribute("lx", lx);
		return prefix+"cwlook";
	}
	@RequestMapping("/auidtCw/{id}")//审核查看页
	public String auidtMx(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("id", id);
		return prefix+"auidLookCw";
		
	}
	@RequestMapping("/auidtCwfzr/{id}")//审核查看页
	public String auidtCwfzr(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("id", id);
		return prefix+"auidtLookCwfzr";
		
	}
	@RequestMapping("/auidtBoss/{id}")//审核查看页
	public String auidtBoss(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("id", id);
		return prefix+"auidtLookBoss";
		
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
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		String  id = request.getParameter("id");
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		if(id==null){
			String str =maxNum();
			
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			String strs = year+str;
			
			User u = SecurityUtil.getUser();
			request.setAttribute("bh",strs);
			request.setAttribute("ryid", u.getId());
			request.setAttribute("xm", u.getName());
			if(u.getDeptId()!=null){
				Org bm = baseService.get(Org.class, u.getDeptId());
				if(bm==null){
					request.setAttribute("msg","未找到所属部门，请联系办公管理人员！");	
				}
			}else{
				request.setAttribute("msg","请联系办公管理人员，完善个人部门信息！");
			}
		}else{
			request.setAttribute("id", id);
		}
		
		return prefix+"add";
	}
	@RequestMapping("/bmlook")
	public String bmlook(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String  id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix+"bmlook";
	}
	
	@RequestMapping("/addBxmx")
	public String addBxmx(HttpServletRequest request){
		String bxid = request.getParameter("bxid");
		request.setAttribute("bxid", bxid);
		return prefix +"addMX";
	}
	@RequestMapping("/zffs")
	public String zffs(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix +"zffs";
	}
	@RequestMapping("/fjsl")
	public String fjsl(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		//request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix +"fjsl";
	}
	
	@RequestMapping("/saveBx")
	@ResponseBody
	public Result saveBx(HttpServletRequest request,Reimburse bx){
		Result r = new Result();
		String id  =request.getParameter("id");
		String fileId = request.getParameter("fileId");
		if(id==null){//新增
			bx.setBxdate(new Date());
			String userid = bx.getReerid();
			User u = baseService.get(User.class, userid);
			Org bm = baseService.get(Org.class, u.getDeptId());
			bx.setDept(bm.getName());
			bx.setPid(u.getDeptId());
			bx.setStatus("10");
			bx.setIsjz("0");
			bx.setMoney("0");//初始金额为0
			if("10".equals(bx.getType())){
				bx.setProid(null);
			}
			baseService.save(bx);
			id = bx.getId();
		}else{
			Reimburse b = baseService.get(Reimburse.class, id);
			b.setContent(bx.getContent());
			b.setType(bx.getType());
			if("10".equals(bx.getType())){
				b.setProid(null);
			}else if("20".equals(bx.getType())){
				b.setProid(bx.getProid());
			}
			baseService.update(b);
		}
		r.setCode(id);
		
		
		if(fileId!=null&&fileId.length()>0){
			if(fileId.indexOf(",")<0){//单张
				CwwjFile wj = baseService.get(CwwjFile.class, fileId);
				wj.setFormId(id);
				baseService.update(wj);
			}else{//多文件
				String[] ids = fileId.split(",");
				for(int i =0;i<ids.length;i++){
					CwwjFile wj = baseService.get(CwwjFile.class, ids[i]);
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
		String id = request.getParameter("id");
		List<CwwjFile> wjs = baseService.findMapBySql("select id  from tbl_financefile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CwwjFile.class);
        String wjid = "";
		for(int i =0;i<wjs.size();i++){
        	if(i==wjs.size()-1){
        		wjid += wjs.get(i).getId();
        	}else {
        		wjid += wjs.get(i).getId()+",";
        	}
        }
		Result r = new Result();
		Reimburse bx = baseService.get(Reimburse.class, id);
		r.setData(bx);
		r.setCode(id);
		r.setMessage(wjid);
		return r;
	}
	@RequestMapping("/deleteBx")
	@ResponseBody
	public Result deleteBx(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Reimburse bx = baseService.get(Reimburse.class, id);
		List<ReimburseDet> bxmxs = baseService.find(" from ReimburseDet where reimburseId='"+id+"'");
		for(ReimburseDet mx : bxmxs){
			baseService.delete(mx);
		}
		
		List<CwwjFile> wjs = baseService.find(" from CwwjFile where form_ID='"+id+"'");
		for(CwwjFile tp:wjs){
			String dirPath=request.getRealPath("/");
	        FileUtil.delFile(uploaderPath+File.separator+tp.getSavedName());
			baseService.delete(tp);
		}
		
		baseService.delete(bx);
		return r;
	}
	
	@RequestMapping("/saveBxMx")
	@ResponseBody
	public Result saveBxMx(HttpServletRequest request,ReimburseDet mx){
		
		Result r = new Result();
		String bxid = request.getParameter("bxid");
		mx.setReimburseId(bxid);
		baseService.save(mx);
		docountJe(bxid);//计算报销金额
		return r;
	}
	@RequestMapping("/deleteBxMx")
	@ResponseBody
	public Result saveBxMx(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		ReimburseDet mx = baseService.get(ReimburseDet.class, id);
		String bxid = mx.getReimburseId();
		baseService.delete(mx);
		docountJe(bxid);//计算报销金额
		return r;
	}
	
	public void docountJe(String bxid){
		Arith ari = new Arith();
		Reimburse bx = baseService.get(Reimburse.class, bxid);
		List<ReimburseDet> bxmxs = baseService.find(" from ReimburseDet where reimburseId='"+bxid+"'");
		Double je = 0.0;
		for(ReimburseDet mx : bxmxs){
			je = ari.add(Double.valueOf(mx.getFpmoney()), je);
		}
		bx.setMoney(je+"");
		baseService.update(bx);
	}
	
	//提交部门审核
	@RequestMapping("/bmsh")
	@ResponseBody
	public Result bmsh(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Reimburse bx = baseService.get(Reimburse.class, id);
		bx.setStatus("20");
		baseService.update(bx);
		return r;
	}
	
	//部门审核
		@RequestMapping("/bmsubmit")
		@ResponseBody
		public Result bmsubmit(HttpServletRequest request,Reimburse jk){
			String id = request.getParameter("id");
			String lx = request.getParameter("lx");
			Reimburse bx = baseService.get(Reimburse.class, id);
			
			User user = SecurityUtil.getUser();
			Org bm = baseService.get(Org.class, bx.getPid());
			if("10".equals(lx)){//通过
				bx.setStatus("40");
				String name ="部门："+bm.getName()+",姓名："+jk.getReername()+",报销【审核】";

		        // businessKey
		        String businessKey = bx.getId();

		        // 配置流程变量
		        Map<String, Object> variables = new HashMap<>();
		        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
		        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
		        variables.put("taskName", name);

		        // 启动流程
		        runtimePageService.startProcessInstanceByKey("Reimburse", name, variables, user.getId(), businessKey);
				
				
			}else if("20".equals(lx)){//驳回
				bx.setStatus("30");
			}
			bx.setBmyj(jk.getBmyj());
			bx.setBmshr(user.getName());
			baseService.update(bx);
			Result r = new Result();
			return r;
		}
	
		//确认付款
		@RequestMapping("/dofk")
		@ResponseBody
		public Result dofk(HttpServletRequest request){
			String id = request.getParameter("id");
			Reimburse bx = baseService.get(Reimburse.class, id);
			bx.setStatus("90");
			baseService.update(bx);
			Result r = new Result();
			return r;
			
		}
		
		
		//记账
				@RequestMapping("/dojz")
				@ResponseBody
				public Result dojz(HttpServletRequest request){
					Arith arith = new Arith();
					String id = request.getParameter("id");
					Reimburse bx = baseService.get(Reimburse.class, id);
					
					String xmid = "";
					String content = bx.getDept()+"-"+bx.getReername()+"报销";
					if("10".equals(bx.getType())){//公司账本
						xmid = "1";
					}else if("20".equals(bx.getType())){//项目账本
						xmid = bx.getProid();
					}
					
					List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+xmid+"'");
					
					User u = SecurityUtil.getUser();
					//账目明细
					ProAccountDet mx = new ProAccountDet();
					mx.setCreateDate(new Date());
					mx.setAbstracts(content);
					mx.setZmtype("2");
					mx.setOutMoney(bx.getMoney());
					mx.setAccounter(u.getName());
					mx.setAccounterId(u.getId());
					//mx.setAuditName(u.getName());
					//mx.setAuditId(u.getId());
					mx.setStatus("10");
					if(xmz.size()<=0){//无账本，先生成账本
						ProAccount zb = new ProAccount();
						zb.setProjectId(xmid);
						if("1".equals(xmid)){
							zb.setAccountName("公司账本");
						}else{
							Project p = baseService.get(Project.class, bx.getProid());
							zb.setAccountName(p.getProjectName());
						}
						
						baseService.save(zb);
						zb.setAllMoney(arith.sub(0.0,Double.valueOf(bx.getMoney()))+"");
						mx.setProjectAccountId(zb.getId());
						baseService.save(mx);
						baseService.update(zb);
					}else{
						ProAccount zb = xmz.get(0);
						zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(bx.getMoney()))+"");
						mx.setProjectAccountId(zb.getId());
						baseService.save(mx);
						baseService.update(zb);
					}
					bx.setIsjz("10");
					baseService.update(bx);
					Result r = new Result();
					return r;
				}
				
				@RequestMapping(value="/projectdetailfile", method = RequestMethod.GET)
			    public String projectdetailfile(String id, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
					List<CwwjFile> wjs = baseService.findMapBySql("select id  from tbl_financefile where form_ID='"+id+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CwwjFile.class);
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
			        List<CwwjFile> fileList=new ArrayList<>();
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
			                    CwwjFile sysFile=new CwwjFile();
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
			    public FileResult getPreivewSettings(List<CwwjFile> fileList,HttpServletRequest request){
			        FileResult fileResult=new FileResult();
			        List<String> previews=new ArrayList<>();
			        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
			        //缓存当前的文件
			        String[] fileArr=new String[fileList.size()];
			        int index=0;
			        for (CwwjFile sysFile : fileList) {
			            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
			            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
			        	String dirPath = request.getContextPath() + "/reimburse/showImage?imageID=" + sysFile.getId();
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
			    	CwwjFile sysFile=baseService.get(CwwjFile.class,id);
			        String dirPath=request.getRealPath("/");
			        FileUtil.delFile(uploaderPath+File.separator+sysFile.getSavedName());
			        baseService.delete(sysFile);
			        return new Result();
			    }
			    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
			    public void downloadFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
			    	CwwjFile sysfile = baseService.get(CwwjFile.class, id);

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
			        List<CwwjFile> fileList=new ArrayList<>();
			        if(!StrUtil.isEmpty(fileIds)) {
			            String[] fileIdArr = fileIds.split(",");
			            DetachedCriteria criteria = DetachedCriteria.forClass(CwwjFile.class);
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
			    public FileResult getPreivewSettingsPreview(List<CwwjFile> fileList,HttpServletRequest request){
			        FileResult fileResult=new FileResult();
			        List<String> previews=new ArrayList<>();
			        List<FileResult.PreviewConfig> previewConfigs=new ArrayList<>();
			        //缓存当前的文件
			        //String dirPath = request.getRealPath("/");
			        String[] fileArr=new String[fileList.size()];
			        int index=0;
			        for (CwwjFile sysFile : fileList) {
			            //上传后预览 TODO 该预览样式暂时不支持theme:explorer的样式，后续可以再次扩展
			            //如果其他文件可预览txt、xml、html、pdf等 可在此配置
			        	String dirPath = request.getContextPath() + "/reimburse/showImage?imageID=" + sysFile.getId();
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
			        
			    	CwwjFile item = baseService.get(CwwjFile.class, imageID);

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
