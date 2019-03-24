package com.radish.master.controller.doCount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.core.util.ArrayUtils;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.doCount.CostAll;
import com.radish.master.entity.doCount.CostAllST;
import com.radish.master.entity.doCount.InAndOutDet;
import com.radish.master.pojo.MatInAndOut;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("costall")
public class CostAllController {
	private String prefix="/doCount/costall/";
	
	private Arith ari;
	
	@Resource
    private BudgetService budgetService;
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix +"costindex";
	}
	
	@RequestMapping("/docount")
	@ResponseBody
	public Map<String,Object> docount(HttpServletRequest request) throws NumberFormatException, IllegalAccessException{
		
		
		int i=1;
		Map<String,Object> m = new HashMap<String,Object>();
		//拿到项目id
		String xmid = request.getParameter("xmid");
		
		//先删除零时数据
		List<CostAll> mxs =   baseService.find(" from CostAll where proid='"+xmid+"'");
			baseService.batchDelete(mxs);
		
		
		List<CostAllST> sts = baseService.findMapBySql("select * from v_costall where id = '"+xmid+"'", new Object[]{}, new Type[]{}, CostAllST.class);
		List<CostAll> jgs = new ArrayList<CostAll>();
		//项目id  序号  col1 col2 测算 消耗 对比 合同金额
		//先存总项目信息
		if(sts.size()>0){
			CostAllST st = sts.get(0);
			CostAll a = getstl(xmid,(i++)+"","一",st.getXmmc()+"","","","","");
			jgs.add(a);
		}
		List<String[]> cs = new ArrayList<String[]>();
		List<String[]> xh = new ArrayList<String[]>();
		for(CostAllST st :sts){
			String[] zcs = new String[]{
					st.getC1()+"",st.getC2()+"",st.getC3()+"",
					st.getC4()+"",st.getC5()+"",st.getC6()+"",
					st.getC7()+"",st.getC8()+"",st.getC9()+"",st.getC10()+""};
			String[] zxh = new String[]{st.getD1()+"",st.getD2()+"",st.getD3()+"",
					st.getD4()+"",st.getD5()+"",st.getD6()+"",
					st.getD7()+"",st.getD8()+"",st.getD9()+"",st.getD10()+""};
			//存入临时表
			String[] cs1 = new String[]{st.getC1()+"",st.getC2()+"",st.getC3()+"",
					st.getC4()+"",st.getC5()+""};
			String[] xh1 = new String[]{st.getD1()+"",st.getD2()+"",st.getD3()+"",
					st.getD4()+"",st.getD5()+""};
			//从子项开始存
			CostAll a1 = getstl(xmid,(i++)+"","子项",st.getZxmc()+"","","","","");
			CostAll a2 = getstl(xmid,(i++)+"","1","单项工程费",jssz(cs1),jssz(xh1),divString(jssz(xh1),jssz(cs1)),"");
			CostAll a3 = getstl(xmid,(i++)+"","1.1","人工费",st.getC1()+"",st.getD1()+"",divString(st.getD1()+"",st.getC1()+""),"");
			CostAll a4 = getstl(xmid,(i++)+"","1.2","材料费",st.getC2()+"",st.getD2()+"",divString(st.getD2()+"",st.getC2()+""),"");
			CostAll a5 = getstl(xmid,(i++)+"","1.3","机械费",st.getC3()+"",st.getD3()+"",divString(st.getD3()+"",st.getC3()+""),"");
			CostAll a6 = getstl(xmid,(i++)+"","1.4","包工包料",st.getC4()+"",st.getD4()+"",divString(st.getD4()+"",st.getC4()+""),"");
			CostAll a7 = getstl(xmid,(i++)+"","1.5","机械租赁",st.getC5()+"",st.getD5()+"",divString(st.getD5()+"",st.getC5()+""),"");
			
			String[] cs2 = new String[]{st.getC6()+"",st.getC7()+""};
			String[] xh2 = new String[]{st.getD6()+"",st.getD7()+""};
			CostAll a8 = getstl(xmid,(i++)+"","2","项目措施费",jssz(cs2),jssz(xh2),divString(jssz(xh2),jssz(cs2)),"");
			CostAll a9 = getstl(xmid,(i++)+"","2.1","安全文明施工费",st.getC6()+"",st.getD6()+"",divString(st.getD6()+"",st.getC6()+""),"");
			CostAll a10 = getstl(xmid,(i++)+"","2.2","其他组织措施费",st.getC7()+"",st.getD7()+"",divString(st.getD7()+"",st.getC7()+""),"");
			CostAll a11 = getstl(xmid,(i++)+"","3","规费",st.getC8()+"",st.getD8()+"",divString(st.getD8()+"",st.getC8()+""),"");
			CostAll a12 = getstl(xmid,(i++)+"","4","管理费",st.getC9()+"",st.getD9()+"",divString(st.getD9()+"",st.getC9()+""),"");
			CostAll a13 = getstl(xmid,(i++)+"","5","税金",st.getC10()+"",st.getD10()+"",divString(st.getD10()+"",st.getC10()+""),"");
			
			CostAll a14 = getstl(xmid,(i++)+"","","小计",jssz(zcs),jssz(zxh),divString(jssz(zxh),jssz(zcs)),"");
			cs.add(zcs);
			xh.add(zxh);
			
			jgs.add(a1);
			jgs.add(a2);
			jgs.add(a3);
			jgs.add(a4);
			jgs.add(a5);
			jgs.add(a6);
			jgs.add(a7);
			jgs.add(a8);
			jgs.add(a9);
			jgs.add(a10);
			jgs.add(a11);
			jgs.add(a12);
			jgs.add(a13);
			jgs.add(a14);
		}
		String jgcs = "0.0";
		for(String[] jgcss : cs){
			String re1 = jssz(jgcss);
			jgcs = addStr(jgcs,re1);
		}
		String jgxh = "0.0";
		for(String[] jgxhs : xh){
			String re1 = jssz(jgxhs);
			jgxh = addStr(jgxh,re1);
		}
		if(jgs.size()>0){
			//插入总项目的测算、消耗、对比、合同金额
			jgs.get(0).setCol3(jgcs);
			jgs.get(0).setCol4(jgxh);
			jgs.get(0).setCol5(divString(jgxh,jgcs));
			//拿到合同金额
			List<Labor> hts = baseService.find(" from Labor where project_id='"+xmid+"'");
			Double htje = 0.0;
			for(Labor ht:hts){
				htje = ari.add(htje, Double.valueOf(ht.getContractPrice()));
			}
			jgs.get(0).setCol6(htje+"");
		}
		
		//最后保存
		baseService.batchSave(jgs);
		
		m.put("success", "true");
		m.put("id",xmid);
		return m;
	}
	
	public CostAll getstl(String a1,String a2,String a3,String a4,String a5,String a6,String a7,String a8){
		CostAll a = new CostAll();
		a.setProid(a1);
		a.setXh(a2);
		a.setCol1(a3);
		a.setCol2(a4);
		a.setCol3(a5);
		a.setCol4(a6);
		a.setCol5(a7);
		a.setCol6(a8);
		return a;
		
	}
	public String addStr(String s1,String s2){
		Double jg = ari.add(Double.valueOf(s1), Double.valueOf(s2));
		return jg+"";
	}
	public String jssz(String[] sz){
		Double jg = 0.0;
		for(int i =0;i<sz.length;i++){
			jg = ari.add(jg, Double.valueOf(sz[i]));
		}
		return jg+"";
	}
	public String divString(String a1,String a2) throws NumberFormatException, IllegalAccessException{
		if(Double.valueOf(a1)==0|| Double.valueOf(a2)==0){
			return "0";
					
		}
		return  div(Double.valueOf(a1), Double.valueOf(a2), 2)+"";
	}
	 public static double div(double value1,double value2,int scale) throws IllegalAccessException{
	        //如果精确范围小于0，抛出异常信息
	        if(scale<0){
	            throw new IllegalAccessException("精确度不能小于0");
	        }
	        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
	        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
	        double b =  b1.divide(b2, 2, RoundingMode.HALF_UP).doubleValue();
	        return b;
	    }
	
	@RequestMapping("/getList")
	@ResponseBody
	public List<CostAll> getList(String xmid){
		List<CostAll> mxs =   baseService.find(" from CostAll where proid='"+xmid+"'");
		return mxs;
	}
}
