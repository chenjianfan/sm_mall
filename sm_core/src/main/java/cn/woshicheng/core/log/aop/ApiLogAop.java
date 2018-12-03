package cn.woshicheng.core.log.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import cn.woshicheng.core.log.ApiLogAnnotation;

/***
 * api请求参数以及回应的日志信息
 * 
 * @author chenjf
 *
 */
@Aspect
@Component
public class ApiLogAop {
	/**
	 * 切点
	 */
	@Pointcut("@annotation(cn.woshicheng.core.log.ApiLogAnnotation)")
	public void ShowLogAopData() {

	}

	/**
	 * 前置通知
	 *
	 * @param joinPoint
	 *            连接点
	 */
	@Before("ShowLogAopData()")
	public void saveSysLog(JoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		ApiLogAnnotation cacheAnnotation = method.getAnnotation(ApiLogAnnotation.class);
		if (cacheAnnotation != null) {
			// 注解上的描述
			System.out.println("ShowLogAnnotation前置通知");

		}
	}
}
