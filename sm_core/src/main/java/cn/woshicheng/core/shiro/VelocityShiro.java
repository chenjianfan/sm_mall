package cn.woshicheng.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro权限Velocity标签
 *
 */
public class VelocityShiro {

	/**
	 * 是否拥有该权限
	 *
	 * @param permission
	 *            权限标识
	 * @return true：yes false：no
	 */
	public boolean hasPermission(String permission) {
		Subject subject = SecurityUtils.getSubject();
		return subject != null && subject.isPermitted(permission);
	}

}
