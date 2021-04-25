/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.budget;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import com.radish.master.entity.project.Fee;
import com.radish.master.entity.project.FeeDetail;
import com.radish.master.entity.volumePay.Reimburse;
import com.radish.master.service.CommonService;
import com.radish.master.system.Arith;
import com.radish.master.system.FileHelper;
import com.radish.master.system.TimeUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月15日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/project/feevolume")
public class FeeVolumeController {

    @Resource
    private CommonService commonService;
    
    @Autowired
    private BaseService baseService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    private static Logger logger= LoggerFactory.getLogger(UploaderController.class);
	private static final String uploaderPath=PropertiesUtil.getValue("qualityChecksFilePath")+"\\gf";
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
    
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String list(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/fee/fee_report_list";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/querylist")
    private String queryList(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/fee/fee_query_list";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/listmanage")
    private String listManage(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/fee/manage/fee_report_list";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/querylistmanage")
    private String queryListManage(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/fee/manage/fee_query_list";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("feeID", id);
        return "budgetmanage/fee/detail";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        request.setAttribute("feeOptions", JSONArray.toJSONString(commonService.getFeeCombobox()));
        return "budgetmanage/fee/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request){
        request.setAttribute("feeID", id);
        request.setAttribute("feeOptions", JSONArray.toJSONString(commonService.getFeeCombobox()));
        return "budgetmanage/fee/edit";
    }
    
    @RequestMapping(value="/addmanage",method = RequestMethod.GET)
    public String addManage(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        request.setAttribute("feeOptions", JSONArray.toJSONString(commonService.getFeeManageCombobox()));
        return "budgetmanage/fee/manage/edit";
    }
    
