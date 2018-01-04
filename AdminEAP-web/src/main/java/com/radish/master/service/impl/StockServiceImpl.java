package com.radish.master.service.impl;


import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.*;
import com.radish.master.pojo.Options;
import com.radish.master.service.StockService;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;


@Service("stockService")
public class StockServiceImpl extends BaseServiceImpl implements StockService {


    @Override
    public Boolean saveHistory(String project_ID, String mat_ID, Double stockChangeNum, String useTpye, String stockSource, String remark) {

        StockHistory tockHistory = new StockHistory();
        tockHistory.setProject_id(project_ID);
        tockHistory.setMat_id(mat_ID);
        tockHistory.setStock_change_num(stockChangeNum);
        tockHistory.setOperation_bill_ID(stockSource);// 采购单/調度單/出庫單編號
        tockHistory.setUsetpye(useTpye);//1：采购入库 2：调度入库 3：消耗出库  4：调度出库
        tockHistory.setOperation_person_id(SecurityUtil.getUserId());
        tockHistory.setOperation_time(new Date());
        save(tockHistory);
        return true;
    }

    @Override
    public Boolean stockChange(String project_ID, String mat_ID, Double stockChangeNum,int changeType,String useType) {
        Stock stock =new Stock();
        String sql = " select * from tbl_stock where mat_id='"+mat_ID+"' and project_ID='"+project_ID+"'";
        List<Stock> list= findBySql(sql, Stock.class);
        if(list.size()>0){
            stock = list.get(0);
            if(changeType==1){
                stockChangeNum = stock.getStock_num()+stockChangeNum;
            }else if(changeType==2){
                stockChangeNum = stock.getStock_num()-stockChangeNum;
            }else{
                return false;
            }
            list.get(0).setStock_num(stockChangeNum);
            this.update(stock);
            return true;
        }else{
            stock.setProject_id(project_ID);
            stock.setMat_id(mat_ID);
            stock.setStock_num(stockChangeNum);
            stock.setUsetype(useType);//1:采购入库，2：调度入库
            stock.setStorage_person_id(SecurityUtil.getUserId());
            stock.setStorage_time(new Date());
            this.save(stock);
            return true;
        }
    }

