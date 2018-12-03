package cn.woshicheng.core.cache.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import cn.woshicheng.core.cache.J2CacheAnnotation;

@Aspect
@Component
public class J2CacheAop {
	/**
	 * 切点
	 */
	@Pointcut("@annotation(cn.woshicheng.core.cache.J2CacheAnnotation)")
	public void J2CacheData() {

	}

	/**
	 * 前置通知
	 *
	 * @param joinPoint
	 *            连接点
	 */
	@Before("J2CacheData()")
	public void saveSysLog(JoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		J2CacheAnnotation cacheAnnotation = method.getAnnotation(J2CacheAnnotation.class);
		if (cacheAnnotation != null) {
			// 注解上的描述
			System.out.println("J2CacheData前置通知");
		}else{
			System.out.println("J2CacheData前置通知----没有J2CacheAnnotation");
		} 
	}

}
