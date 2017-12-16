package com.radish.master.service;

import com.radish.master.entity.DutyCheck;

public interface DutyCheckService {
	public String save(DutyCheck dc);
	public void saveOrUpdate(DutyCheck dc);
	public DutyCheck load(String id);
}
