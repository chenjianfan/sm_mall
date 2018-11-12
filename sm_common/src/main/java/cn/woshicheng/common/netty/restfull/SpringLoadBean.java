package cn.woshicheng.common.netty.restfull;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/****
 * 解决reastEasy 自己管理对象依赖注入，导致在类中。spring ioc的bean，加载不了的问题
 * 
 * @author chenjf
 *
 */
@Component
public class SpringLoadBean implements ApplicationContextAware {

	private static ApplicationContext applicationContext; // Spring应用上下文环境

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringLoadBean.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T) applicationContext.getBean(name);
	}
}
