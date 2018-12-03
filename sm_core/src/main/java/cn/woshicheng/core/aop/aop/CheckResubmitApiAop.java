package cn.woshicheng.core.aop.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.woshicheng.common.util.AopUtil;
import cn.woshicheng.common.util.json.JsonR;
import cn.woshicheng.core.aop.CheckResubmitAnnotation;
import cn.woshicheng.core.error.BusinessException;
import cn.woshicheng.core.shiro.ShiroUtils;
import cn.woshicheng.core.utils.MD5Util;
import cn.woshicheng.core.utils.RedisService;
import cn.woshicheng.core.utils.RequestUtil;
import javassist.NotFoundException;

/**
 * 防止重复提交操作，使用redis验证
 * 
 * @author cjf
 *
 */
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CheckResubmitApiAop {
	private final Logger LOGGER = LoggerFactory.getLogger(CheckResubmitApiAop.class);

	@Autowired
	private RedisService redisService;

	private final static String KEY_PREFIX = "_submit";
	private final static String CONTACT_STR = "_";

	@Pointcut("@annotation(cn.woshicheng.core.aop.CheckResubmitAnnotation)")
	private void cut() {

	}

	/**
	 * 被拦截方法before进行 切面处理保存请求参数日志,便于排查问题
	 * 
	 * @throws Throwable
	 */
	@Order(Integer.MAX_VALUE) //
	@Around("cut()")
	public Object checkRepeatBeforeMethod(ProceedingJoinPoint jp) throws Throwable {
		// 获取切面的签名
		MethodSignature signature = (MethodSignature) jp.getSignature();
		// 获取被拦截的方法
		Method method = signature.getMethod();
		CheckResubmitAnnotation lock = method.getDeclaredAnnotation(CheckResubmitAnnotation.class);
		String repeatSubmitLogRedisKey = getRepeatSubmitLogRedisKey(jp, lock);
		// 查询redis是不是有这个key
		String result = redisService.get(repeatSubmitLogRedisKey);

		// 在指定时间内存在这个key就报错
		if (null != result) {
			LOGGER.error("用户在上次请求时间：" + result + "请求最短间隔：" + lock.expireTime());
			LOGGER.error("请求redis key：" + repeatSubmitLogRedisKey);
			return JsonR.error("请不要重复提交");
		} else {
			// 不存在就在redis里面存放一份
			redisService.setString(repeatSubmitLogRedisKey, System.currentTimeMillis() + "");
			redisService.expire(repeatSubmitLogRedisKey, lock.expireTime());
			return jp.proceed();
		}
	}

	private String getRepeatSubmitLogRedisKey(JoinPoint jp, CheckResubmitAnnotation lock) throws Exception {
		// 获取切面的签名
		MethodSignature signature = (MethodSignature) jp.getSignature();
		// 获取被拦截的方法
		Method method = signature.getMethod();
		// 获取注解
		// 获取类名称
		String className = signature.getDeclaringTypeName();

		// 获取方法名称
		String methodName = method.getName();
		Object[] args = jp.getArgs();

		StringBuffer requestJson = AopUtil.getNameAndArgs(this.getClass(), className, methodName, args);

		requestJson.append(lock.token());

		String md5Key = MD5Util.GetMD5Code(requestJson.toString());

		List<String> params = new ArrayList<String>();
		// params.add(KEY_PREFIX);
		// 用户名

		className = className.substring(className.lastIndexOf(".") + 1);

		params.add(className);
		params.add(methodName);
		params.add(md5Key);
		return getRedisKey(params);
	}

	/**
	 * 获取redis的键： prefix + className + methodName + uid +
	 *
	 * @param params
	 */
	public String getRedisKey(List<String> params) {
		StringBuffer keyBuf = new StringBuffer();
		params.forEach(param -> keyBuf.append(param).append(CONTACT_STR));
		return keyBuf.deleteCharAt(keyBuf.lastIndexOf("_")).toString();
	}
}
