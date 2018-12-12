package cn.woshicheng.core.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.woshicheng.core.Constant;
import cn.woshicheng.core.cache.J2CacheUtil;

import java.io.Serializable;

/**
 * 分布式session管理
 *
 */
public class ShiroSessionDao extends EnterpriseCacheSessionDAO {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = super.doCreate(session);

		final String key = Constant.SESSION_KEY + sessionId.toString();

		setShiroSession(key, session);

		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = super.doReadSession(sessionId);
		if (null == session) {
			final String key = Constant.SESSION_KEY + sessionId.toString();
			session = getShiroSession(key);
		}
		return session;
	}

	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);
		final String key = Constant.SESSION_KEY + session.getId().toString();
		setShiroSession(key, session);
	}

	@Override
	protected void doDelete(Session session) {
		super.doDelete(session);
		final String key = Constant.SESSION_KEY + session.getId().toString();
		J2CacheUtil.remove(key);
	}

	private Session getShiroSession(String key) {
		return (Session) J2CacheUtil.get(key);
	}

	private void setShiroSession(String key, Session session) {
		J2CacheUtil.put(key, session);
	}
}
