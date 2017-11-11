package com.radish.master.controller;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Project;
import com.radish.master.entity.Stock;
import com.radish.master.entity.StockHistory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Resource
    private BaseService baseService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "stock/stockQuery_list";
    }


    @VerifyCSRFToken
    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpServletRequest request){
        //新增库存记录（入库）
        Stock stock = new Stock();
        stock.setBudget_id(request.getParameter("budget_ID"));
        stock.setProject_id(request.getParameter("project_ID"));
        stock.setMat_id(request.getParameter("mat_number"));
        stock.setStock_num( Integer.parseInt(request.getParameter("stock_Num")));
        stock.setUsetype("1");//1:采购入库，2：调度入库
        stock.setStorage_person_id(SecurityUtil.getUserId());
        stock.setStorage_time(new Date());
        String id = (String)baseService.save(stock);
        //新增历史库存记录

        StockHistory tockHistory = new StockHistory();
        tockHistory.setProject_id(request.getParameter("project_ID"));
        tockHistory.setMat_id(request.getParameter("mat_number"));
        tockHistory.setStock_change_num( Integer.parseInt(request.getParameter("stock_Num")));
        tockHistory.setUsetpye("1");//1:采购入库，2：调度变更，3.消耗出库
        tockHistory.setOperation_person_id(SecurityUtil.getUserId());
        tockHistory.setOperation_time(new Date());
        String id2 = (String)baseService.save(tockHistory);

        Result r = new Result();
        r.setSuccess(true);
        return r;
    }

    @RequestMapping(value="/getMateriel",method = RequestMethod.POST)
    @ResponseBody
    public Result getMat(HttpServletRequest request){
        List<Materiel> m = new ArrayList<Materiel>();
        String id = request.getParameter("id");
        String sql = " select * from tbl_materiel where id='"+id+"'";
        List<Materiel> list= baseService.findBySql(sql, Materiel.class);
        Result r = new Result();
        r.setData(list);
        return r;
    }


    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "stock/stock_add";
    }


    @RefreshCSRFToken
    @RequestMapping(value="/list_dispatch",method = RequestMethod.GET)
    public String dispatch(){
        return "stock/stockQuery_list_dispatch";
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
        String sql = "select s.project_ID, s.stock_Num, s.mat_ID, m.mat_name, m.unit ,m.mat_standard from tbl_stock s , tbl_materiel m where s.id ='"+ id+"' and m.id = s.mat_ID";
        List list= baseService.findMapBySql(sql);
        return list.get(0);
    }

    @RequestMapping(value="/getProject",method = RequestMethod.POST)
    @ResponseBody
    public Result getProject(HttpServletRequest request){
        String id = request.getParameter("mb_budget_id");
        System.out.println("mb_budget_id ... : "+id);
        Project p = baseService.get(Project.class, id);
        Result r = new Result();
        r.setData(p);
        return r;
    }
}
