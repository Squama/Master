/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.system.Arith;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月13日    Create this file
* </pre>
* 
*/

public class VolumePayTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = 4294247719992051739L;

    @Override
    public void notify(DelegateTask delegateTask) {
    	Arith ari = new Arith();
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             VolumePay zf = baseService.get("from VolumePay where id=:id", params);
             
             List<ProjectVolume> zzs = new ArrayList<ProjectVolume>();
             double zje = 0.0;
             String gclid = zf.getVolumeId();
             if("10".equals(zf.getPayType())){//人工
            	List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gclid+"'", SalaryDetail.class);
         		
         		for(SalaryDetail gzmx:gzmxs){
         			zje =ari.add(zje, Double.valueOf(gzmx.getActual()));
         		}
            	List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where salary_id='"+gclid+"'", SalaryVolume.class);
     			for(SalaryVolume gx:gxs){
     				ProjectVolume gcl = baseService.get(ProjectVolume.class, gx.getVolumeID());
     				zzs.add(gcl);
     			}
     		}else{//机械和材料
     			ProjectVolume gcl = baseService.get(ProjectVolume.class, gclid);
     			if("20".equals(zf.getPayType())){//机械
                	zje = ari.add(zje, Double.valueOf(gcl.getFinalMech()));
                }else if("30".equals(zf.getPayType())){//材料
                	zje = ari.add(zje,Double.valueOf(gcl.getFinalMat()));
                }else if("40".equals(zf.getPayType())){//包公包料
                	zje = ari.add(zje,Double.valueOf(gcl.getFinalPack()));
                }
     			zzs.add(gcl);
     		}
             Double yzje = zje;
             //计算是否已经完全支付，并修改工程量的状态
             if("10".equals(zf.getPayType())){
            	 String sql = " select * from tbl_volumePay where volumeId='"+zf.getVolumeId()+"' and payType='10' and status='30'";
          		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
          		for(VolumePay z:zfs){
         			zje = ari.sub(zje, Double.valueOf(z.getPayMoney()));
         		}
             }else if("20".equals(zf.getPayType())){//机械
            	 String sql = " select * from tbl_volumePay where volumeId='"+zf.getVolumeId()+"' and payType='20' and status='30'";
         		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
         		for(VolumePay z:zfs){
         			zje = ari.sub(zje, Double.valueOf(z.getPayMoney()));
         		}
             }else if("30".equals(zf.getPayType())){//材料
            	 String sql = " select * from tbl_volumePay where volumeId='"+zf.getVolumeId()+"' and payType='30' and status='30'";
          		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
          		for(VolumePay z:zfs){
         			zje = ari.sub(zje, Double.valueOf(z.getPayMoney()));
         		}
             }else if("40".equals(zf.getPayType())){//材料
            	 String sql = " select * from tbl_volumePay where volumeId='"+zf.getVolumeId()+"' and payType='40' and status='30'";
          		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
          		for(VolumePay z:zfs){
         			zje = ari.sub(zje, Double.valueOf(z.getPayMoney()));
         		}
             }
             
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 zf.setStatus("30");
            	 zje = ari.sub(zje, Double.valueOf(zf.getPayMoney()));
             }else if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 String suggestion = delegateTask.getVariable("suggestion").toString();
            	 zf.setStatus("40");
            	 zf.setRebutReason(suggestion);
             }
             
             if(zje>0&&zje<yzje){
       			for(ProjectVolume g:zzs){
       				if("10".equals(zf.getPayType())){
         				g.setLabourStatus("10");
                    }else if("20".equals(zf.getPayType())){//机械
                    	g.setMechStatus("10");
                    }else if("30".equals(zf.getPayType())){//材料
                    	g.setMatStatus("10");
                    }else if("40".equals(zf.getPayType())){//包工包料
                    	g.setPackStatus("10");
                    }
       				
       				baseService.update(g);
       			}
       		}else if(zje==0){
       			for(ProjectVolume g:zzs){
       				if("10".equals(zf.getPayType())){
         				g.setLabourStatus("20");
                    }else if("20".equals(zf.getPayType())){//机械
                    	g.setMechStatus("20");
                    }else if("30".equals(zf.getPayType())){//材料
                    	g.setMatStatus("20");
                    }else if("40".equals(zf.getPayType())){//包工包料
                    	g.setPackStatus("20");
                    }
       				baseService.update(g);
       			}
       		}else{
       			for(ProjectVolume g:zzs){
       				if("10".equals(zf.getPayType())){
         				g.setLabourStatus(null);
                    }else if("20".equals(zf.getPayType())){//机械
                    	g.setMechStatus(null);
                    }else if("30".equals(zf.getPayType())){//材料
                    	g.setMatStatus(null);
                    }else if("40".equals(zf.getPayType())){//包工包料
                    	g.setPackStatus(null);
                    }
       				baseService.update(g);
       			}
       		}
             baseService.save(zf);
             
             
        }

    }
    
}