    @Override
    public Boolean savePurchaseChange(String purchase_ID, String mat_ID, Double stockChangeNum,String stockType) {
        String sql = "select * from tbl_purchase_det where status = '50' and stock_type = '"+stockType+"' and purchase_id = '"+purchase_ID+"' and mat_number='"+mat_ID+"'; ";
        List<PurchaseDet> list= findBySql(sql, PurchaseDet.class);
        if(list.size()>0){
            PurchaseDet pd = list.get(0);
            if(pd.getSurplusQuantity()>=stockChangeNum){
                pd.setSurplusQuantity(pd.getSurplusQuantity() - stockChangeNum);
                if(pd.getSurplusQuantity()==0){
                    pd.setStatus("60");
                    list = getPurchaseDetList(purchase_ID);
                    if(list.size()==0){
                        Purchase p = getPurchaseByID(purchase_ID);
                        p.setStatus("60");
                        update(p);
                    }
                }
                update(pd);
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public PurchaseDet getPurchaseDetByID(String id,String mat_id){
        String sql = "select * from tbl_purchase_det where purchase_id = '"+id+"' and mat_number ='"+mat_id+"'";
        List<PurchaseDet> list= findBySql(sql,PurchaseDet.class);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
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
    public Project getProjectByPurchase(String purchase_ID){
        String sql = "select p.* from tbl_project p ,tbl_purchase pu where p.id = pu.project_id and pu.id =  '"+purchase_ID+"'";
        List<Project> list= findBySql(sql, Project.class);
        if(list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Purchase getPurchaseByID(String purchase_ID){
        String sql = "select * from tbl_purchase  where id = '"+purchase_ID+"'";
        List<Purchase> list= findBySql(sql, Purchase.class);
        if(list.size()>0) {
            return list.get(0);
        }
        return null;

    }

    @Override
    public Channel getChannelByID(String channel_ID){
        String sql = "select * from tbl_channel where id = '"+channel_ID+"'";
        List<Channel> list = findBySql(sql,Channel.class);
        if(list.size()>0){
            return  list.get(0);
        }
        return  null;
    }


    //调度库存渠道保存
    @Override
    public Boolean saveChannelDispatch(String mat_ID,String project_ID,String mbProject_ID,Double stockNum){
        List<StockChannel> cList = this.getStockChannelList(mat_ID,project_ID);
        StockChannel stockChannel = null;
        for(int i= 0;i<cList.size();i++){
            stockChannel = cList.get(i);
            if(stockChannel.getStock_num()>=stockNum){
                //目标库增加
                addChannel(mat_ID,mbProject_ID,stockChannel.getChannel_id(),stockNum);
                //当前库存减少的
                stockChannel.setStock_num(stockChannel.getStock_num()-stockNum);
                update(stockChannel);
                break;
            }else {
                addChannel(mat_ID,mbProject_ID,stockChannel.getChannel_id(),stockNum);
                stockNum = stockNum - stockChannel.getStock_num();
                stockChannel.setStock_num(0.0);
                update(stockChannel);
            }
        }
        return true;
    }

    //库存渠道增加
    private void addChannel(String mat_ID, String project_ID, String channel_ID,Double stockNum){
        StockChannel stockChannel = getStockChannel(mat_ID,project_ID,channel_ID);
        Double price = getChannelByID(channel_ID).getPrice();
        if(stockChannel==null){
            stockChannel = new StockChannel();
            stockChannel.setMat_id(mat_ID);
            stockChannel.setProject_id(project_ID);
            stockChannel.setChannel_id(channel_ID);
            stockChannel.setStock_num(stockNum);
            stockChannel.setPrice(price);
            save(stockChannel);
        }else{
            stockChannel.setStock_num(stockChannel.getStock_num()+stockNum);
            update(stockChannel);
        }
    }

    //库存渠道保存（进出库，不包含调度）
    @Override
    public Boolean saveChannel(String mat_ID, String project_ID, String channel_ID, Double stockNum,int changeType) {
        StockChannel stockChannel ;
        List<StockChannel> cList = this.getStockChannelList(mat_ID,project_ID);
        if(changeType == 1){
            addChannel(mat_ID,project_ID,channel_ID,stockNum);
        }else if(changeType==2){
            for(int i= 0;i<cList.size();i++){
                stockChannel = cList.get(i);
                if(stockChannel.getStock_num()>=stockNum){
                    stockChannel.setStock_num(stockChannel.getStock_num()-stockNum);
                    update(stockChannel);
                    break;
                }else {
                    stockNum = stockNum - stockChannel.getStock_num();
                    stockChannel.setStock_num(0.0);
                    update(stockChannel);
                }
            }
        }

        return true;
    }

    @Override
    public List<StockChannel> getStockChannelList(String mat_ID, String project_ID) {
        String sql = " select * from tbl_stock_channel where mat_id='" + mat_ID + "' and project_ID='" + project_ID + "' ORDER BY create_date_time ";
        List<StockChannel> list = findBySql(sql, StockChannel.class);
        if(list.size()>0){
            return list;
        }else {
            return  null;
        }
    }

    @Override
    public List<PurchaseDet> getPurchaseDetList(String purchaseID){
        String sql = " select * from tbl_purchase_det where status <> '60' and purchase_id='"+purchaseID+"'";
        List<PurchaseDet> list = findBySql(sql, PurchaseDet.class);
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

    @Override
    public List<Options> getPurchaseCombobox(String stockType) {
        String sql = "select p.id value, p.purchase_name data from tbl_purchase p , tbl_purchase_det pd where pd.purchase_id = p.id and p.status = '50' and pd.stock_type = '"+stockType+"' GROUP BY VALUE";
        return this.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getMatCombobox(String purchase_ID ,String stockType) {
        String sql = "select pud.mat_number value,m.mat_name data from tbl_purchase_det pud ,tbl_materiel m where  stock_type = '"+stockType+"' and pud.mat_number = m.mat_number and pud.surplus_quantity>0 and pud.purchase_id ='"+purchase_ID+"'";
        return this.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

}
