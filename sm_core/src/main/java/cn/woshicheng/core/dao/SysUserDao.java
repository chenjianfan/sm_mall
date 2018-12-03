package cn.woshicheng.core.dao;

import java.util.List;
import java.util.Map;

import cn.woshicheng.common.dao.BaseDao;
import cn.woshicheng.core.entity.SysUserEntity;

/***
 * 系统用户
 * 
 * @author chenjf
 *
 */
public interface SysUserDao extends BaseDao<SysUserEntity> {

	/**
	 * 查询用户的所有权限
	 *
	 * @param userId
	 *            用户ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	
	SysUserEntity queryByPwd(String password,String userName);

	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);

}
