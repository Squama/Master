package com.radish.master.service.impl;


import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.*;
import com.radish.master.pojo.Options;
import com.radish.master.service.StockService;
import com.radish.master.system.Arith;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("stockService")
public class StockServiceImpl extends BaseServiceImpl implements StockService {

    Arith arith = new Arith();

    /**
     * 新增库存操作日志
     * project_ID 调度单id
     * mat_ID 物料编号
     * stockChangeNum 变化数量
     * useTpye 使用类型1：采购入库 2：调度入库 3：消耗出库  4：调度出库
     * stockSource 库存变化来源:采购单/調度單/出库单
     * remark
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */
    @Override
    public Boolean saveHistory(String project_ID, String mat_ID, Double stockChangeNum, String useTpye, String stockSource, String remark,Double price) {
        StockHistory tockHistory = new StockHistory();
        tockHistory.setProject_id(project_ID);
        tockHistory.setMat_id(mat_ID);
        tockHistory.setStock_change_num(stockChangeNum);
        tockHistory.setOperation_bill_ID(stockSource); //.set/ 采购单/調度單/出庫單編號
        tockHistory.setStockSource(stockSource);//库存操作来源
        tockHistory.setUsetpye(useTpye);//1：采购入库 2：调度入库 3：消耗出库  4：调度出库  5：初始库存
        tockHistory.setOperation_person_id(SecurityUtil.getUserId());
        tockHistory.setOperation_time(new Date());
        tockHistory.setPrice(price);
        save(tockHistory);
        return true;
    }

