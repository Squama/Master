package com.radish.master.service;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.safty.CheckItemAQ;

import java.util.List;

public interface SaftyItemService extends BaseService {

    List<TreeNode> getTreeData();

    List<CheckItemAQ> getMatsByCode(String code);

    boolean referByMaterial(String matId);

    Result getMatNames(String id);
}
