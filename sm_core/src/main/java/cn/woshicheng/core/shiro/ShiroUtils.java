package cn.woshicheng.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import cn.woshicheng.core.entity.SysUserEntity;


/***
 * Shiro工具类
 * 
 * @author chenjf
 *
 */
public class ShiroUtils {

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static SysUserEntity getUserEntity() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	public static String getUserId() {
		return getUserEntity().getUserId();
	}

	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}

	/**
	 * 跟进key获取值的
	 * 
	 * @param key
	 * @return
	 */
	public static String getKaptcha(String key) {
		String kaptcha;
		try {
			kaptcha = getSessionAttribute(key).toString();
			getSession().removeAttribute(key);
		} catch (Exception e) {
			return null;
		}
		return kaptcha;
	}

}
