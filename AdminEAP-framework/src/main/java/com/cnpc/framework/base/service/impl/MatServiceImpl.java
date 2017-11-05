package com.cnpc.framework.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.MatService;
import com.cnpc.framework.base.service.OrgService;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.StrUtil;
import com.cnpc.framework.utils.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("matService")
public class MatServiceImpl extends BaseServiceImpl implements MatService {

    @Override
    public List<TreeNode> getTreeData() {

        // 获取数据
        String key = RedisConstant.MAT_PRE + "tree";
        List<TreeNode> tnlist = null;
        String tnStr = redisDao.get(key);
        if (!StrUtil.isEmpty(key)) {
            tnlist = JSON.parseArray(tnStr, TreeNode.class);
        }
        if (tnlist != null) {
            return tnlist;
        } else {
            String hql = "from Mat order by levelCode asc";
            List<Mat> mats = this.find(hql);
            Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
            for (Mat mat : mats) {
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

    public List<Mat> getMatsByCode(String code) {
        String key = RedisConstant.MAT_PRE + code;
        List mats = redisDao.get(key, List.class);
        if (mats == null) {
            String hql = "from Mat where code='" + code + "'";
            Mat mat = this.get(hql);
            mats = this.find("from Mat where parentId='" + mat.getId() + "' order by levelCode");
            redisDao.add(key, mats);
            return mats;
        } else {
            return mats;
        }
    }

    public boolean referByMaterial(String matId) {
    	Mat mat = this.get(Mat.class, matId);
        String sql = "select m.id from tbl_materiel m ,tbl_dic_mat dm where m.parent_id=dm.id " +
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
            Mat org=this.get(Mat.class,id);
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
