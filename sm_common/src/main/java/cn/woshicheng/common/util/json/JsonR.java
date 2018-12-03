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
	}

	public static JsonR error500() {
		JsonR jsonR = new JsonR();
		jsonR.put(msgCode, fail);
		jsonR.put(msg, "未知错误，请找管理员");
		return jsonR;
	}

	public static JsonR error(String msgError) {
		JsonR jsonR = new JsonR();
		jsonR.put(msgCode, fail);
		jsonR.put(msg, msgError);
		return jsonR;
	}

	public static JsonR ok() {
		JsonR jsonR = new JsonR();
		jsonR.put(msgCode, success);
		jsonR.put(msg, "成功");
		return jsonR;
	}

}
