package com.cnpc.framework.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnpc.framework.base.pojo.TreeNode;

/**
 * Created by billJiang on 2017/2/12. e-mail:jrn1012@petrochina.com.cn
 * qq:475572229
 */
public class TreeUtil {

    public static List<TreeNode> getNodeList(Map<String, TreeNode> nodelist) {
        List<TreeNode> tnlist = new ArrayList<>();
        for (String id : nodelist.keySet()) {
            TreeNode node = nodelist.get(id);
            if (StrUtil.isEmpty(node.getParentId())) {
                tnlist.add(node);
            } else {
                if (nodelist.get(node.getParentId()).getNodes() == null)
                    nodelist.get(node.getParentId()).setNodes(new ArrayList<TreeNode>());
                nodelist.get(node.getParentId()).getNodes().add(node);
            }
        }
        return tnlist;
    }
    
    public static List<TreeNode> getThirdNodeList(Map<String, TreeNode> nodelist) {
        List<TreeNode> tnlist = new ArrayList<>();
        String recentFirstNodeID = "";
        String recentSecondNodeID = "";
        for (String id : nodelist.keySet()) {
            TreeNode node = nodelist.get(id);
            
            if(node.getLevelCode().length() == 6){
                tnlist.add(node);
                recentFirstNodeID = node.getId();
            }else if(node.getLevelCode().length() == 12){
                
                if (nodelist.get(recentFirstNodeID).getNodes() == null){
                    nodelist.get(recentFirstNodeID).setNodes(new ArrayList<TreeNode>());
                }
                    
                nodelist.get(recentFirstNodeID).getNodes().add(node);
                
                if("2".equals(node.getRightType())){
                    recentSecondNodeID = node.getId();
                }
            }else if(node.getLevelCode().length() == 18){
                if (nodelist.get(recentSecondNodeID).getNodes() == null){
                    nodelist.get(recentSecondNodeID).setNodes(new ArrayList<TreeNode>());
                }
                nodelist.get(recentSecondNodeID).getNodes().add(node);
            }
        }
        return tnlist;
    }
}
