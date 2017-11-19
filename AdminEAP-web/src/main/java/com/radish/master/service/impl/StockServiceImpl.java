package com.radish.master.service.impl;


import com.cnpc.framework.base.service.impl.BaseServiceImpl;

import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.Stock;
import com.radish.master.entity.StockChannel;
import com.radish.master.entity.StockHistory;
import com.radish.master.service.StockService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("stockService")
public class StockServiceImpl extends BaseServiceImpl implements StockService {


    @Override
    public Boolean saveHistory(String budget_no, String mat_ID, int stockChangeNum, String useTpye, String stockSource, String remark) {

        StockHistory tockHistory = new StockHistory();
        tockHistory.setProject_id(getProjectByBudget(budget_no).getProjectCode());
        tockHistory.setBudget_id(budget_no);
        tockHistory.setMat_id(mat_ID);
        tockHistory.setStock_change_num(stockChangeNum);
        tockHistory.setStock_Source(stockSource);//目前填入预算编号， 后期晚上采购单后为采购单编号
        tockHistory.setUsetpye(useTpye);//1：采购入库 2：调度入库 3：消耗出库  4：调度出库
        tockHistory.setOperation_person_id(SecurityUtil.getUserId());
        tockHistory.setOperation_time(new Date());
        save(tockHistory);
        return true;
    }

    @Override
    public Boolean stockChange(String project_ID, String mat_ID, int stockChangeNum,int changeType) {

        String sql = " select * from tbl_stock where mat_id='"+mat_ID+"' and project_ID='"+project_ID+"'";
        List<Stock> list= findBySql(sql, Stock.class);
        if(list.size()>0){
            Stock stock = list.get(0);
            if(changeType==1){
                stockChangeNum = stock.getStock_num()+stockChangeNum;
            }else if(changeType==2){
                stockChangeNum = stock.getStock_num()-stockChangeNum;
            }else{
                return false;
            }
            list.get(0).setStock_num(stockChangeNum);
            save(stock);
            return true;
        }
        return false;
    }

    @Override
    public Project getProjectByBudget(String budget_no){
        String sql = "select p.* from tbl_project p ,tbl_budget b where p.project_code = b.project_id and b.budget_no = '"+budget_no+"'";
        List<Project> list= findBySql(sql, Project.class);
        if(list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Boolean saveChannel(String mat_ID, String project_ID, String channel_ID, int stockNum) {
        StockChannel stockChannel = getStockChannel(mat_ID,project_ID,channel_ID);
        if(stockChannel==null){
            stockChannel = new StockChannel();
            stockChannel.setMat_id(mat_ID);
            stockChannel.setProject_id(project_ID);
            stockChannel.setChannel_id(channel_ID);
            stockChannel.setStock_num(stockNum);
            save(stockChannel);
        }else {
            stockChannel.setStock_num(stockChannel.getStock_num()+stockNum);
            update(stockChannel);
        }
        return true;
    }

    @Override
    public List<StockChannel> getStockChannelList(String mat_ID, String project_ID) {
        String sql = " select * from tbl_stock_channel where mat_id='" + mat_ID + "' and project_ID='" + project_ID + "'";
        List<StockChannel> list = findBySql(sql, StockChannel.class);
        if(list.size()>0){
            return list;
        }else {
            return  null;
        }
    }

    @Override
    public StockChannel getStockChannel(String mat_ID, String project_ID, String channel_ID) {
        String sql = " select * from tbl_stock_channel where mat_id='" + mat_ID + "' and project_ID='" + project_ID + "' and channel_id ='"+channel_ID+"'";
        List<StockChannel> list = findBySql(sql, StockChannel.class);
        if(list.size()>0){
            return list.get(0);
        }else {
            return  null;
        }
    }

}
