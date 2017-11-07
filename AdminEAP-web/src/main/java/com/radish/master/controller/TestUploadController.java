/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.controller;

import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.service.UploaderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年10月24日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/imageupload")
public class TestUploadController {
    @Resource
    private UploaderService uploaderService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "workmanage/testUpload";
    }

    @RequestMapping(value="/download/{id}",method = RequestMethod.GET)
    public void downloadFile(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        SysFile sysfile = uploaderService.get(SysFile.class, id);
        InputStream is = null;
        OutputStream os = null;
        File file = null;
        try {
            // PrintWriter out = response.getWriter();
            if (sysfile != null)
                file = new File(request.getRealPath("/")+sysfile.getFilePath());
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
    
}
