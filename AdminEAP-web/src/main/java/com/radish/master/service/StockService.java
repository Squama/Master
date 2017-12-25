package com.radish.master.service;


import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Project;
import com.radish.master.entity.*;
import com.radish.master.pojo.Options;

import java.util.List;


public interface StockService extends BaseService {

    Boolean saveHistory(String project_ID,String mat_ID,Double stockChangeNum,String useTpye,String stockSource,String remark);
    Boolean stockChange(String project_ID,String mat_ID,Double stockChangeNum,int changeType,String useType);
    //采购单入库数量变更
    Boolean savePurchaseChange(String purchase_ID,String mat_ID,Double stockChangeNum,String stockType);
    Project getProjectByBudget(String budget_ID);
    Project getProjectByPurchase(String purchase_ID);
    Channel getChannelByID(String channel_ID);
    //库存变更
    Boolean saveChannel(String mat_ID,String project_ID,String channel_ID,Double stockNum,int changeType);
    Boolean saveChannelDispatch(String mat_ID,String project_ID,String mbProject_ID,Double stockNum);
    List<StockChannel> getStockChannelList(String mat_ID, String project_ID);
    StockChannel getStockChannel(String mat_ID,String project_ID,String channel_ID);
    PurchaseDet getPurchaseByID(String ID,String mat_id);
    List<Options> getPurchaseCombobox(String stockType);
    List<Options> getMatCombobox(String purchase_ID,String stockType);//stockType : 入库方式，1：采购入库，2：调度入库

}
