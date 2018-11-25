package cn.woshicheng.common.dao;

/**
 * 基本的dao crud
 * 
 * @author chenjf
 *
 * @param <T>
 */
public interface BaseDao<T> {

	int save(T t);

	int update(T t);

	int delete(Object id);

}