    @RequestMapping(value="/editmanage",method = RequestMethod.GET)
    public String editManage(String id, HttpServletRequest request){
        request.setAttribute("feeID", id);
        request.setAttribute("feeOptions", JSONArray.toJSONString(commonService.getFeeManageCombobox()));
        return "budgetmanage/fee/manage/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(Fee fee, HttpServletRequest request){
    	String fileId = request.getParameter("fileId");
        fee.setStatus("10");
        fee.setType("10");
        fee.setUpdateDateTime(new Date());
        fee.setAmount("0");
        fee.setName(SecurityUtil.getUser().getName() + "上报规费" + TimeUtil.getCurrentTime());
        
        try {
            commonService.save(fee);
            String id = fee.getId();
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
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系管理员");
        }
        return new Result(true, fee);
    }
    @RequestMapping(method = RequestMethod.POST, value="/delete")
    @ResponseBody
    public Result delete(String id,HttpServletRequest request){
    	Fee fee = commonService.get(Fee.class, id);
    	List<FeeDetail> fees =  commonService.find(" from FeeDetail where feeID='"+id+"'") ;  
    	for(FeeDetail f : fees){
    		commonService.delete(f);
    	}
    	commonService.delete(fee);
    	List<CwwjFile> wjs = baseService.find(" from CwwjFile where form_ID='"+id+"'");
		for(CwwjFile tp:wjs){
			String dirPath=request.getRealPath("/");
	        FileUtil.delFile(uploaderPath+File.separator+tp.getSavedName());
			baseService.delete(tp);
		}
        return new Result(true, "删除成功！");
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savemanage")
    @ResponseBody
    public Result saveManage(Fee fee, HttpServletRequest request){
    	String fileId = request.getParameter("fileId");
        fee.setStatus("10");
        fee.setType("20");
        fee.setUpdateDateTime(new Date());
        fee.setAmount("0");
        fee.setName(SecurityUtil.getUser().getName() + "上报管理费" + TimeUtil.getCurrentTime());
        
        try {
            commonService.save(fee);
            
            String id = fee.getId();
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
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系管理员");
        }
        return new Result(true, fee);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savedetail")
    @ResponseBody
    public Result saveDet(FeeDetail feeDetail, HttpServletRequest request){
        feeDetail.setCreateDateTime(new Date());
        commonService.save(feeDetail);
        
        Fee fee = commonService.get(Fee.class, feeDetail.getFeeID());
        BigDecimal oldValue = new BigDecimal(fee.getAmount());
        BigDecimal thisValue = new BigDecimal(feeDetail.getPrice());
        fee.setAmount(oldValue.add(thisValue).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        commonService.save(fee);
        
        return new Result(true, "success");
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/deletedetail")
    @ResponseBody
    public Result deleteDet(String id, HttpServletRequest request){
        FeeDetail feeDetail = commonService.get(FeeDetail.class, id);
        String feeid = feeDetail.getFeeID();
        commonService.delete(feeDetail);
        
        Fee fee = commonService.get(Fee.class, feeDetail.getFeeID());
        BigDecimal oldValue = new BigDecimal(fee.getAmount());
        BigDecimal thisValue = new BigDecimal(feeDetail.getPrice());
        fee.setAmount(oldValue.subtract(thisValue).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        commonService.save(fee);
        
        return new Result(true, "success",feeid);
    }
    
    @RequestMapping(value="/getfee")
    @ResponseBody
    public Result getPackage(String feeID){
        Fee fee = commonService.get(Fee.class, feeID);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("projectName", fee.getProjectName());
        map.put("projectSubName", fee.getProjectSubName());
        map.put("payObj", fee.getPayObj());
        
        List<CwwjFile> wjs = baseService.findMapBySql("select id  from tbl_financefile where form_ID='"+feeID+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, CwwjFile.class);
        String wjid = "";
		for(int i =0;i<wjs.size();i++){
        	if(i==wjs.size()-1){
        		wjid += wjs.get(i).getId();
        	}else {
        		wjid += wjs.get(i).getId()+",";
        	}
        }
		map.put("wjid", wjid);
        return new Result(true, map);
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        Fee fee = commonService.get(Fee.class, id);
        fee.setStatus("20");
        fee.setUpdateDateTime(new Date());
        
        commonService.update(fee);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "上报规费，项目：" + fee.getProjectName() +",子项：" + fee.getProjectSubName();
        
        //businessKey
        String businessKey = fee.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("feeBudget", name, variables,
                user.getId(), businessKey);
    }
    
    @RequestMapping(value = "/startmanage", method = RequestMethod.POST)
    @ResponseBody
    public Result startManage(String id) {
        Fee fee = commonService.get(Fee.class, id);
        fee.setStatus("20");
        fee.setUpdateDateTime(new Date());
        
        commonService.update(fee);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "上报管理费，项目：" + fee.getProjectName() +",子项：" + fee.getProjectSubName();
        
        //businessKey
        String businessKey = fee.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("feeBudget", name, variables,
                user.getId(), businessKey);
    }
    //-------------------------------------------财务支付部分
    @RequestMapping("/payList")
    private String payList(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/fee/payList";
    }
    @RequestMapping("/doPrint")
    private String doPrint(HttpServletRequest request) {
        request.setAttribute("feeID",request.getParameter("id"));
        request.setAttribute("zdr", SecurityUtil.getUser().getId());
        return "budgetmanage/fee/payEdit";
    }
    @RequestMapping(value = "/changePayObj", method = RequestMethod.POST)
    @ResponseBody
    public Result startManage(Fee fee) {
    	 Fee f = commonService.get(Fee.class, fee.getId());
         f.setPayObj(fee.getPayObj());
         commonService.update(f);
         return new Result(true);
    }
    @RequestMapping("/dojz")
	@ResponseBody
	public Result dojz(HttpServletRequest request){
		Arith arith = new Arith();
		String id = request.getParameter("id");
		Fee fee = commonService.get(Fee.class, id);
		
		String xmid = "";
		String content = "";
		if("10".equals(fee.getType())){//规费
			content = "规费消耗：其中";
		}else if("20".equals(fee.getType())){//管理费
			content = "管理费消耗：其中";
		}
		List<FeeDetail> feeMxs = commonService.find(" from FeeDetail where feeID='"+id+"'");
		for(FeeDetail feeMx:feeMxs){
			content += feeMx.getName()+"("+feeMx.getPrice()+"),";
		}
		
		List<ProAccount> xmz = commonService.find(" from ProAccount where projectId='"+fee.getProjectID()+"'");
		User u = SecurityUtil.getUser();
		//账目明细
		ProAccountDet mx = new ProAccountDet();
		mx.setCreateDate(new Date());
		mx.setAbstracts(content);
		mx.setZmtype("2");
		mx.setOutMoney(fee.getAmount());
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
				Project p = commonService.get(Project.class, fee.getProjectID());
				zb.setAccountName(p.getProjectName());
			}
			
			commonService.save(zb);
			zb.setAllMoney(arith.sub(0.0,Double.valueOf(fee.getAmount()))+"");
			mx.setProjectAccountId(zb.getId());
			commonService.save(mx);
			commonService.update(zb);
		}else{
			ProAccount zb = xmz.get(0);
			zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(fee.getAmount()))+"");
			mx.setProjectAccountId(zb.getId());
			commonService.save(mx);
			commonService.update(zb);
		}
		fee.setIsjz("1");
		commonService.update(fee);
		Result r = new Result();
		return r;
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
        	String dirPath = request.getContextPath() + "/project/feevolume/showImage?imageID=" + sysFile.getId();
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
        	String dirPath = request.getContextPath() + "/project/feevolume/showImage?imageID=" + sysFile.getId();
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