    /**
     * 单个物料采购入库
     * project_ID 调度单id
     * mat_id 物料编号
     * channel_ID 采购渠道编号
     * stockNum 变化数量
     * stockSource 采购单编号
     * remark
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */
    @Override
    public Boolean saveOneStock(String mat_id,String project_ID,String channel_ID,String purchase_ID,Double stockNum,String region_ID){
        Stock stock = new Stock();
        String sql = " select * from tbl_stock where mat_id='"+mat_id+"' and project_ID='"+project_ID+"'";
        List<Stock> list= findBySql(sql, Stock.class);
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
            save(stock);
        }else{
            //同一种物料在一个项目下原有库存，更新数量
            stock = get(Stock.class,list.get(0).getId());
            stock.setStock_num(arith.add(stock.getStock_num() ,stockNum));
            stock.setAvailable_num(arith.add(stock.getAvailable_num(),stockNum));
            update(stock);
        }
        //同步库存渠道表
        saveChannel(mat_id ,project_ID,channel_ID,stockNum,1);
        //新增历史库存记录
        saveHistory(project_ID,mat_id,stockNum,"1",purchase_ID,"",0.0);
        //更新采购单余量
        savePurchaseChange(purchase_ID,mat_id,stockNum,"1", region_ID);
        return true;
    }

    /**
     * 初始化库存入库
     * project_ID 调度单id
     * mat_id 物料编号
     * channel_ID 采购渠道编号
     * stockNum 变化数量
     * stockSource 采购单编号
     * remark
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */
    @Override
    public Boolean initializationStock(String mat_id,String project_ID,String channel_ID,Double stockNum,String remark){
        Stock stock = new Stock();
        String sql = " select * from tbl_stock where mat_id='"+mat_id+"' and project_ID='"+project_ID+"'";
        List<Stock> list= findBySql(sql, Stock.class);
        if(list.size()==0){//同一种物料在一个项目下无记录，做新增
            //新增库存记录（入库）
            stock.setProject_id(project_ID);
            stock.setMat_id(mat_id);
            stock.setStock_num(stockNum);
            stock.setFrozen_num(0.0);
            stock.setAvailable_num(stockNum);
            stock.setUsetype("5");//1:采购入库，2：调度入库
            stock.setStorage_person_id(SecurityUtil.getUserId());
            stock.setStorage_time(new Date());
            stock.setRemark(remark);
            save(stock);
        }else{
            //同一种物料在一个项目下原有库存，更新数量
            stock = get(Stock.class,list.get(0).getId());
            stock.setStock_num(arith.add(stock.getStock_num() ,stockNum));
            stock.setAvailable_num(arith.add(stock.getAvailable_num(),stockNum));
            stock.setRemark(remark);
            update(stock);
        }
        //同步库存渠道表
        saveChannel(mat_id ,project_ID,channel_ID,stockNum,1);
        Double price = getChannelByID(channel_ID).getPrice();
        //新增历史库存记录
        saveHistory(project_ID,mat_id,stockNum,"5","","",price);
        return true;
    }



    /**
     * 库存总表变化
     * project_ID 调度单id
     * mat_id 物料编号
     * stockChangeNum 变化数量
     * useType 使用类型：1：采购入库，2：调度入库
     * changeType 变化类型1：增加库存 ，2：减少库存
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */
    @Override
    public Boolean stockChange(String project_ID, String mat_ID, Double stockChangeNum,int changeType,String useType) {
        Stock stock =new Stock();
        String sql = " select * from tbl_stock where mat_id='"+mat_ID+"' and project_ID='"+project_ID+"'";
        List<Stock> list= findBySql(sql, Stock.class);
        if(list.size()>0){
            stock = list.get(0);
            if(changeType==1){
                stockChangeNum = arith.add(stock.getStock_num(),stockChangeNum);
                stock.setAvailable_num(arith.add(stock.getStock_num(),stockChangeNum));
            }else if(changeType==2){
                stockChangeNum = arith.sub(stock.getStock_num(),stockChangeNum);
                stock.setAvailable_num(arith.sub(stock.getStock_num(),stockChangeNum));
            }else{
                return false;
            }
            stock.setStock_num(stockChangeNum);
            this.update(stock);
            return true;
        }else{
            stock.setProject_id(project_ID);
            stock.setMat_id(mat_ID);
            stock.setStock_num(stockChangeNum);
            stock.setAvailable_num(stockChangeNum);
            stock.setFrozen_num(0.0);
            stock.setUsetype(useType);//1:采购入库，2：调度入库
            stock.setStorage_person_id(SecurityUtil.getUserId());
            stock.setStorage_time(new Date());
            this.save(stock);
            return true;
        }
    }
    /**
     * 采购单入库更新（部分入库）
     * purchase_ID 采购单id
     * mat_id 物料编号
     * stockChangeNum 变化数量
     * stockType 变化类型1：增加库存 ，2：减少库存
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */
    @Override
    public Boolean savePurchaseChange(String purchase_ID, String mat_ID, Double stockChangeNum,String stockType,String region_ID) {
        String sql = "select * from tbl_purchase_det where status = '50' and stock_type = '"+stockType+"' and purchase_id = '"+purchase_ID+"' and mat_number='"+mat_ID+"' and region_id = '"+region_ID+"';";
        List<PurchaseDet> list= findBySql(sql, PurchaseDet.class);
        if(list.size()>0){
            PurchaseDet pd = list.get(0);
            if(pd.getSurplusQuantity()>=stockChangeNum){
                pd.setSurplusQuantity(arith.sub(pd.getSurplusQuantity() ,stockChangeNum));
                if(pd.getSurplusQuantity()<=0.0){
                    pd.setStatus("60");
                    update(pd);
                    list = getPurchaseDetList(purchase_ID);
                    if(list==null){
                        Purchase p = getPurchaseByID(purchase_ID);
                        p.setStatus("60");
                        update(p);
                    }
                }
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
                stockChannel.setStock_num(arith.sub(stockChannel.getStock_num(),stockNum));
                update(stockChannel);
                break;
            }else {
                addChannel(mat_ID,mbProject_ID,stockChannel.getChannel_id(),stockNum);
                stockNum = arith.sub(stockNum , stockChannel.getStock_num());
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
            stockChannel.setAvailable_num(stockNum);
            stockChannel.setPrice(price);
            stockChannel.setFrozen_num(0.0);
            save(stockChannel);
        }else{
            stockChannel.setStock_num(arith.add(stockChannel.getStock_num(),stockNum));
            stockChannel.setAvailable_num(arith.add(stockChannel.getAvailable_num(),stockNum));
            update(stockChannel);
        }
    }

    //库存渠道保存（进出库，不包含调度）
    @Override
    public Boolean saveChannel(String mat_ID, String project_ID, String channel_ID, Double stockNum,int changeType) {
        StockChannel stockChannel ;
        List<StockChannel> cList = this.getStockChannelList(mat_ID,project_ID);//按时间排序
        if(changeType == 1){
            addChannel(mat_ID,project_ID,channel_ID,stockNum);
        }else if(changeType==2){
            for(int i= 0;i<cList.size();i++){
                stockChannel = cList.get(i);
                if(stockChannel.getStock_num()>=stockNum){
                    stockChannel.setStock_num(arith.sub(stockChannel.getStock_num(),stockNum));
                    stockChannel.setAvailable_num(arith.sub(stockChannel.getAvailable_num(),stockNum));
                    update(stockChannel);
                    break;
                }else {
                    stockNum = arith.sub(stockNum , stockChannel.getStock_num());
                    stockChannel.setStock_num(0.0);
                    stockChannel.setAvailable_num(0.0);
                    update(stockChannel);
                }
            }
        }
        return true;
    }

    @Override
    public List<StockChannel> getStockChannelList(String mat_ID, String project_ID) {
        String sql = "select * from tbl_stock_channel where mat_id='" + mat_ID + "' and project_ID='" + project_ID + "' ORDER BY create_date_time ";
        List<StockChannel> list = findBySql(sql, StockChannel.class);
        return list;
    }

    @Override
    public List<PurchaseDet> getPurchaseDetList(String purchaseID){
        String sql = " select * from tbl_purchase_det  where status <> '60' and purchase_id='"+purchaseID+"'";
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
        String sql = "select pud.mat_number value,m.mat_name data from tbl_purchase_det pud ,tbl_materiel m where  stock_type = '"+stockType+"' and pud.mat_number = m.mat_number and pud.surplus_quantity>0 and pud.purchase_id ='"+purchase_ID+"' group by pud.mat_number";
        return this.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    /**
     * 调度单生成物料冻结
     * projectID 项目库id
     * matNumber 物料编号
     * outNum 冻结数量
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */

    @Override
    public  List<StockChannel> getStockChannelFrozenList(String projectID,String matNumber,Double outNum){
        Double totalOut = outNum;
        List<StockChannel> cList = getStockChannelList(matNumber,projectID);//按入库时间排序
        List<StockChannel> oList = new ArrayList<StockChannel>();
        StockChannel stockChannel = new StockChannel();
        for(int i= 0;i<cList.size();i++){
            Double totalFrozen_num;
            stockChannel = cList.get(i);
            if(stockChannel.getAvailable_num()>=outNum){
                totalFrozen_num = arith.add(stockChannel.getFrozen_num(),outNum);//库存累计冻结
                stockChannel.setFrozen_num(outNum);
                stockChannel.setAvailable_num(arith.sub(stockChannel.getAvailable_num(),outNum));
                oList.add(stockChannel);
                //更新数据库，冻结量累加，可用量扣除
                stockChannel.setFrozen_num(totalFrozen_num);
                update(stockChannel);
                break;
            }else{
                outNum = arith.sub(outNum ,stockChannel.getAvailable_num());
                totalFrozen_num = stockChannel.getStock_num();
                stockChannel.setFrozen_num(stockChannel.getAvailable_num());
                stockChannel.setAvailable_num(0.0);
                oList.add(stockChannel);
                //库存累计冻结
                stockChannel.setFrozen_num(totalFrozen_num);
                update(stockChannel);
            }
        }
        //总库总表更新
        String sql = " select * from tbl_stock where mat_id='" + matNumber + "' and project_ID='" + projectID + "'";
        List<Stock> list = findBySql(sql, Stock.class);
        Stock stock = list.get(0);
        stock.setFrozen_num(arith.add(stock.getFrozen_num(),totalOut));
        stock.setAvailable_num(arith.sub(stock.getAvailable_num(),totalOut));
        update(stock);
        return oList;
    }
    /**
     * 调度单生解冻
     * List<StockChannel> 解冻库存渠道列表
     * getFrozen_num，getMat_id，getProject_id，getChannel_id等信息为必要信息
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */
    @Override
    public Boolean thawStockChannel(List<StockChannel> list){
        for (int i = 0; i < list.size(); i++) {
            StockChannel newStockChannel = list.get(i);
            Double frozen_num = newStockChannel.getFrozen_num();
            String mat_id = newStockChannel.getMat_id();
            String project_id = newStockChannel.getProject_id();
            String channel_id = newStockChannel.getChannel_id();
            StockChannel oldStockChannel = getStockChannel(mat_id,project_id,channel_id);
            oldStockChannel.setFrozen_num(arith.sub(oldStockChannel.getFrozen_num(),frozen_num));
            oldStockChannel.setAvailable_num(arith.add(oldStockChannel.getAvailable_num(),frozen_num));
            update(oldStockChannel);
            //总库总表更新
            String sql = " select * from tbl_stock where mat_id='" + mat_id + "' and project_ID='" + project_id + "'";
            List<Stock> slist = findBySql(sql, Stock.class);
            Stock stock = slist.get(0);
            stock.setFrozen_num(arith.sub(stock.getFrozen_num(),frozen_num));
            stock.setAvailable_num(arith.add(stock.getAvailable_num(),frozen_num));
            update(stock);
        }
        return true;
    }

    /**
     * 调度冻结生效
     * List<StockChannel> 解冻库存渠道列表
     * getFrozen_num，getMat_id，getProject_id，getChannel_id等信息为必要信息
     * @author 周庆博
     * @创建时间 2018年1月7日 下午4:42:33
     * @return
     */
    public  Boolean stockFrozenTakeEffect(String dispatchId){
        Dispatch dispatch = baseDao.get(Dispatch.class, dispatchId);

        //调度单明细list
        String sql = " select * from tbl_dispatch_detail where dispatch_id ='"+dispatchId+"'";
        List<DispatchDetail> mxList = findBySql(sql, DispatchDetail.class);
        //来源库id
        String sourceProjectID = dispatch.getSourceProjectID();
        Project sp = baseDao.get(Project.class, sourceProjectID);
        //目标库id
        String targetProjectID = dispatch.getTargetProjectID();
        Project tp = baseDao.get(Project.class, targetProjectID);
        //遍历明细，执行相关入库出库操作操作
        for (int i = 0; i < mxList.size(); i++) {
            DispatchDetail dispatchDetail = mxList.get(i);
            String channelID = dispatchDetail.getChannelID();
            String matID = dispatchDetail.getMatNumber();
            Double quantity = dispatchDetail.getQuantity();
            //原库存冻结生效stock/stockChannel
            sql = " select * from tbl_stock where mat_id='"+matID+"' and project_ID='"+sourceProjectID+"'";
            Stock stock;
            List<Stock> stockList = findBySql(sql ,Stock.class);
            if(stockList!=null){
                stock = stockList.get(0);
                stock.setFrozen_num(arith.sub(stock.getFrozen_num(),quantity));
                stock.setStock_num(arith.sub(stock.getStock_num(),quantity));
                update(stock);
            }
            StockChannel stockChannel = getStockChannel(matID,sourceProjectID,channelID);
            stockChannel.setFrozen_num(arith.sub(stockChannel.getFrozen_num(),quantity));
            stockChannel.setStock_num(arith.sub(stockChannel.getStock_num(),quantity));
            update(stockChannel);
            saveHistory(sourceProjectID,matID,quantity,"4",tp.getProjectName(),"",0.0);

            //目标库存入库
            stockChange(targetProjectID, matID,quantity, 1, "2");
            saveChannel(matID,targetProjectID,channelID,quantity,1);
            saveHistory(targetProjectID,matID,quantity,"2",sp.getProjectName(),"",0.0);
        }
        dispatch.setStatus("60");//调度完成，调度单状态更新为已完成
        dispatch.setUpdateDateTime(new Date());
        update(dispatch);
        return true;
    }
    /**
		 * 调度单入库 lx=rk/出库lx=ck 
		 * dispatchId 调度单id
		 * @author 王志浩
		 * @创建时间 2018年1月7日 下午4:42:33
		 * @return
		 */
    public Result doDispatch(String lx,String dispatchId){
    	Dispatch d = baseDao.get(Dispatch.class, dispatchId);

    	if(lx.equals("ck")){
            d.setStatus("20");//更新调度单状态：已出库
            d.setCreateDateTime(new Date());
            update(d);
        }else if(lx.equals("rk")){
    	    //调度单冻结生效
            stockFrozenTakeEffect(dispatchId);
            //申请单信息
            String purchase_id = d.getPurchaseID();
            Purchase p = baseDao.get(Purchase.class,purchase_id);
            p.setStatus("50");//更新申请单状态：已入库
            update(p);
        }
        Result r = new Result(true,null,"获取成功");
        return r;
    }
}
