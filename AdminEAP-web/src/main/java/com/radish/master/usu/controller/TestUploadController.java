/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.usu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "workmanage/testUpload";
    }
    
}
