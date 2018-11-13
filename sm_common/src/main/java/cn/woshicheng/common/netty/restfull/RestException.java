package cn.woshicheng.common.netty.restfull;

/***
 * 
 * @author cjf
 *
 */
public class RestException extends RuntimeException {
	public RestException(String s) {
		super(s);
	}

	public RestException(String s, Throwable e) {
		super(s, e);
	}

	public RestException(Throwable e) {
		super(e);
	}
}
