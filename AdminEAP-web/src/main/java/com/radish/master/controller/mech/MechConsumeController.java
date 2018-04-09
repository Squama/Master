/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.mech;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radish.master.service.MechService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月9日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/mech/consume")
public class MechConsumeController {

    @Resource
    private MechService mechService;
    
}
