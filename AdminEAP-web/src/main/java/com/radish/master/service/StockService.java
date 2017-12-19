package com.radish.master.service;


import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Project;
import com.radish.master.entity.*;
import com.radish.master.pojo.Options;

import java.util.List;


public interface StockService extends BaseService {

    Boolean saveHistory(String project_ID,String mat_ID,Double stockChangeNum,String useTpye,String stockSource,String remark);
    Boolean stockChange(String project_ID,String mat_ID,Double stockChangeNum,int changeType,String useType);
    Project getProjectByBudget(String budget_ID);
    Project getProjectByPurchase(String purchase_ID);
    Boolean saveChannel(String mat_ID,String project_ID,String channel_ID,Double stockNum,int changeType);
    List getStockChannelList(String mat_ID, String project_ID);
    StockChannel getStockChannel(String mat_ID,String project_ID,String channel_ID);
    List<Options> getPurchaseCombobox();
    List<Options> getMatCombobox(String purchase_ID);

}
