package cn.woshicheng.common.util.json;

import java.util.HashMap;
import java.util.Map;

/**
 * json返回值的
 * 
 * @author chenjf
 *
 */
public class JsonR extends HashMap<Object, Object> {

	private final static String msgCode = "msgCode";// 0 成功，-1失败
	private final static String msg = "msg";// 是0的话，也许没有，但是是-1的必须有
	private final static String data = "data";// 数据内容

	private final static int success = 0;
	private final static int fail = -1;

	public JsonR() {
		put(msgCode, success);
	}

	public static JsonR error500() {
		return error(500, "未知异常，请联系开发者");
	}

	public static JsonR error(String msg) {
		return error(fail, msg);
	}

	public static JsonR error(String msg, Object dataObject) {
		return error(fail, msg, dataObject);
	}

	public static JsonR error(int msgCode, String msg) {
		JsonR r = new JsonR();
		r.put(msgCode, msgCode);
		r.put(msg, msg);
		return r;
	}

	public static JsonR error(int msgCode, String msg, Object dataObject) {
		JsonR r = new JsonR();
		r.put(msgCode, msgCode);
		r.put(msg, msg);
		r.put(data, dataObject);
		return r;
	}

	/*************************
	 * * wo 是分割线
	 ************************************************/
	public static JsonR ok(String msg) {
		JsonR r = new JsonR();
		r.put(msg, msg);
		return r;
	}

	public static JsonR ok(String msg, Object dataObject) {
		JsonR r = new JsonR();
		r.put(msg, msg);
		r.put(data, dataObject);
		return r;
	}

	public static JsonR ok(Map<String, Object> map) {
		JsonR r = new JsonR();
		r.putAll(map);
		return r;
	}

	public static JsonR ok() {
		return new JsonR();
	}

	public JsonR put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public JsonR put(Object value) {
		super.put(data, value);
		return this;
	}

}
