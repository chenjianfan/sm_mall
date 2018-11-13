
package cn.woshicheng.common.netty.restfull;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

import org.apache.log4j.Logger;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.embedded.EmbeddedJaxrsServer;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.netty.*;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import cn.woshicheng.common.netty.NettyUtils;
import cn.woshicheng.common.util.SystemIpUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/***
 * 
 * @author chenjf
 *
 *         <!--restfull相关 --> <bean id="springLoadBean"
 *         class="com.talianshe.common.netty.restfull.SpringLoadBean"scope=
 *         "singleton" />
 *         <bean id="restfullsocket" class="io.netty.bootstrap.ServerBootstrap"
 *         />
 */

public class RestFullNettyServer implements EmbeddedJaxrsServer {
	private EventLoopGroup eventLoopGroup;
	private EventLoopGroup eventExecutor;
	protected SecurityDomain domain;

	protected ServerBootstrap bootstrap = new ServerBootstrap();

	@Autowired
	SpringLoadBean springLoadBean;

	protected ResteasyDeployment deployment;

	private int maxRequestSize = 1024 * 1024 * 10;
	private int backlog = 128;
	// 访问的根目录

	protected String root = "";
	protected int port;
	protected String ioMode;

	public void setMaxRequestSize(int maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	public void init() {
		System.out.println("启动restfull");
		start();
	}

	private int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
	private int executorThreadCount = 16;

	@Override
	public void start() {
		try {
			RestfullConfig.initfig("/restfullConfig.properties");

			root = RestfullConfig.restfull_socket_rootPath;
			ioMode = RestfullConfig.iomode;
			port = Integer.parseInt(RestfullConfig.restfull_socket_port);
			//
			eventLoopGroup = NettyUtils.createEventLoop(ioMode, Runtime.getRuntime().availableProcessors() + 1,
					"restfull_netty");
			eventExecutor = NettyUtils.createEventLoop(ioMode, Runtime.getRuntime().availableProcessors() + 1,
					"restfull_netty");
			deployment = setAutotActualResourceClasses();
			deployment.start();
			final RequestDispatcher dispatcher = new RequestDispatcher(
					(SynchronousDispatcher) deployment.getDispatcher(), deployment.getProviderFactory(), domain);

		

			// Configure the server.
			bootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new HttpRequestDecoder());
							ch.pipeline().addLast(new HttpObjectAggregator(maxRequestSize));
							ch.pipeline().addLast(new HttpResponseEncoder());
							ch.pipeline().addLast(new RestEasyHttpRequestDecoder(dispatcher.getDispatcher(), root,
									RestEasyHttpRequestDecoder.Protocol.HTTP));
							ch.pipeline().addLast(new MyRestEasyHttpResponseEncoder(dispatcher));
							ch.pipeline().addLast(eventExecutor, new RequestHandler(dispatcher));
						}
					}).option(ChannelOption.SO_BACKLOG, backlog).childOption(ChannelOption.SO_KEEPALIVE, true);
			// bootstrap.channel(NettyUtils.getServerChannelClass(ioMode));
			bootstrap.bind(port).syncUninterruptibly();

			
			StringBuffer sb=new StringBuffer();
			sb.append("启动restfull 服务完成=http://");
			sb.append(SystemIpUtil.INTERNET_IP);
			sb.append(":");
			sb.append(port);
			sb.append(root);
			
			System.out.println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		eventLoopGroup.shutdownGracefully();
		eventExecutor.shutdownGracefully();
	}

	@Override
	public ResteasyDeployment getDeployment() {
		return deployment;
	}

	@Override
	public void setDeployment(ResteasyDeployment deployment) {

		this.deployment = setAutotActualResourceClasses();
	}

	@Override
	public void setRootResourcePath(String rootResourcePath) {
		root = rootResourcePath;
		if (root != null && root.equals("/"))
			root = "";
	}

	@Override
	public void setSecurityDomain(SecurityDomain sc) {
		this.domain = sc;
	}

	/**
	 * 自动注入所有的controller
	 * 
	 * @return
	 */
	public ResteasyDeployment setAutotActualResourceClasses() {
		
		ResteasyDeployment rdp = new ResteasyDeployment();
		ApplicationContext ac = springLoadBean.getContext();
		Collection<Object> controllers = ac.getBeansWithAnnotation(Controller.class).values();
		rdp.getResources().addAll(controllers);
		return rdp;
	}
}