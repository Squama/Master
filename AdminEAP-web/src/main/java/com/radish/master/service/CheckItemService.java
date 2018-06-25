package com.radish.master.service;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.qualityCheck.CheckItem;

import java.util.List;

public interface CheckItemService extends BaseService {

    List<TreeNode> getTreeData();

    List<CheckItem> getMatsByCode(String code);

    boolean referByMaterial(String matId);

    Result getMatNames(String id);
}
