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

public class SpringLoadBean implements ApplicationContextAware {

	private static ApplicationContext context = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static Object getBean(String name) {
		return context.getBean(name);
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		SpringLoadBean.context = context;
	}
	
	
	
}
