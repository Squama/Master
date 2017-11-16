package com.radish.master.controller;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.*;
import com.radish.master.pojo.ResultObj;
import com.radish.master.service.ProjectService;
import com.radish.master.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Resource
    private BaseService baseService;
    @Resource
    private StockService stockService;

    @RefreshCSRFToken
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "stock/stockQuery_list";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/list_dispatch",method = RequestMethod.GET)
    public String dispatch(){
        return "stock/stockQuery_list_dispatch";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/list_out",method = RequestMethod.GET)
    public String out(){
        return "stock/stockQuery_list_out";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/list_history",method = RequestMethod.GET)
    public String stockHistory(){
        return "stock/stock_history_list";
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpServletRequest request){
        Stock stock = new Stock();
        String id = request.getParameter("id");
        String mat_id = request.getParameter("mat_id");
        String project_ID = request.getParameter("mat_project_id");
        String budget_ID = request.getParameter("budget_ID");
        String sql = " select * from tbl_stock where mat_id='"+mat_id+"' and project_ID='"+project_ID+"'";
        List<Stock> list= baseService.findBySql(sql, Stock.class);
        if(list.size()==0){//同一种物料在一个项目下无记录，做新增
            //新增库存记录（入库）
            stock.setProject_id(project_ID);
            stock.setMat_id(request.getParameter("mat_id"));
            stock.setStock_num( Integer.parseInt(request.getParameter("stock_Num")));
            stock.setUsetype("1");//1:采购入库，2：调度入库
            stock.setStorage_person_id(SecurityUtil.getUserId());
            stock.setStorage_time(new Date());
            baseService.save(stock);
        }else{
            //同一种物料在一个项目下原有库存，更新数量
            stock = baseService.get(Stock.class,list.get(0).getId());
            stock.setStock_num(stock.getStock_num()+Integer.parseInt(request.getParameter("stock_Num")));
            baseService.update(stock);
        }
        //新增历史库存记录
        stockService.saveHistory(budget_ID,request.getParameter("mat_id"),Integer.parseInt(request.getParameter("stock_Num")),"1",request.getParameter("budget_ID"),"");
        Result r = new Result();
        r.setSuccess(true);
        return r;
    }

    @VerifyCSRFToken
    @RequestMapping(value="/saveDispatch",method = RequestMethod.POST)
    @ResponseBody
    public Result saveDispatch(HttpServletRequest request){

        Stock stock = new Stock();
        String mbBudgetID = request.getParameter("mb_budget_id");
        String budgetID = request.getParameter("budget_no");
        String mbProject_ID =  stockService.getProjectByBudget(mbBudgetID).getProjectCode();
        String project_ID = stockService.getProjectByBudget(budgetID).getProjectCode();
        String mat_id = request.getParameter("mat_ID");
        String unit = request.getParameter("unit");
        String stockNum = request.getParameter("stock_Num");
        String matStandard = request.getParameter("mat_standard");

        //原库存减少
        stockService.stockChange(project_ID,mat_id,Integer.parseInt(stockNum),2);
        //目标库存增加
        stockService.stockChange(mbProject_ID,mat_id,Integer.parseInt(stockNum),1);
        stockService.saveHistory(budgetID,mat_id,Integer.parseInt(stockNum),"2",mbProject_ID,"");
        stockService.saveHistory(mbBudgetID,mat_id,Integer.parseInt(stockNum),"3",project_ID,"");
        Result r = new Result();
        r.setSuccess(true);
        return r;
    }



    @RequestMapping(value="/getMateriel",method = RequestMethod.POST)
    @ResponseBody
    public Result getMat(HttpServletRequest request){
        String id = request.getParameter("id");
        String sql = " select * from tbl_materiel where mat_number='"+id+"'";
        List<Materiel> list= baseService.findBySql(sql, Materiel.class);
        if(list.size()!= 0){
            return new Result(true,list.get(0),"获取成功");
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

    //验证budget_ID是否合法
    @RequestMapping(value="/validBudget",method = RequestMethod.POST)
    @ResponseBody
    public ResultObj validBudget(HttpServletRequest request){

        String id = request.getParameter("budget_ID");
        String sql = "select p.project_name,p.project_code from tbl_project p ,tbl_budget b where b.project_id = p.project_code and b.budget_no = '"+id+"'";
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
    public String add(){
        return "stock/stock_add";
    }



    @RefreshCSRFToken
    @RequestMapping(value="/dispatch",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "stock/stock_dispatch";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Object get(@PathVariable("id") String id ,HttpServletRequest request){
        String sql = "select s.project_ID, p.project_name, b.budget_no, s.stock_Num, s.mat_ID, m.mat_name, m.unit, m.mat_standard FROM tbl_stock s, tbl_materiel m, tbl_project p, tbl_budget b WHERE s.id = '"+ id+"' AND m.mat_number = s.mat_ID and s.project_ID = p.project_code and b.project_id = p.project_code";
        System.out.println(sql);
        List list= baseService.findMapBySql(sql);
        if(list.size()!=0){
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
           // Materiel m = baseService.getCacheByKey(id,Materiel.class);
            return new Result(true,m.getMat_name(),"获取成功");
        }else{
            return new Result(false,id,"获取查询索引失败");
        }
    }

    //库存历史查询



}
