package com.cnpc.framework.base.service;

import java.util.List;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.pojo.TreeNode;

public interface DictService extends BaseService {

    List<TreeNode> getTreeData();
    
    List<TreeNode> getTreeDataByCode(String code);

    List<Dict> getDictsByCode(String code);
}
