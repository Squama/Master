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
             double rgzje = 0.0;
     		double clzje = 0.0;
     		double jxzje = 0.0;
             String gclid = zf.getVolumeId();
             if("10".equals(zf.getPayType())){//人工
            	
            	 String gzid = "";
            	List<SalaryVolume> gxs = baseService.findBySql("select * from tbl_salary_volume where volume_id='"+gclid+"'", SalaryVolume.class);
     			for(SalaryVolume gx:gxs){
     				ProjectVolume gcl = baseService.get(ProjectVolume.class, gx.getVolumeID());
     				zzs.add(gcl);
     				gzid = gx.getSalaryID();
     			}
     			//工资id
     			
     			List<SalaryDetail> gzmxs = baseService.findBySql(" select * from tbl_salary_detail where salary_id ='"+gzid+"'", SalaryDetail.class);
         		
         		for(SalaryDetail gzmx:gzmxs){
         			zje =ari.add(zje, Double.valueOf(gzmx.getActual()));
         		}
     		}else{//机械和材料
     			ProjectVolume gcl = baseService.get(ProjectVolume.class, gclid);
     			if("20".equals(zf.getPayType())){//机械
                	zje = ari.add(zje, Double.valueOf(gcl.getFinalMech()));
                	if(gcl.getFinalDebitjx()!=null){
        				zje = ari.sub(zje, Double.valueOf(gcl.getFinalDebitjx()));
        			}
                }else if("30".equals(zf.getPayType())){//材料
                	zje = ari.add(zje,Double.valueOf(gcl.getFinalMat()));
                	if(gcl.getFinalDebitcl()!=null){
        				zje = ari.sub(zje, Double.valueOf(gcl.getFinalDebitcl()));
        			}
                }else if("40".equals(zf.getPayType())){//包公包料
                	rgzje = Double.valueOf(gcl.getFinalLabour());
        			if(gcl.getFinalDebit()!=null){
        				rgzje = ari.sub(rgzje, Double.valueOf(gcl.getFinalDebit()));
        			}
        			
        			clzje = Double.valueOf(gcl.getFinalMat());
        			if(gcl.getFinalDebitcl()!=null){
        				clzje = ari.sub(clzje, Double.valueOf(gcl.getFinalDebitcl()));
        			}
        			
        			jxzje = Double.valueOf(gcl.getFinalMech());
        			if(gcl.getFinalDebitjx()!=null){
        				jxzje = ari.sub(jxzje, Double.valueOf(gcl.getFinalDebitjx()));
        			}
                }
     			zzs.add(gcl);
     		}
             Double yzje = zje;
             double rgzfs =0.0;
 	   		double clzfs = 0.0;
 	   		double jxzfs = 0.0;
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
            	 String sql = " select * from tbl_volumePay where volumeId='"+zf.getVolumeId()+"'  and status='30'";
          		List<VolumePay> zfs = baseService.findBySql(sql, VolumePay.class);
    	   		for(VolumePay bgbl : zfs){
    	   			rgzfs =ari.add(rgzfs, Double.valueOf(bgbl.getRgmoney()));
    	   			clzfs =ari.add(clzfs, Double.valueOf(bgbl.getClmoney()));
    	   			jxzfs =ari.add(jxzfs, Double.valueOf(bgbl.getJxmoney()));
    	   		}
             }
             
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 zf.setStatus("30");
            	 zje = ari.sub(zje, Double.valueOf(zf.getPayMoney()));
            	 if("40".equals(zf.getPayType())){//材料
    	   			rgzfs =ari.add(rgzfs, Double.valueOf(zf.getRgmoney()));
    	   			clzfs =ari.add(clzfs, Double.valueOf(zf.getClmoney()));
    	   			jxzfs =ari.add(jxzfs, Double.valueOf(zf.getJxmoney()));
                 }
             }else if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 String suggestion = delegateTask.getVariable("suggestion").toString();
            	 zf.setStatus("40");
            	 zf.setRebutReason(suggestion);
             }
             if("40".equals(zf.getPayType())){//包工包料算法
            	//计算是否完全支付完
	        	 for(ProjectVolume g:zzs){
	        		 if(rgzfs>=rgzje){
	      	   			g.setLabourStatus("20");
	      	   		}else if(rgzfs>0&&rgzfs<rgzje){
	      	   			g.setLabourStatus("10");
	      	   		}else{
	      	   			g.setLabourStatus(null);
	      	   		}
	      	   		if(clzfs>=clzje){
	      	   			g.setMatStatus("20");
	      	   		}else if(clzfs>0&&clzfs<clzje){
	      	   			g.setMatStatus("10");
	      	   		}else{
	      	   			g.setMatStatus(null);
	      	   		}
	      	   		if(jxzfs>=jxzje){
	      	   			g.setMechStatus("20");
	      	   		}else if(jxzfs>0&&jxzfs<jxzje){
	      	   			g.setMechStatus("10");
	      	   		}else{
	      	   			g.setMechStatus(null);
	      	   		}
	      	   		baseService.update(g);
	        	 }
             }else{
            	 if(zje>0&&zje<yzje){
            			for(ProjectVolume g:zzs){
            				if("10".equals(zf.getPayType())){
              				g.setLabourStatus("10");
                         }else if("20".equals(zf.getPayType())){//机械
                         	g.setMechStatus("10");
                         }else if("30".equals(zf.getPayType())){//材料
                         	g.setMatStatus("10");
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
                         }
            				baseService.update(g);
            			}
            		}
             }
             baseService.save(zf);
             
             
        }

    }
    
}
