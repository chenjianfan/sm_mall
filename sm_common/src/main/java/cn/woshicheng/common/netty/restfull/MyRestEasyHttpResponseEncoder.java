package cn.woshicheng.common.netty.restfull;

import java.util.List;
import java.util.Map;

import javax.ws.rs.ext.RuntimeDelegate;

import org.jboss.resteasy.plugins.server.netty.NettyHttpResponse;
import org.jboss.resteasy.plugins.server.netty.RequestDispatcher;
import org.jboss.resteasy.plugins.server.netty.RestEasyHttpResponseEncoder;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;

public class MyRestEasyHttpResponseEncoder extends RestEasyHttpResponseEncoder {
	private RequestDispatcher dispatcher;

	public MyRestEasyHttpResponseEncoder(RequestDispatcher dispatcher) {
		super(dispatcher);
		this.dispatcher = dispatcher;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void encode(ChannelHandlerContext ctx, NettyHttpResponse nettyResponse, List<Object> out)
			throws Exception {
		ByteBuf buffer = nettyResponse.getBuffer();
		HttpResponse response = nettyResponse.getDefaultFullHttpResponse();

		transformHeaders(nettyResponse, response, dispatcher.getProviderFactory());

		out.add(response);
	}

	public static void transformHeaders(NettyHttpResponse nettyResponse, HttpResponse response,
			ResteasyProviderFactory factory) {
		ByteBuf buffer = nettyResponse.getBuffer();
		if (nettyResponse.isKeepAlive()) {
			// Add content length and connection header if needed
			response.headers().set(Names.CONTENT_LENGTH, buffer.readableBytes());
			response.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
			response.headers().set("access-control-allow-origin", "*");
		}
		for (Map.Entry<String, List<Object>> entry : nettyResponse.getOutputHeaders().entrySet()) {
			String key = entry.getKey();
			for (Object value : entry.getValue()) {
				RuntimeDelegate.HeaderDelegate delegate = factory.getHeaderDelegate(value.getClass());
				if (delegate != null) {
					response.headers().add(key, delegate.toString(value));
				} else {
					response.headers().set(key, value.toString());
				}
			}
		}
	}

}
