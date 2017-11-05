package com.cnpc.framework.base.controller;

import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.MatService;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by billJiang on 2017/6/19.
 * e-mail:475572229@qq.com  qq:475572229
 * 组织结构管理控制器
 */
@Controller
@RequestMapping("/mat")
public class MatController {

    @Resource
    private MatService matService;

    /**
     * 物料列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/tree")
    private String list() {

        return "materialSpace/material_tree/material_tree";
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public List<Org> getAll() {

        String hql = "from Mat order by levelCode asc";
        return matService.find(hql.toString());
    }

    /**
     * getTreeData 构造bootstrap-treeview格式数据
     *
     * @return
     */
    @RequestMapping(value = "/treeData", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeData() {
        return matService.getTreeData();
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Mat get(@PathVariable("id") String id) {
        Mat mat = matService.get(Mat.class, id);
        if (!StrUtil.isEmpty(mat.getParentId())) {
        	mat.setParentName(matService.get(Mat.class, mat.getParentId()).getName());
        } else {
        	mat.setParentName("总根类");
        }
        return mat;
    }


    @RequestMapping(value = "/show/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result show(@PathVariable("id") String id) {
        return matService.getMatNames(id);
    }

    @RequestMapping(value = "/getList/{code}", method = RequestMethod.POST)
    @ResponseBody
    public List<Mat> getMatsByCode(String code) {
        return matService.getMatsByCode(code);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(Mat org) {

        org.setUpdateDateTime(new Date());
        matService.saveOrUpdate(org);
        if (!StrUtil.isEmpty(org.getParentId())) {
            Mat parent = matService.get(Mat.class, org.getParentId());
            matService.deleteCacheByKey(RedisConstant.MAT_PRE + parent.getCode());
        }
        matService.deleteCacheByKey(RedisConstant.MAT_PRE + "tree");
        return new Result(true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        try {
            Mat org = matService.get(Mat.class, id);
            if(matService.referByMaterial(org.getId())) {
               return new Result(false,"该物料分类被物料表引用，不可删除");
            }
            matService.delete(org);
            if (!StrUtil.isEmpty(org.getParentId())) {
                Mat parent = matService.get(Mat.class, org.getParentId());
                matService.deleteCacheByKey(RedisConstant.MAT_PRE + parent.getCode());
            }
            matService.deleteCacheByKey(RedisConstant.MAT_PRE + "tree");
            return new Result(true);
        } catch (Exception e) {
            return new Result(false, "该物料分类被物料表引用，不可删除");
        }
    }
}
