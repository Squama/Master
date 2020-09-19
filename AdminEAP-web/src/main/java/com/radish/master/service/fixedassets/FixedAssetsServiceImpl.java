/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.service.fixedassets;

import java.util.List;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
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
@Service("fixedAssetsService")
public class FixedAssetsServiceImpl extends BaseServiceImpl implements FixedAssetsService {

	@Override
    public List<Options> getAllocateAssetsCombobox() {
        return this.findMapBySql("select id value, name data from tbl_fixedassets_stk where fa_type IN ('10', '20')", new Object[] {}, new Type[] {}, Options.class);
    }
	@Override
    public List<FixedAssetsStk> getAllocateAssetsComboboxInfo() {
//        return this.findMapBySql("select * from tbl_fixedassets_stk where fa_type IN ('10', '20')", new Object[] {}, new Type[] {}, FixedAssetsStk.class);
        return this.find(" from FixedAssetsStk where fa_type IN ('10', '20') and quantity_avl>0.0");
	}
	
	@Override
    public List<Options> getDeptNameCombobox() {
        return this.findMapBySql("select name value, name data from tbl_org", new Object[] {}, new Type[] {}, Options.class);
    }
	@Override
    public List<Options> getDeptNameComboboxByXm() {
        return this.findMapBySql("select name value, name data from tbl_org where name like '%项目%'", new Object[] {}, new Type[] {}, Options.class);
    }

	@Override
	public List<Options> getAllocateStkExitsCombobox(String stkID) {
		FixedAssetsStk stk = this.get(FixedAssetsStk.class, stkID);
		
        return this.findMapBySql("select belonged_stock value, belonged_stock data from tbl_fixedassets_stk where fa_type IN ('10', '20') AND name=? AND norm=? AND unit=?", 
        		new Object[] {stk.getName(), stk.getNorm(), stk.getUnit()}, new Type[] {StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE}, Options.class);

	}
	
	@Override
	public List<Options> getAllocateStkExitsComboboxByXm(String stkID) {
		FixedAssetsStk stk = this.get(FixedAssetsStk.class, stkID);
		
        return this.findMapBySql("select belonged_stock value, belonged_stock data from tbl_fixedassets_stk where fa_type IN ('10', '20') AND name=? AND norm=? AND unit=? AND belonged_stock like '%项目%'", 
        		new Object[] {stk.getName(), stk.getNorm(), stk.getUnit()}, new Type[] {StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE}, Options.class);

	}
	@Override
	public List<Options> getAllocateStkExitsComboboxByAll(String stkID) {
		FixedAssetsStk stk = this.get(FixedAssetsStk.class, stkID);
		
        return this.findMapBySql("select belonged_stock value, belonged_stock data from tbl_fixedassets_stk where fa_type IN ('10', '20') AND name=? AND norm=? AND unit=? and belonged_stock=?", 
        		new Object[] {stk.getName(), stk.getNorm(), stk.getUnit(),stk.getBelongedStock()}, new Type[] {StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE}, Options.class);

	}
}
