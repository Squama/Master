/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.radish.master.pojo.Options;
import com.radish.master.service.WechatService;

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
@Service("wechatService")
public class WechatServiceImpl extends BaseServiceImpl implements WechatService {

    @Override
    public List<Options> getAgentOptions() {
        return this.findMapBySql("select id value, agent_name data from tbl_wechat_config", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }


}
