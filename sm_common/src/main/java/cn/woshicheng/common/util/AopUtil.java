package cn.woshicheng.common.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class AopUtil {

	public static StringBuffer getNameAndArgs(Class<?> cls, String clazzName, String methodName, Object[] args)
			throws NotFoundException {

		Map<String, Object> nameAndArgs = new HashMap<String, Object>();
		ClassPool pool = ClassPool.getDefault();
		ClassClassPath classPath = new ClassClassPath(cls);
		pool.insertClassPath(classPath);

		CtClass cc = pool.get(clazzName);
		CtMethod cm = cc.getDeclaredMethod(methodName);
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			// exception
		}
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < cm.getParameterTypes().length; i++) {
			nameAndArgs.put(attr.variableName(i + pos), args[i]);// paramNames即参数名
		}
		// nameAndArgs的两种类型，用实体类接收的类似这样：
		// reqParams=com.whoareyou.fido.rest.User@616b9c0e
		// 用Map<String,Object>接收的是这样：menuNo=56473283，遍历这个map区分两种不同，使用不同的取值方式。
		// 根据获取到的值所属的不同类型通过两种不同的方法获取参数
		boolean flag = false;
		if (nameAndArgs != null && nameAndArgs.size() > 0) {
			for (Map.Entry<String, Object> entry : nameAndArgs.entrySet()) {
				if (entry.getValue() instanceof String) {
					flag = true;
					break;
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		if (flag) {
			// 从Map中获取
			sb.append(JSON.toJSONString(nameAndArgs));
		} else {
			if (args != null) {
				for (Object object : args) {
					if (object != null) {
						if (object instanceof MultipartFile || object instanceof ServletRequest
								|| object instanceof ServletResponse) {
							continue;
						}
						sb.append(JSON.toJSONString(object));
					}
				}
			}
		}
		return sb;
	}

}
