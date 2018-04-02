package com.cnpc.framework.base.controller;

import com.alibaba.fastjson.JSON;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.Contract;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.ContractService;
import com.cnpc.framework.utils.PropertiesUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/contract")
public class ContractController {

    @Resource
    private ContractService contractService;


    private final static String initPassword= PropertiesUtil.getValue("contract.initPassword");

    /**
     * 用户编辑
     *
     * @return
     */
    @RefreshCSRFToken
    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    private String edit(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "/contract/contract_edit";
    }

    /**
     * 用户列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String list() {
        return "/contract/contract_list";
    }

    /**
     * 用户列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/audit_list")
    private String auditList() {
        return "/contract/contract_audit";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private Contract getcontract(String id) {

        return contractService.get(Contract.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result savecontract(Contract contract, HttpServletRequest request) {
        if(contract.getId()==null) {
            contract.setStatus(0);
            contractService.save(contract);
        }else {
            contractService.update(contract);
        }
        return new Result(true);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
    @ResponseBody
    private Result deletecontract(@PathVariable("id") String id) {
        Contract contract = contractService.get(Contract.class, id);
        try {
            contractService.delete(contract);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(true);
    }

    /**
     * loadData
     *
     * @param pInfo
     * @param conditions
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/loadData")
    @ResponseBody
    public Map<String, Object> loadData(String pInfo, String conditions) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageInfo pageInfo = JSON.parseObject(pInfo, PageInfo.class);
        DetachedCriteria criteria = DetachedCriteria.forClass(Contract.class);
        pageInfo.setCount(contractService.getCountByCriteria(criteria));
        map.put("pageInfo", pageInfo);
        map.put("data", contractService.getListByCriteria(criteria, pageInfo));
        return map;
    }

    /**
     * tab方式curd demo
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/tab/list")
    private String tablist() {
        return "base/contract/contract_tab_list";
    }


    /**
     * 新的页面打开 curd demo
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/list")
    private String pagelist(String id, HttpServletRequest request) {
        request.setAttribute("selectId", id);
        return "base/contract/contract_page_list";
    }

    /**
     * 用户编辑 new page
     *
     * @return
     */
    @RefreshCSRFToken
    @RequestMapping(method = RequestMethod.GET, value = "/page/edit")
    private String pageEdit(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "base/contract/contract_page_edit";
    }




    @RequestMapping(value = "/select/{multiple}/{ids}/{callback}", method = RequestMethod.GET)
    public String selectcontractPage(@PathVariable("multiple") String multiple,
                                 @PathVariable("ids") String ids,
                                 @PathVariable("callback") String callback, HttpServletRequest request) {
        request.setAttribute("multiple", multiple);
        request.setAttribute("ids", ids);
        request.setAttribute("callback",callback);
        return "base/contract/contract_select_list";
    }

}
