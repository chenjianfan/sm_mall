package cn.woshicheng.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交操作
 * 
 * @author Administrator
 *
 */

@Retention(RetentionPolicy.RUNTIME) // 在运行时可以获取
@Target(value = { ElementType.METHOD, ElementType.TYPE }) // 作用到类，方法，接口上等
public @interface CheckResubmitAnnotation {

	/**
	 * 重复提交超时时间 单位：秒
	 * 
	 * @return
	 */
	long expireTime() default 2L;

	/**
	 * 用户token,用来做唯一标识
	 * 
	 * @return
	 */
	String token() default "token字符串";

}
