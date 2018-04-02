package com.cnpc.framework.base.service.impl;

import com.cnpc.framework.base.dao.BaseDao;
import com.cnpc.framework.base.entity.Contract;
import com.cnpc.framework.base.service.ContractService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("contractService")
public class ContractServiceImpl extends BaseServiceImpl implements ContractService {

    @Resource
    private BaseDao baseDao;


    private List<Contract> getContracts() {
        return baseDao.find("from Contract");
    }

    @Override
    public Contract getContractByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        return this.get("from Contract where name=:name", params);
    }

    @Override
    public Contract getContractById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return this.get("from Contract where id=:id", params);
    }
}
