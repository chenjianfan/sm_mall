package cn.woshicheng.core.cache;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.woshicheng.core.Constant;

/**
 * 系统日志注解
 *
 * @author cjf
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface J2CacheAnnotation {

	int saveTime() default 20; // 默认保存

	String key() default Constant.CacheFinalName;// 缓存保存的key地址

}
