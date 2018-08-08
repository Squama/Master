package com.radish.master.controller;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.*;
import com.radish.master.pojo.Options;
import com.radish.master.pojo.ResultObj;
import com.radish.master.service.StockService;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Resource
    private BaseService baseService;
    @Resource
    private StockService stockService;


    @RefreshCSRFToken
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
    	List st = baseService.findMapBySql("select id value, id data from tbl_stock", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);

    	request.setAttribute("st", JSONArray.toJSONString(st));
    	request.setAttribute("xm", JSONArray.toJSONString(xm));
        return "stock/stockQuery_list";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/list_dispatch",method = RequestMethod.GET)
    public String dispatch(HttpServletRequest request){
    	List dd = baseService.findMapBySql("select d.id value, p.purchase_name data from tbl_dispatch d , tbl_purchase p where d.purchase_id = p.id and d.status='10'", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	
    	request.setAttribute("dd", JSONArray.toJSONString(dd));
    	request.setAttribute("xm", JSONArray.toJSONString(xm));
        return "stock/stockQuery_list_dispatch";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/list_dispatch_rk",method = RequestMethod.GET)
    public String dispatchRk(HttpServletRequest request){
    	List dd = baseService.findMapBySql("select d.id value, p.purchase_name data from tbl_dispatch d , tbl_purchase p where d.purchase_id = p.id and d.status='20'", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	
    	request.setAttribute("dd", JSONArray.toJSONString(dd));
    	request.setAttribute("xm", JSONArray.toJSONString(xm));
        return "stock/stockQuery_list_dispatchRk";
    }
    @RefreshCSRFToken
    @RequestMapping(value="/dispatchManage",method = RequestMethod.GET)
    public String dispatchManage(HttpServletRequest request){
    	List dd = baseService.findMapBySql("select d.id value, p.purchase_name data from tbl_dispatch d , tbl_purchase p where d.purchase_id = p.id ", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	
    	request.setAttribute("dd", JSONArray.toJSONString(dd));
    	request.setAttribute("xm", JSONArray.toJSONString(xm));
        return "stock/dispatchManage";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/list_out",method = RequestMethod.GET)
    public String out(HttpServletRequest request){
        List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
        List bg = baseService.findMapBySql("select id value, purchase_name data from tbl_purchase", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);

        request.setAttribute("xm", JSONArray.toJSONString(xm));
        request.setAttribute("bg", JSONArray.toJSONString(bg));
        return "stock/stockQuery_list_out";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/test",method = RequestMethod.GET)
    public String test(HttpServletRequest request){
        String kcid = request.getParameter("kcid");
        request.setAttribute("kcid", kcid);
        List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		
        return "stock/stockQuery_look";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/list_history",method = RequestMethod.GET)
    public String stockHistory( HttpServletRequest request){
    	List st = baseService.findMapBySql("select id value, id data from tbl_stock", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List xm = baseService.findMapBySql("select project_code value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List mat = baseService.findMapBySql("select mat_number value, mat_number data from tbl_materiel", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);

    	request.setAttribute("st", JSONArray.toJSONString(st));
    	request.setAttribute("xm", JSONArray.toJSONString(xm));
    	request.setAttribute("mat", JSONArray.toJSONString(mat));
        return "stock/stock_history_list";
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpServletRequest request){

        String mat_id = request.getParameter("mat_id");
        String project_ID = request.getParameter("project_id");
        String channel_ID = request.getParameter("channel_id");
        String purchase_ID = request.getParameter("purchase_id");
        String region_ID = request.getParameter("region_id");
        Double stockNum = Double.valueOf(request.getParameter("stock_Num")).doubleValue();
        stockService.saveOneStock(mat_id,project_ID,channel_ID,purchase_ID,stockNum,region_ID);
        /*Stock stock = new Stock();
        String sql = " select * from tbl_stock where mat_id='"+mat_id+"' and project_ID='"+project_ID+"'";
        List<Stock> list= baseService.findBySql(sql, Stock.class);
        if(list.size()==0){//同一种物料在一个项目下无记录，做新增
            //新增库存记录（入库）
            stock.setProject_id(project_ID);
            stock.setMat_id(mat_id);
            stock.setStock_num(stockNum);
            stock.setFrozen_num(0.0);
            stock.setAvailable_num(stockNum);
            stock.setUsetype("1");//1:采购入库，2：调度入库
            stock.setStorage_person_id(SecurityUtil.getUserId());
            stock.setStorage_time(new Date());
            baseService.save(stock);
        }else{
            //同一种物料在一个项目下原有库存，更新数量
            stock = baseService.get(Stock.class,list.get(0).getId());
            stock.setStock_num(stock.getStock_num() + stockNum);
            stock.setAvailable_num(stock.getAvailable_num()+stockNum);
            baseService.update(stock);
        }
        //同步库存渠道表
        stockService.saveChannel(mat_id ,project_ID,channel_ID,stockNum,1);
        //新增历史库存记录
        stockService.saveHistory(project_ID,mat_id,stockNum,"1",purchase_ID,"");
        //更新采购单余量
        stockService.savePurchaseChange(purchase_ID,mat_id,stockNum,"1");
*/
        Result r = new Result();
        r.setSuccess(true);
        return r;
    }

   /* @VerifyCSRFToken
    @RequestMapping(value="/saveDispatch",method = RequestMethod.POST)
    @ResponseBody
    public Result saveDispatch(HttpServletRequest request){

        String mat_id = request.getParameter("mat_ID");
        Double stockNum = Double.valueOf(request.getParameter("stock_Num")).doubleValue();
        String mbProject_ID = request.getParameter("mb_project_id");
        String dqProject_ID = request.getParameter("dq_project_id");
        String purchase_ID = request.getParameter("purchase_id");

        //原库存减少  //useType 1:采购入库，2：调度入库
        stockService.stockChange(dqProject_ID,mat_id,stockNum,2,"1");
        //目标库存增加
        stockService.stockChange(mbProject_ID,mat_id,stockNum,1,"2");
        //历史变更
        stockService.saveHistory(mbProject_ID,mat_id,stockNum,"4",purchase_ID,"");
        stockService.saveHistory(dqProject_ID,mat_id,stockNum,"2",purchase_ID,"");
        //库存渠道变更
        stockService.saveChannelDispatch(mat_id,dqProject_ID,mbProject_ID,stockNum);
        //更新采购单余量
        stockService.savePurchaseChange(purchase_ID,mat_id,stockNum,"2");
        Result r = new Result();
        r.setSuccess(true);
        return r;
    }*/

    @VerifyCSRFToken
    @RequestMapping(value="/saveOut",method = RequestMethod.POST)
    @ResponseBody
    public Result saveOut(String id,HttpServletRequest request){
        String purchaseID = id;
        PurchaseDet pd ;
        Purchase p = stockService.getPurchaseByID(purchaseID);
        List<PurchaseDet> list = stockService.getPurchaseDetList(purchaseID);
        for (int i = 0; i <list.size() ; i++) {
            pd = list.get(i);
            //原库存减少  //changeType 1:入库，2：出库
            stockService.stockChange(p.getProjectID(),pd.getMatNumber(),pd.getQuantity(),2,"1");
            //同步库存渠道表 1:入库，2：出库
            stockService.saveChannel(pd.getMatNumber(),p.getProjectID(),"",pd.getQuantity(),2);
            //历史变更
            stockService.saveHistory(purchaseID,pd.getMatNumber(),pd.getQuantity(),"3","stockOut","",0.0,pd.getChannelID());
        }
        //更新申请单明细状态
        String sql= "update tbl_purchase_det set status = '99' where purchase_id = '"+purchaseID+"'";
        String sql1="update tbl_purchase set status = '99' where id = '"+purchaseID+"'";
        baseService.executeSql(sql);
        baseService.executeSql(sql1);

        Result r = new Result(true,null,"获取成功");
        return r;
    }



    @RequestMapping(value="/getMateriel",method = RequestMethod.POST)
    @ResponseBody
    public Result getMat(HttpServletRequest request){
        String id = request.getParameter("mat_id");
        String sql = " select * from tbl_materiel where mat_number='"+id+"'";
        List<Materiel> list= baseService.findBySql(sql, Materiel.class);
        if(list.size()!= 0){
            return new Result(true,list.get(0),"获取成功");
        }else{
            return new Result(false,null,"获取查询索引失败");
        }
    }

    @RequestMapping(value="/getChannelByPurchase",method = RequestMethod.POST)
    @ResponseBody
    public Result getChannelByPurchase(HttpServletRequest request){
        String mat_id = request.getParameter("mat_id");
        String purchase_id = request.getParameter("purchase_id");
        String region_id = request.getParameter("region_id");
        String sql = " select * from tbl_purchase_det where stock_type='1' and region_id ='"+region_id+"' and mat_number ='"+mat_id+"' and purchase_id='"+purchase_id+"'";
        List list= baseService.findMapBySql(sql);
        if(list.size()!= 0){
            return new Result(true,list.get(0),"获取成功");
        }else{
            return new Result(false,null,"获取查询索引失败");
        }
    }

    @RequestMapping(value="/getProjectByPurchase",method = RequestMethod.POST)
    @ResponseBody
    public Result getProjectByPurchase(HttpServletRequest request){
        String id = request.getParameter("id");
        String mat_id = request.getParameter("mat_id");
        Project project = stockService.getProjectByPurchase(id);
        PurchaseDet pd = stockService.getPurchaseDetByID(id,mat_id);
        String matOptions = JSONArray.toJSONString(stockService.getMatCombobox(id,"1"));
        List<Object> list = new ArrayList<Object>();
        list.add(0,matOptions);
        list.add(1,project);
        list.add(2,pd);
        if(project!= null){
            return new Result(true,list,"获取成功");
        }else{
            return new Result(false,null,"获取查询索引失败");
        }
    }


    @RequestMapping(value="/getProjectByBudget",method = RequestMethod.POST)
    @ResponseBody
    public Result getProjectByBudget(HttpServletRequest request){
        String id = request.getParameter("id");
        String sql = "select p.project_name,p.project_code from tbl_project p ,tbl_budget b where b.project_id = p.project_code and b.budget_no = '"+id+"'";
        List list= baseService.findMapBySql(sql);
        if(list.size()!= 0){
            return new Result(true,list.get(0),"获取成功");
        }else{
            return new Result(false,null,"获取查询索引失败");
        }
    }

    @RequestMapping(value="/getPurchase",method = RequestMethod.POST)
    @ResponseBody
    public Result get(HttpServletRequest request){
        String id = request.getParameter("id");
        String sql = "select p.project_name,p.project_code from tbl_project p ,tbl_budget b where b.project_id = p.project_code and b.budget_no = '"+id+"'";
        List list= baseService.findMapBySql(sql);
        if(list.size()!= 0){
            return new Result(true,list.get(0),"获取成功");
        }else{
            return new Result(false,null,"获取查询索引失败");
        }
    }

    //验证budget_ID是否合法
    @RequestMapping(value="/validBudget",method = RequestMethod.POST)
    @ResponseBody
    public ResultObj validBudget(HttpServletRequest request){

        String id = request.getParameter("budget_ID");
        if(id == null){
            id = request.getParameter("mb_budget_id");
        }
        String sql = "select p.project_name,p.project_code from tbl_project p ,tbl_budget b where b.project_id = p.id and b.budget_no = '"+id+"'";
        List list= baseService.findMapBySql(sql);
        if(list.size()!= 0){
            return new ResultObj(true);
        }else{
            return new ResultObj(false);
        }
    }

    @RequestMapping(value="/validMateriel",method = RequestMethod.POST)
    @ResponseBody
    public ResultObj validMateriel(HttpServletRequest request){

        String id = request.getParameter("mat_id");
        String sql = "select * from tbl_materiel where mat_number  = '"+id+"'";
        List list= baseService.findMapBySql(sql);
        if(list.size()!= 0){
            return new ResultObj(true);
        }else{
            return new ResultObj(false);
        }
    }

    @RequestMapping(value="/validStockNum",method = RequestMethod.POST)
    @ResponseBody
    public ResultObj validStockNum(HttpServletRequest request){

        String num = request.getParameter("stock_Num");
        String sql = "select * from tbl_materiel where mat_number  = '"+num+"'";
        List list= baseService.findMapBySql(sql);
        if(list.size()!= 0){
            return new ResultObj(true);
        }else{
            return new ResultObj(false);
        }
    }


    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        //request.setAttribute("purchaseOptions", JSONArray.toJSONString(stockService.getPurchaseCombobox("1")));
        String sql ="select p.id value, p.purchase_name data from tbl_purchase p , tbl_purchase_det pd where pd.purchase_id = p.id and p.status = '50' and pd.surplus_quantity >0 and pd.stock_type = '1' GROUP BY VALUE";
        List purchaseOptions = baseService.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
        request.setAttribute("purchaseOptions", JSONArray.toJSONString(purchaseOptions));

        return "stock/stock_add";
    }
    /**
		 * 物料库存调度跳转页面
		 * @author 王志浩
		 * @创建时间 2018年1月7日 下午2:37:07
		 * @return
		 */
    @RefreshCSRFToken
    @RequestMapping(value="/dispatch",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("dispatchId", id);
        String lx = request.getParameter("lx");
        request.setAttribute("lx", lx);
        Dispatch d = baseService.get(Dispatch.class, id);
        Project ly = baseService.get(Project.class,d.getSourceProjectID());
        Project mb = baseService.get(Project.class, d.getTargetProjectID());
        Purchase p = baseService.get(Purchase.class, d.getPurchaseID());
        request.setAttribute("ly", ly.getProjectName());
        request.setAttribute("mb", mb.getProjectName());
        request.setAttribute("pname",p.getPurchaseName());
        return "stock/stock_dispatch";
    }
    /**
		 * 调度单入库 lx=rk/出库lx=ck 
		 * @author 王志浩
		 * @创建时间 2018年1月7日 下午4:35:36
		 * @return
		 */
    @RequestMapping("/doDispatch")
    @ResponseBody
    public Result doDispatch(HttpServletRequest request){
    	String lx = request.getParameter("lx");//调度类型
    	String dispatchId = request.getParameter("dispatchId");//调度单id
    	Result r = stockService.doDispatch(lx, dispatchId);
    	return r;
    }
    
    
    @RefreshCSRFToken
    @RequestMapping(value="/out",method = RequestMethod.GET)
    public String out(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "stock/stock_out";
    }

    //入库选择物料渠道,从渠道表中找
    @RefreshCSRFToken
    @RequestMapping(value="/getChannel",method = RequestMethod.GET)
    public String getChannel(String mat_id,HttpServletRequest request){
        request.setAttribute("mat_id", mat_id);
        return "stock/chooseChannel";
    }

    //调度选择物料渠道，从库存渠道表中找
    @RefreshCSRFToken
    @RequestMapping(value="/getStockChannel",method = RequestMethod.GET)
    public String getStockChannel(String mat_id,String project_id,HttpServletRequest request){
        request.setAttribute("mat_id", mat_id);
        request.setAttribute("project_id", project_id);
        return "stock/chooseStockChannel";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Object get(@PathVariable("id") String id ,HttpServletRequest request){
        String sql = "select s.project_ID, p.project_name, s.stock_Num, s.mat_ID, m.mat_name, m.unit, m.mat_standard FROM tbl_stock s, tbl_materiel m, tbl_project p WHERE s.id = '"+id+"' AND m.mat_number = s.mat_ID and s.project_ID = p.project_code";
        List<Map<String, Object>> list = baseService.findMapBySql(sql);
        //获取采购单价，完成组装
        if(list.size()!=0){
            String mat_ID =list.get(0).get("mat_ID").toString();
            String project_ID  = list.get(0).get("project_ID").toString();
            String outStr ="";
            List<StockChannel> SCList = stockService.getStockChannelList(mat_ID,project_ID);
            for(int i=0;i<SCList.size();i++){
                outStr += SCList.get(i).getPrice().toString()+"*"+SCList.get(i).getStock_num().toString();
                if(i!=SCList.size()-1){
                    outStr = outStr+", ";
                }
            }
            list.get(0).put("price",outStr);
            return list.get(0);
        }else {
            return new Result(false,id,"获取查询索引失败");
        }
    }

    @RequestMapping(value="/getProject",method = RequestMethod.POST)
    @ResponseBody
    public Result getProject(HttpServletRequest request){
        String id = request.getParameter("mb_budget_id");
        Project p = baseService.get(Project.class, id);
        Result r = new Result();
        r.setData(p);
        return r;
    }

    //管理显示所属项目信息
    @RequestMapping(value = "/showProject/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result showProject(@PathVariable("id") String id) {
       if(id!= null){
           Project p = baseService.get(Project.class, id);
           return new Result(true,p.getProjectName(),"获取成功");
       }else{
           return new Result(false,id,"获取查询索引失败");
       }
    }

    //管理显示物料信息
    @RequestMapping(value = "/showMat/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result showMat(@PathVariable("id") String id) {
        if(id!= null){
            Materiel m = baseService.get(Materiel.class, id);
            return new Result(true,m.getMat_name(),"获取成功");
        }else{
            return new Result(false,id,"获取查询索引失败");
        }
    }
  //初始化库存物料页面
    @RequestMapping("/showStockMat")
    public String showStockMat(HttpServletRequest request) {
    	List st = baseService.findMapBySql("select id value, id data from tbl_stock", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List xm = baseService.findMapBySql("select project_code value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	request.setAttribute("xm", JSONArray.toJSONString(xm));
    	request.setAttribute("st", JSONArray.toJSONString(st));
        return "stock/stockAdd_list";
    }
  //初始化库存物料弹框
    @RequestMapping("/addMat")
    public String addMat(HttpServletRequest request) {
    	List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    	List mat = baseService.findMapBySql("select id value, mat_name data from tbl_materiel", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);

    	request.setAttribute("xm", JSONArray.toJSONString(xm));
    	request.setAttribute("mat", JSONArray.toJSONString(mat));
        return "stock/stock_stockAddMat_Edit";
    }
    //获取渠道信息
    @RequestMapping("/getQdInfo")
    @ResponseBody
    public Map<String,Object> getQdInfo(HttpServletRequest request){
    	String matid = request.getParameter("matid");
    	Materiel m = baseService.get(Materiel.class, matid);
    	List<Channel> cl = baseService.findBySql("select * from tbl_channel where mat_ID='"+m.getMat_number()+"' and isValid='1'", Channel.class);
    	Map<String ,Object> map = new HashMap<String,Object>();
    	map.put("mat", m);
    	map.put("cl", cl);
    	return map;
    }
    //获取部位列表
    @RequestMapping("/getRegionInfo")
    @ResponseBody
    public Map<String,Object> getRegionInfo(HttpServletRequest request){
        String mat_id = request.getParameter("mat_id");
        String purchase_id = request.getParameter("purchase_id");
        List<PurchaseDet> region = baseService.findBySql("select * from tbl_purchase_det where purchase_id = '"+purchase_id+"' and mat_number = '"+mat_id+"' and stock_type=1 and status = '50'; ", PurchaseDet.class);
        Map<String ,Object> map = new HashMap<String,Object>();
        map.put("region", region);
        return map;
    }

    //物料初始化入库
    @RequestMapping("/addCshMat")
    @ResponseBody
    public Result addCshMat(HttpServletRequest request,Stock s){
    	Materiel m = baseService.get(Materiel.class, s.getMat_id());
    	stockService.initializationStock(m.getMat_number(), s.getProject_id(), request.getParameter("channel_id"), s.getStock_num(),s.getRemark());
    	return new Result();
    }
    /**
		 * 跳转新增物料框
		 * @author wangzhihao
		 * @创建时间 2018年2月28日 下午7:53:34
		 * @return
		 */
    @RequestMapping("/addMatIndex")
    public String addMatIndex(HttpServletRequest request){
    	return "stock/addMat";
    }
    /**
		 * 获取库存渠道详情
		 * @author wangzhihao
		 * @创建时间 2018年4月23日 上午11:18:35
		 * @return
		 */
    @RequestMapping("/getstockDet")
    @ResponseBody
    public Map<String,Object> getstockDet(HttpServletRequest request){
    	Map<String,Object> m = new HashMap<String,Object>();
    	
    	String kcid = request.getParameter("kcid");
    	Stock kc = baseService.get(Stock.class, kcid);
    	
    	List<Materiel> wl = baseService.findBySql(" select * from tbl_materiel where mat_number='"+kc.getMat_id()+"'", Materiel.class); 
    	if(wl.size()>0){
    		m.put("projectId", kc.getProject_id());
    		m.put("matName", wl.get(0).getMat_name());
    		m.put("available_num", kc.getAvailable_num());
    		m.put("matNumber", wl.get(0).getMat_number());
    		m.put("matStandard", wl.get(0).getMat_number());
    		m.put("unit", wl.get(0).getUnit());
    		m.put("success", true);
    	}else{
    		m.put("success", false);
    	}
    	return m;
    }
}
