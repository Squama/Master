/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.safty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.util.Streams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cnpc.framework.base.entity.UserAvatar;
import com.cnpc.framework.base.pojo.AvatarResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.UploaderService;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.Worker;
import com.radish.master.service.CommonService;
import com.radish.master.service.ProjectService;
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
        if (StrUtil.isEmpty(worker.getId())) {
            commonService.save(worker);
        } else {
            Worker oldWorker=commonService.get(Worker.class, worker.getId());
            SpringUtil.copyPropertiesIgnoreNull(worker, oldWorker);
            oldWorker.setUpdateDateTime(new Date());
            commonService.update(oldWorker);
        }
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private Worker getWorker(String id) {
        return commonService.get(Worker.class, id);
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
    
}
