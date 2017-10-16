package com.cnpc.demos.service;

import com.cnpc.demos.entity.Contract;
import com.cnpc.framework.base.service.BaseService;

public interface ContractService extends BaseService {

    Contract getContractByName(String name);

    Contract getContractById(String id);

}
