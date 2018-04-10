/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.service;

import java.util.List;

import com.cnpc.framework.base.service.BaseService;
import com.radish.master.pojo.Options;

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

public interface MechService extends BaseService{

    List<Options> getMechComboboxByProject(String projectID);
    
    List<Options> getConsumeCombobox(String mechID);
    
}
