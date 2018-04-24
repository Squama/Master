/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.common.TaskWechat;
import com.radish.master.entity.wechat.WechatConfig;
import com.radish.master.pojo.AccessToken;
import com.radish.master.system.CodeException;
import com.radish.master.system.SpringUtil;
import com.radish.master.system.WeChatUtil;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年3月8日    Create this file
 * </pre>
 * 
 */

public class ProjectVolumeTaskGeneralListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;

    private static final String TRUE = "true";

    private static final String FALSE = "false";

    private static final String ZHILIANG = "zhiliangApproved";

    private static final String SHIGONG = "shigongApproved";

    private static final String ANQUAN = "anquanApproved";
    
    private static final Map<String, String> TASKMAP = new HashMap<String, String>(){{
        put("zhiliang", "质量员");
        put("shigong", "施工员");
        put("anquan", "安全员");
        put("fuzeren", "项目负责人");
        put("yusuan", "预算人员");
        put("boss", "分公司主管");
        put("account", "财务人员");
        put("jingyingke", "经营科");
    }};

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            ProjectVolume pv = baseService.get(ProjectVolume.class, businessKey);
            
            String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
            Map<String, Object> suggestionParams = new HashMap<>();
            suggestionParams.put("businessKey", businessKey);
            suggestionParams.put("taskNode", taskDefinitionKey);
            ActivitiSuggestion as = baseService.get(suggestionHql, suggestionParams);
            
            if(as == null){
                as = new ActivitiSuggestion();
                as.setCreateDateTime(new Date());
                as.setUpdateDateTime(new Date());
                as.setBusinessKey(businessKey);
                as.setTaskNode(taskDefinitionKey);
                as.setApprove("true");
            }

            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ZHILIANG, FALSE);
                } else if ("shigong".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(SHIGONG, FALSE);
                } else if ("anquan".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ANQUAN, FALSE);
                }
                as.setApprove("false");
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ZHILIANG, TRUE);
                } else if ("shigong".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(SHIGONG, TRUE);
                } else if ("anquan".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ANQUAN, TRUE);
                }
                if (delegateTask.getVariable(ZHILIANG) != null && delegateTask.getVariable(SHIGONG) != null && delegateTask.getVariable(ANQUAN) != null
                        && TRUE.equals(delegateTask.getVariable(ZHILIANG).toString()) && TRUE.equals(delegateTask.getVariable(SHIGONG).toString())
                        && TRUE.equals(delegateTask.getVariable(ANQUAN).toString())) {
                    pv.setStatus("30");
                }
            }
            
            if ("fuzeren".equals(taskDefinitionKey)) {
                pv.setStatus("40");
            } else if ("yusuan".equals(taskDefinitionKey)) {
                pv.setStatus("50");
            } else if ("boss".equals(taskDefinitionKey)) {
                pv.setStatus("60");
            } else if ("account".equals(taskDefinitionKey)) {
                //在流程审核完成时，统计该合同下的各次工程量上报金额，记录到合同的消耗字段中。 即tbl_labor新加的字段
            	
            	String hql = "from ProjectVolume where laborID=:laborID AND status=:status";
                Map<String, Object> params = new HashMap<>();
                params.put("laborID", pv.getLaborID());
                params.put("status", "70");
            	List<ProjectVolume> pvList = baseService.find(hql, params);
            	BigDecimal sum = new BigDecimal("0");
            	for(ProjectVolume proV : pvList){
            		BigDecimal consume = new BigDecimal(proV.getEngineerMoney());
                    
                    sum = sum.add(consume);
            	}
            	
            	BigDecimal thisVolume = new BigDecimal(pv.getEngineerMoney());
            	sum = sum.add(thisVolume);
            	
            	Labor labor = baseService.get(Labor.class, pv.getLaborID());
            	labor.setConsumePrice(sum.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            	
            	baseService.save(labor);
            	
                pv.setStatus("70");
            }

            baseService.save(pv);
            as.setSuggestion(suggestion);
            as.setOperator(SecurityUtil.getUser().getName());
            as.setUpdateDateTime(new Date());
            baseService.save(as);
            
            
            String wechatHql = "from TaskWechat where processDefinitionKey=:processDefinitionKey";
            Map<String, Object> wechatParams = new HashMap<>();
            wechatParams.put("processDefinitionKey", "projectVolume");
            TaskWechat tw = baseService.get(wechatHql, wechatParams);
            
            if(tw == null){
                return;
            }
            
            WechatConfig wechatConfig = baseService.get(WechatConfig.class, tw.getWechatConfigID());
            
            
            try {
                // 获取Token
                AccessToken token = WeChatUtil.getAccessToken(wechatConfig.getSecret());
                // 组装消息
                String alarmString = getMessage(taskDefinitionKey, wechatConfig.getAgentID(), tw.getToParty(), delegateTask);
                // 发送消息
                String postURL = PropertiesUtil.getValue("SENDMESSAGE_URL").replace("ACCESS_TOKEN", token.getToken());
                JSONObject responseJSON = WeChatUtil.httpRequest(postURL, "POST", alarmString);
                System.out.println(responseJSON);
            } catch (CodeException e) {
                e.printStackTrace();
            }
            

        }

    }
    
    private String getMessage(String taskCode, String agentID, String party, DelegateTask delegateTask){
        Map<String, Object> map = new HashMap<String, Object>();

        // 成员ID列表
        map.put("touser", "");
        // 部门ID列表，当touser为@all时忽略本参数
        map.put("toparty", party);
        // 标签ID列表，当touser为@all时忽略本参数
        map.put("totag", "");
        // 消息类型，固定为text
        map.put("msgtype", "textcard");
        // 企业应用ID
        map.put("agentid", agentID);

        HashMap<String, String> contentMap = new HashMap<String, String>();
        contentMap.put("title", "工程量上报流程变更通知");
        contentMap.put("description", getDesc(taskCode, delegateTask));
        contentMap.put("url", "URL");
        contentMap.put("btntxt", "以上");
        map.put("textcard", contentMap);

        // 消息是否保密，0：否，1：是
        map.put("safe", "0");

        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return json.toString();
    }
    
    private String getDesc(String taskCode, DelegateTask delegateTask){
        
        String name ="";
        if(delegateTask.getVariable("taskName") != null){
            name = delegateTask.getVariable("taskName").toString();
        }
        
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"gray\">");
        sb.append(year+"年"+month+"月"+day+"日");
        sb.append("</div> <div class=\"normal\">");
        sb.append(name);
        sb.append("</div><div class=\"highlight\">");
        sb.append("已经过【");
        sb.append(TASKMAP.get(taskCode));
        sb.append("】审批，操作人：");
        sb.append(SecurityUtil.getUser().getName());
        sb.append("</div><div class=\"normal\">");
        sb.append("审批结果：");
        sb.append(TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())?"通过":"不通过");
        sb.append("</div><div class=\"highlight\">");
        sb.append("审批意见：");
        sb.append(delegateTask.getVariable("suggestion").toString());
        sb.append("</div>");
        
        return sb.toString();
    }

}
