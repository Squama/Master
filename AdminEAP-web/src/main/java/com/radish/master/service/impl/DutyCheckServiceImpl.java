package com.radish.master.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpc.framework.base.dao.BaseDao;
import com.radish.master.entity.DutyCheck;
import com.radish.master.service.DutyCheckService;

@Service("dutyCheckService")
public class DutyCheckServiceImpl implements DutyCheckService {
	@Autowired
	private BaseDao bd;
	
	@Override
	public void saveOrUpdate(DutyCheck dc) {
		bd.saveOrUpdate(dc);
	}

	@Override
	public DutyCheck load(String id) {
		DutyCheck dc = bd.get(DutyCheck.class, id);
		return dc;
	}

	@Override
	public String save(DutyCheck dc) {
		String i = (String)bd.save(dc);
		return i;
	}
	
}
