package com.radish.master.service.impl;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.framework.utils.TreeUtil;
import com.radish.master.entity.qualityCheck.CheckItem;
import com.radish.master.service.CheckItemService;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("checkItemService")
public class CheckItemImpl extends BaseServiceImpl implements CheckItemService {

    @Override
    public List<TreeNode> getTreeData() {

        // 获取数据
        String key = RedisConstant.CHECK_PRE + "tree";
        List<TreeNode> tnlist = null;
        String tnStr = redisDao.get(key);
        if (!StrUtil.isEmpty(key)) {
            tnlist = JSON.parseArray(tnStr, TreeNode.class);
        }
        if (tnlist != null) {
            return tnlist;
        } else {
            String hql = "from CheckItem order by levelCode asc";
            List<CheckItem> mats = this.find(hql);
            Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
            if(mats==null){
            	// 构造树形结构
                tnlist = TreeUtil.getNodeList(nodelist);
                redisDao.save(key, tnlist);
                return tnlist;
            }
            for (CheckItem mat : mats) {
                TreeNode node = new TreeNode();
                node.setText(mat.getName());
                node.setId(mat.getId());
                node.setParentId(mat.getParentId());
                node.setLevelCode(mat.getLevelCode());
                nodelist.put(node.getId(), node);
            }
            // 构造树形结构
            tnlist = TreeUtil.getNodeList(nodelist);
            redisDao.save(key, tnlist);
            return tnlist;
        }
    }

    public List<CheckItem> getMatsByCode(String code) {
        String key = RedisConstant.MAT_PRE + code;
        List mats = redisDao.get(key, List.class);
        if (mats == null) {
            String hql = "from CheckItem where code='" + code + "'";
            CheckItem mat = this.get(hql);
            mats = this.find("from CheckItem where parentId='" + mat.getId() + "' order by levelCode");
            redisDao.add(key, mats);
            return mats;
        } else {
            return mats;
        }
    }

    public boolean referByMaterial(String matId) {
    	CheckItem mat = this.get(CheckItem.class, matId);
        String sql = "select m.id from tbl_checkItem m ,tbl_dic_mat dm where m.parent_id=dm.id " +
                " and dm.levelCode like :levelCode";
        Map<String,Object> param=new HashMap<>();
        param.put("levelCode",mat.getLevelCode()+"%");
        List<Map<String, Object>> list=this.findMapBySql(sql,param);
        if(list.size()>0)
            return true;
        else
            return false;
    }

    @Override
    public Result getMatNames(String id) {
        String name=this.redisDao.get("org:"+id);
        if(StrUtil.isEmpty(name)) {
        	CheckItem org=this.get(CheckItem.class,id);
            if(org.getLevelCode().length()==12){
                redisDao.save("mat:"+id,org.getName());
                return new Result(true,org.getName(),"获取成功");
            }else{
                name=getMatNames(org.getParentId()).getData().toString()+"->"+org.getName();
                redisDao.save("mat:"+id,name);
                return new Result(true,name,"获取成功");
            }
        }else{
            return new Result(true,name,"获取成功");
        }
    }

}
