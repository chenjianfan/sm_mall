package cn.woshicheng.core.aop.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.woshicheng.common.util.AopUtil;
import cn.woshicheng.common.util.SystemIpUtil;
import cn.woshicheng.core.aop.BaseAop;
import cn.woshicheng.core.aop.ShowLogAnnotation;
import cn.woshicheng.core.entity.SyslogEntitiy;
import cn.woshicheng.core.service.SysLogService;
import cn.woshicheng.core.shiro.ShiroUtils;
import javassist.NotFoundException;

/**
 * 查询操作日志
 * 
 * @author cjf
 */
@Aspect
@Component
public class ShowLogAop extends BaseAop {
	private final Logger logger = LoggerFactory.getLogger(ShowLogAop.class);

	@Autowired
	private SysLogService sysLogService;

	/**
	 * 切点
	 */
	@Pointcut("@annotation(cn.woshicheng.core.aop.ShowLogAnnotation)")
	public void ShowLogAopData() {

	}

	/**
	 * 后置通知通知
	 *
	 * @param joinPoint
	 *            连接点
	 */
	@After("ShowLogAopData()")
	public void saveSysLog(JoinPoint joinPoint) {
		try {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();

			ShowLogAnnotation cacheAnnotation = method.getAnnotation(ShowLogAnnotation.class);
			if (cacheAnnotation != null) {
				// 注解上的描述
				SyslogEntitiy entitiy = new SyslogEntitiy();
				entitiy.setLoginfo(cacheAnnotation.value());

				// 方法名
				String className = joinPoint.getTarget().getClass().getName();
				String methodName = signature.getName();

				StringBuffer sb = new StringBuffer();
				sb.append("类名-");
				sb.append(className);
				sb.append("-方法名");
				sb.append(methodName);

				entitiy.setMethodName(sb.toString());

				Object[] args = joinPoint.getArgs();

				StringBuffer parmJosn;
				parmJosn = AopUtil.getNameAndArgs(this.getClass(), className, methodName, args);
				entitiy.setParams(parmJosn.toString());
				logger.info("--------------请求内容--------------");

				logger.info("类和方法：" + sb.toString());

				logger.info("请求类方法参数名称和值：" + parmJosn.toString());
				logger.info("--------------请求内容--------------");

				// 获取当前用户的名和ip
				String username = "";
				if ("login".equals(methodName)) {
					username = "登录";
				}
				if (ShiroUtils.getUserEntity() != null
						&& !StringUtils.isEmpty(ShiroUtils.getUserEntity().getUsername())) {
					username = ShiroUtils.getUserEntity().getUsername();
				}
				entitiy.setName(username);

				// 获取request
				HttpServletRequest request = getHttpServletRequest();
				entitiy.setIp(SystemIpUtil.getIpAddr(request));
				// 保存系统日志

				if (cacheAnnotation.isSaveDb()) {
					sysLogService.saveLog(entitiy);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 在方法执行完结后打印返回内容
	@AfterReturning(returning = "o", pointcut = "ShowLogAopData()")
	public void methodAfterReturing(Object o) {
		logger.info("--------------返回内容----------------");
		logger.info("Response内容:" + JSON.toJSONString(o));
		logger.info("--------------返回内容----------------");

		logger.info("--------------end----------------");

	}

}
