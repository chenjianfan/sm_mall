package cn.woshicheng.core.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShowLogAnnotation {

	String value() default "操作日志内容";

	// 是否保存到数据库里面
	boolean isSaveDb() default true;
}
