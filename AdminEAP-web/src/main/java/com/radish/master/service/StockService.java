package com.radish.master.service;


import com.cnpc.framework.base.pojo.Result;
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
    Boolean saveOneStock(String mat_id,String project_ID,String channel_ID,String purchase_ID,Double stockNum);
    //库存采购渠道变更操作
    Channel getChannelByID(String channel_ID);
    Boolean saveChannel(String mat_ID,String project_ID,String channel_ID,Double stockNum,int changeType);
    Boolean saveChannelDispatch(String mat_ID,String project_ID,String mbProject_ID,Double stockNum);
    List<StockChannel> getStockChannelList(String mat_ID, String project_ID);
    StockChannel getStockChannel(String mat_ID,String project_ID,String channel_ID);

    //查询搜索框
    List<Options> getPurchaseCombobox(String stockType);
    List<Options> getMatCombobox(String purchase_ID,String stockType);//stockType : 入库方式，1：采购入库，2：调度入库

    //物料申请/采购单操作
    List<PurchaseDet> getPurchaseDetList(String purchaseID);
    PurchaseDet getPurchaseDetByID(String ID,String mat_id);
    Project getProjectByPurchase(String purchase_ID);
    Purchase getPurchaseByID(String purchase_ID);

    //获取物料调度列表
    //参数：String projectCode：项目编号,String matNumber：物料编号,Double stockNum：调度数量
    List<StockChannel> getStockChannelFrozenList(String projectID,String matNumber,Double outNum);
    //库存解冻
    Boolean thawStockChannel(List<StockChannel> list);
    //库存冻结生效
    Boolean stockFrozenTakeEffect(String dispatchId);

    /**
		 * 调度单入库 lx=rk/出库lx=ck 
		 * @author 王志浩
		 * @创建时间 2018年1月7日 下午4:39:38
		 * @return
	*/
    Result doDispatch(String lx,String dispatchId);
}
