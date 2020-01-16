/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.service.fixedassets;

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
* dongyan      2019年1月11日    Create this file
* </pre>
* 
*/

public interface FixedAssetsService extends BaseService {

	List<Options> getAllocateAssetsCombobox();
	
	List<Options> getDeptNameCombobox();
	
	List<Options> getDeptNameComboboxByXm();
	
	List<Options> getAllocateStkExitsCombobox(String stkID);
	
	List<Options> getAllocateStkExitsComboboxByXm(String stkID);
	
}
