package com.cnpc.framework.base.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.base.dao.BaseDao;
import com.cnpc.framework.base.dao.RedisDao;
import com.cnpc.framework.base.entity.TaskWechat;
import com.cnpc.framework.base.entity.WechatConfig;
import com.cnpc.framework.base.pojo.AccessToken;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.CodeException;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.framework.utils.WeChatUtil;

/**
 *
 *
 */
@Service("baseService")
public class BaseServiceImpl implements BaseService {

    @Resource
    public BaseDao baseDao;

    @Resource
    public RedisDao redisDao;

    public <T> Serializable save(T obj) {

        return baseDao.save(obj);
    }

    public <T> void delete(T obj) {

        baseDao.delete(obj);
    }

    public <T> void update(T obj) {

        baseDao.update(obj);
    }

    public Object saveOrUpdate(Object obj) {

        return baseDao.saveOrUpdate(obj);
    }

    public <T> void batchSave(List<T> entityList) {

        baseDao.batchSave(entityList);
    }

    public <T> void batchUpdate(List<T> entityList) {

        baseDao.batchUpdate(entityList);
    }

    public <T> void batchDelete(List<T> entityList) {

        baseDao.batchDelete(entityList);
    }

    public <T> void batchSaveOrUpdate(List<T> entityList) {

        baseDao.batchSaveOrUpdate(entityList);
    }

    public <T> List<T> list(Class<T> clazz) {

        return baseDao.list(clazz);
    }

    public <T> T get(Class<T> clazz, Serializable id) {

        return baseDao.get(clazz, id);
    }

    public <T> T get(String hql) {

        return baseDao.get(hql);
    }

    public <T> T get(String hql, Map<String, Object> params) {

        return baseDao.get(hql, params);
    }

    public <T> List<T> find(String hql) {

        return baseDao.find(hql);
    }

    public <T> List<T> find(String hql, Map<String, Object> params) {

        return baseDao.find(hql, params);
    }

    public <T> List<T> find(String hql, int page, int rows) {

        return baseDao.find(hql, page, rows);
    }

    public <T> List<T> find(String hql, Map<String, Object> params, int page, int rows) {

        return baseDao.find(hql, params, page, rows);
    }

    public long count(String hql) {

        // return baseDao.count(hql);

        Long count = baseDao.count(hql);
        ;
        if (count == null) {
            return 0;
        }
        return count;

    }

    public Long count(String hql, Map<String, Object> params) {

        return baseDao.count(hql, params);
    }

    public int executeHql(String hql) {

        return baseDao.executeHql(hql);
    }

    public int executeHql(String hql, Map<String, Object> params) {
        return baseDao.executeHql(hql, params);
    }

    public <T> T getBySql(String sql) {

        return baseDao.getBySql(sql);
    }

    public <T> T getBySql(String sql, Map<String, Object> params) {

        return baseDao.getBySql(sql, params);
    }

    public <T> List<T> findBySql(String sql, Class<T> clazz) {

        return baseDao.findBySql(sql, clazz);
    }

    public <T> List<T> findBySql(String sql, Map<String, Object> params, Class<T> clazz) {

        return baseDao.findBySql(sql, params, clazz);
    }

    public <T> List<T> findBySql(String sql, int page, int rows, Class<T> clazz) {

        return baseDao.findBySql(sql, page, rows, clazz);
    }

    public <T> List<T> findBySql(String sql, Map<String, Object> params, int page, int rows, Class<T> clazz) {

        return baseDao.findBySql(sql, params, page, rows, clazz);
    }

    public List<Map<String, Object>> findMapBySql(String sql) {

        return baseDao.findMapBySql(sql);
    }

    public List<Map<String, Object>> findMapBySql(String sql, Map<String, Object> params) {

        return baseDao.findMapBySql(sql, params);
    }

    public List<Map<String, Object>> findMapBySql(String sql, int page, int rows) {

        return baseDao.findMapBySql(sql, page, rows);
    }

    public List<Map<String, Object>> findMapBySql(String sql, Map<String, Object> params, int page, int rows) {

        return baseDao.findMapBySql(sql, params, page, rows);
    }

    public List findMapBySql(String sql, Object[] params, Type[] types, Class clazz) {

        return baseDao.findMapBySql(sql, params, types, clazz);
    }

    public <T> List<T> find(String sql, Map<String, Object> params, Class<T> clazz) {
        return baseDao.find(sql, params, clazz);
    }

    public List findMapBySql(String sql, String countStr, PageInfo pageInfo, Object[] params, Type[] types, Class clazz) {

        if (StrUtil.isEmpty(countStr))
            countStr = "count(*)";
        String countSql = "select " + countStr + " from (" + sql + ") as table_alias";// .substring(sql.toLowerCase().indexOf("from"));
        int count = this.countBySql(countSql, params, types).intValue();
        pageInfo.setCount(count);
        return baseDao
                .findMapBySql(sql, pageInfo.getPageSize() * (pageInfo.getPageNum() - 1), pageInfo.getPageSize(), params, types, clazz);
    }

