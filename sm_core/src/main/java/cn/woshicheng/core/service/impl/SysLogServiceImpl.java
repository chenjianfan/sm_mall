package cn.woshicheng.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.woshicheng.core.dao.SysLogDao;
import cn.woshicheng.core.entity.SyslogEntitiy;
import cn.woshicheng.core.service.SysLogService;

//日志操作记录
@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogDao sysLogDao;

	@Override
	public void saveLog(SyslogEntitiy syslogEntitiy) {
		sysLogDao.save(syslogEntitiy);
	}

}
