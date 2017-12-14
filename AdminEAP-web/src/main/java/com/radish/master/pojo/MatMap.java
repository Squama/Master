/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.pojo;

import java.util.Map;

import com.radish.master.entity.Materiel;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月14日    Create this file
* </pre>
* 
*/

public class MatMap {
    
    Map<String, Materiel> map;

    public Map<String, Materiel> getMap() {
        return map;
    }

    public void setMap(Map<String, Materiel> map) {
        this.map = map;
    }
    
}
