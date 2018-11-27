package cn.woshicheng.core.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class SpringCacheKey implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return new MyCacheKey(target, method, params);
	}

}
