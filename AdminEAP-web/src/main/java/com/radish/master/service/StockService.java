package com.radish.master.service;


import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Project;
import com.radish.master.entity.*;

import java.util.List;


public interface StockService extends BaseService {

    Boolean saveHistory(String project_ID,String mat_ID,int stockChangeNum,String useTpye,String stockSource,String remark);
    Boolean stockChange(String project_ID,String mat_ID,int stockChangeNum,int changeType);
    Project getProjectByBudget(String budget_ID);
    Boolean saveChannel(String mat_ID,String project_ID,String channel_ID,int stockNum);
    List getStockChannelList(String mat_ID, String project_ID);
    StockChannel getStockChannel(String mat_ID,String project_ID,String channel_ID);

}
