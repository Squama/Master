/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.budget;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.pojo.TreeNode;
import com.cnpc.framework.base.service.DictService;
import com.cnpc.framework.constant.RedisConstant;
import com.cnpc.framework.utils.StrUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月14日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/budget/fee")
public class FeeTypeManageController {

    @Resource
    private DictService dictService;
    
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String list() {

        return "budgetmanage/fee/fee_tree";
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public List<Dict> getAll() {

        String hql = "from Dict order by levelCode asc";
        return dictService.find(hql.toString());
    }

    @RequestMapping(value = "/treeData", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeData() {

        return dictService.getMeasureTreeData();
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Dict get(@PathVariable("id") String id) {
        Dict dict = dictService.get(Dict.class, id);
        if (!StrUtil.isEmpty(dict.getParentId())) {
            dict.setParentName(dictService.get(Dict.class, dict.getParentId()).getName());
        } else {
            dict.setParentName("系统字典");
        }
        return dict;
    }

    @RequestMapping(value = "/getDictsByCode", method = RequestMethod.POST)
    @ResponseBody
    public List<Dict> getDictsByCode(String code) {
        return dictService.getDictsByCode(code);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(Dict dict) {

        dict.setUpdateDateTime(new Date());
        dictService.saveOrUpdate(dict);
        Dict parent = dictService.get(Dict.class, dict.getParentId());
        dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode());
        dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode() + "s");
        return new Result(true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        try {
            Dict dict = dictService.get(Dict.class, id);
            dictService.delete(dict);
            Dict parent = dictService.get(Dict.class, dict.getParentId());
            dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode());
            dictService.deleteCacheByKey(RedisConstant.DICT_PRE + parent.getCode() + "s");
            return new Result(true);
        } catch (Exception e) {
            return new Result(false, "该字典已经被其他数据引用，不可删除");
        }
    }
    
}
