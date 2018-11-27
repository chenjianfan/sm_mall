package cn.woshicheng.core.cache;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.cache.interceptor.KeyGenerator;

/***
 * Springcache key 的生成策略
 * 
 * @author chenjf
 *
 */
public class MyCacheKey implements Serializable {
	/** 调用目标对象全类名 */
	protected String targetClassName;
	/** 调用目标方法名称 */
	protected String methodName;
	/** 调用目标参数 */
	protected Object[] params;

	public MyCacheKey(Object target, Method method, Object[] elements) {
		this.targetClassName = target.getClass().getName();
		this.methodName = generatorMethodName(method);
		if (ArrayUtils.isNotEmpty(elements)) {
			this.params = new Object[elements.length];
			for (int i = 0; i < elements.length; i++) {
				Object ele = elements[i];
				this.params[i] = ele;
			}
		}
	}

	private String generatorMethodName(Method method) {
		StringBuilder builder = new StringBuilder(method.getName());
		Class<?>[] types = method.getParameterTypes();
		if (ArrayUtils.isNotEmpty(types)) {
			builder.append("(");
			for (Class<?> type : types) {
				String name = type.getName();
				builder.append(name + ",");
			}
			builder.delete(builder.length() - 1, builder.length());
			builder.append(")");
		}
		return builder.toString();
	}

}
