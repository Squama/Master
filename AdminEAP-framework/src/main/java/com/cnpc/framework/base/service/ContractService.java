package com.cnpc.framework.base.service;

import com.cnpc.framework.base.entity.Contract;

public interface ContractService extends BaseService {

    Contract getContractByName(String name);

    Contract getContractById(String id);

}