    public Long countBySql(String sql, Object[] params, Type[] types) {

        return baseDao.countBySql(sql, params, types);
    }

    public Long countBySql(String sql) {

        return baseDao.countBySql(sql);
    }

    public Long countBySql(String sql, Map<String, Object> params) {

        return baseDao.countBySql(sql, params);
    }

    public int executeSql(String sql) {

        return baseDao.executeSql(sql);
    }

    public int executeSql(String sql,Map<String,Object> params) {

        return baseDao.executeSql(sql,params);
    }

    public <T> List<T> getListByCriteria(DetachedCriteria criteria, PageInfo page) {

        return baseDao.getListByCriteria(criteria, page);
    }

    public List<?> getListByCriteria(DetachedCriteria criteria, Integer startPage, Integer pageSize) {

        return baseDao.getListByCriteria(criteria, startPage, pageSize);
    }

    public int getCountByCriteria(DetachedCriteria criteria) {

        return baseDao.getCountByCriteria(criteria);
    }

    public List findByExample(Object example) {

        return baseDao.findByExample(example);
    }

    public List findByExample(Object example, String condition, boolean enableLike) {

        return baseDao.findByExample(example, condition, enableLike);
    }

    public boolean isExist(String hql, Map<String, Object> param) {

        return count(hql, param) > 0;
    }

    public <T> List<T> findByCriteria(DetachedCriteria criteria) {

        return baseDao.findByCriteria(criteria);

    }

    public Object getMaxByExample(Object exampleEntity, String maxProperty, String condition, boolean enableLike) {
        return baseDao.getMaxByExample(exampleEntity, maxProperty, condition, enableLike);
    }


    //redis接口通用方法
    public void deleteCacheByKey(String key) {
        redisDao.delete(key);
    }

    public <T> boolean addCacheByKey(String key, T object) {
        return redisDao.add(key, object);
    }

    public <T> boolean saveCacheByKey(String key, T object) {
        return redisDao.save(key, object);
    }

    public String getCacheByKey(String key) {
        return redisDao.get(key);
    }

    public <T> T getCacheByKey(String key, Class clazz) {
        return redisDao.get(key, clazz);
    }

    public List findMapBySql(String sql, Map<String, Object> params, int page, int rows, Class clazz) {
        return baseDao.findMapBySql(sql, params, page, rows, clazz);
    }

	@Override
	public void sendSteamWeChat(String processDefinitionKey, String title, String desc) {
		String wechatHql = "from TaskWechat where processDefinitionKey=:processDefinitionKey";
        Map<String, Object> wechatParams = new HashMap<>();
        wechatParams.put("processDefinitionKey", processDefinitionKey);
        TaskWechat tw = this.get(wechatHql, wechatParams);
        
        if(tw == null){
            return;
        }
        
        WechatConfig wechatConfig = this.get(WechatConfig.class, tw.getWechatConfigID());
        
        
        try {
            // 获取Token
            AccessToken token = WeChatUtil.getAccessToken(wechatConfig.getSecret());
            // 组装消息
            String alarmString = getMessage(wechatConfig.getAgentID(), tw.getToParty(), title, desc);
            // 发送消息
            String postURL = PropertiesUtil.getValue("SENDMESSAGE_URL").replace("ACCESS_TOKEN", token.getToken());
            JSONObject responseJSON = WeChatUtil.httpRequest(postURL, "POST", alarmString);
        } catch (CodeException e) {
            e.printStackTrace();
        }
		
	}
	
	private String getMessage(String agentID, String party, String title, String desc){
        Map<String, Object> map = new HashMap<String, Object>();

        // 成员ID列表
        map.put("touser", "");
        // 部门ID列表，当touser为@all时忽略本参数
        map.put("toparty", party);
        // 标签ID列表，当touser为@all时忽略本参数
        map.put("totag", "");
        // 消息类型，固定为text
        map.put("msgtype", "textcard");
        // 企业应用ID
        map.put("agentid", agentID);

        HashMap<String, String> contentMap = new HashMap<String, String>();
        contentMap.put("title", title);
        contentMap.put("description", desc);
        contentMap.put("url", "URL");
        contentMap.put("btntxt", "以上");
        map.put("textcard", contentMap);

        // 消息是否保密，0：否，1：是
        map.put("safe", "0");

        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return json.toString();
    }

	@Override
	public String getWeChatDesc(String name, String taskName, String approved, String suggestion) {
        
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"gray\">");
        sb.append(year+"年"+month+"月"+day+"日");
        sb.append("</div> <div class=\"normal\">");
        sb.append(taskName);
        sb.append("</div><div class=\"highlight\">");
        sb.append("已经过【");
        sb.append(name);
        sb.append("】审批，操作人：");
        sb.append(SecurityUtil.getUser().getName());
        sb.append("</div><div class=\"normal\">");
        sb.append("审批结果：");
        sb.append(approved);
        sb.append("</div><div class=\"highlight\">");
        sb.append("审批意见：");
        sb.append(suggestion);
        sb.append("</div>");
        
        return sb.toString();
	}
}
