package com.radish.master.controller.safty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.safty.AqTest;
import com.radish.master.entity.safty.AqTestModel;
import com.radish.master.entity.safty.AqTestMx;
import com.radish.master.entity.safty.Aqjyfj;
import com.radish.master.entity.safty.CheckFileAQ;
import com.radish.master.pojo.AqtestVO;
import com.radish.master.pojo.Options;

@Controller
@RequestMapping("/aqtest")
public class AqTestController {
	@Autowired
	private BaseService baseService;
	
	private String prefix = "/safetyManage/aqtest/";
	
	/**
	 * 1-项目部考核
	 * 2-安全生产
	 * 3-安全目表
	 * 4-形象
	 * 
	 */
	@RequestMapping("/addindex")
	public String jobduty(HttpServletRequest request){
		User u = SecurityUtil.getUser();
		request.setAttribute("name", u.getName());
		return prefix +"addindex";
	}
	
	@RequestMapping("/getModelGroup")
	@ResponseBody
	public Result getModelGroup(String type){
		List<Options> wjs = baseService.findMapBySql("select station data,station value  from tbl_aqtestmodel "
				+ "where type='"+type+"' group by station", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		return new Result(true,wjs);
	}
	@RequestMapping("/getModelInfo")
	@ResponseBody
	public Result getModelInfo(String type,String gw){
		List<AqTestModel> yyfjs = baseService.find(" from AqTestModel where type='"+type+"' and station='"+gw+"' order by orderNum");
		return new Result(true,yyfjs);
	}
	@RequestMapping("/save")
	@ResponseBody
	public Result save(AqtestVO vo,AqTest test,HttpServletRequest request){
		List<Map<String,String>> css = vo.getList();
		if(css==null||css.size()==0){
			return new Result(false,"请录入考核明细");
		}
		List<AqTestMx> mxs = new ArrayList<AqTestMx>();
		for(Map<String,String> cs :css){
			String df = cs.get("df");
			String kf = cs.get("kf");
			String mbid = cs.get("mbid");
			if(df==null||kf==null){
				return new Result(false,"明细不完整，请补充");
			}
			AqTestMx mx = new AqTestMx();
			mx.setDf(df);
			mx.setKf(kf);
			mx.setMbid(mbid);
			mxs.add(mx);
		}
		baseService.save(test);
		String id = test.getId();
		for(AqTestMx mx : mxs){
			mx.setPid(id);
			baseService.save(mx);
		}
		return new Result(true,"保存成功");
	}
	
	
}
