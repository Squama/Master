/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.mech;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

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

public class MechConsumeTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = -4858209421629313973L;

    /**
     * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        // TODO Auto-generated method stub

    }

}
