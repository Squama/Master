package com.cnpc.framework.base.service;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;

import java.util.List;

public interface MatService extends BaseService {

    List<TreeNode> getTreeData();

    List<Mat> getMatsByCode(String code);

    boolean referByMaterial(String matId);

    Result getMatNames(String id);
}
