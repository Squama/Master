/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.wechat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.PropertiesUtil;
import com.radish.master.entity.wechat.WechatConfig;
import com.radish.master.pojo.AccessToken;
import com.radish.master.pojo.SendWeChatVO;
import com.radish.master.service.WechatService;
import com.radish.master.system.CodeException;
import com.radish.master.system.WeChatUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月2日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/wechat/send")
public class SendController {
    
    @Resource
    private WechatService wechatService;
    
    private static final String SENDMESSAGE_URL = PropertiesUtil.getValue("SENDMESSAGE_URL");

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        request.setAttribute("agentOptions", JSONArray.toJSONString(wechatService.getAgentOptions()));
        return "wechat/send/send_list";
    }
    
    
    
    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/dosend")
    @ResponseBody
    private Result sendWeChat(SendWeChatVO sendWeChatVO, HttpServletRequest request) throws CodeException {
        
        WechatConfig wechatConfig = wechatService.get(WechatConfig.class, sendWeChatVO.getConfigID());
        
        // 获取Token
        AccessToken token = WeChatUtil.getAccessToken(wechatConfig.getSecret());
        // 组装消息
        String alarmString = getAlarmString(sendWeChatVO, wechatConfig.getAgentID());
        // 发送消息
        String postURL = SENDMESSAGE_URL.replace("ACCESS_TOKEN", token.getToken());
        JSONObject responseJSON = WeChatUtil.httpRequest(postURL, "POST", alarmString);
        
        String code = responseJSON.getString("errcode");
        String msg = responseJSON.getString("errmsg");
        
        if("0".equals(code)){
            return new Result(true, msg); 
        }else{
            return new Result(false, msg); 
        }
        
        
    }
    
    private String getAlarmString(SendWeChatVO sendWeChatVO, String agentID) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 成员ID列表
        map.put("touser", "@all");
        // 部门ID列表，当touser为@all时忽略本参数
        map.put("toparty", "");
        // 标签ID列表，当touser为@all时忽略本参数
        map.put("totag", "");
        // 消息类型，固定为text
        map.put("msgtype", "text");
        // 企业应用ID
        map.put("agentid", agentID);

        HashMap<String, String> contentMap = new HashMap<String, String>();
        contentMap.put("content", sendWeChatVO.getContent());
        map.put("text", contentMap);

        // 消息是否保密，0：否，1：是
        map.put("safe", "0");

        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return json.toString();
    }
    
}
