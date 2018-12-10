package cn.woshicheng.sm_adminweb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * https://yq.aliyun.com/ask/395568 ssl配置
 * 
 * @author chenjf
 *
 */
public class JettyServer {
	private static final String LOG_PATH = "./logs/access/yyyy_mm_dd.request.log";

	private static final String WEB_XML = "META-INF/webapp/WEB-INF/web.xml";
	private static final String CLASS_ONLY_AVAILABLE_IN_IDE = "cn.woshicheng.sm_adminwebtest.IDE";
	private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/java/META-INF/webapp";

	private int localPort;

	public static interface WebContext {
		public File getWarPath();

		public String getContextPath();
	}

	private Server server;
	private int port;
	private String bindInterface;

	public JettyServer(int aPort) {
		this(aPort, null);
	}

	public JettyServer(int aPort, String aBindInterface) {
		port = aPort;
		bindInterface = aBindInterface;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void start() throws Exception {
		server = new Server(createThreadPool());

		NetworkTrafficServerConnector connector = createConnector();
		server.addConnector(connector);

		server.setHandler(createHandlers());
		server.setStopAtShutdown(true);

		server.start();
		localPort = connector.getLocalPort();
	}

	public void join() throws InterruptedException {
		server.join();
	}

	public void stop() throws Exception {
		server.stop();
	}



	private ThreadPool createThreadPool() {
		// TODO: You should configure these appropriately
		// for your environment - this is an example only
		QueuedThreadPool _threadPool = new QueuedThreadPool();
		_threadPool.setMinThreads(10);
		_threadPool.setMaxThreads(100);
		
		return _threadPool;
	}

	//参数设置: 
	//	minThreads（最小线程数） 
	//	maxThreads (最大线程数）
	//	SelectChannelConnector 
	//	maxIdleTime （连接最大空闲时间） 
	//	acceptors (同时在监听read事件的线程数) 
	//	lowResourceMaxIdleTime(表示线程资源稀少时的maxIdleTime 默认值是 -1，表示没有设置。一般设置值应该<=maxIdleTime ) 
	//	lowResourcesConnections(只有NIO才有这个设置，表示连接空闲时的连接数，大于这个数将被shutdown 
	//	默认值是 0,表示该设置没有生效 每个acceptor的连接数=(lowResourcesConnections+acceptors-1)/acceptors)） 
	//	acceptQueueSize(连接被 accept 前允许等待的连接数即Socket的Backlog)
	
	//	connector.setMaxIdleTime(10000); 
	//	connector.setAcceptors(32); 
	//	connector.setStatsOn(false); 
	//	connector.setLowResourcesConnections(65000); 
	//	connector.setLowResourceMaxIdleTime(5000); 
	//	connector.setAcceptQueueSize(3000); 
	
	private NetworkTrafficServerConnector createConnector() {
		NetworkTrafficServerConnector _connector = new NetworkTrafficServerConnector(server);
		_connector.setPort(port);
		_connector.setHost(bindInterface);
	
//		_connector.setIdleTimeout(idleTimeout);
		
		
		_connector.setAcceptQueueSize(3000);
	
		return _connector;
	}

	private HandlerCollection createHandlers() {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");
		//_ctx.set
		if (isRunningInShadedJar()) {
			_ctx.setWar(getShadedWarUrl());
		} else {
			_ctx.setWar(PROJECT_RELATIVE_PATH_TO_WEBAPP);
		}

		List<Handler> _handlers = new ArrayList<Handler>();

		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[0]));

		RequestLogHandler _log = new RequestLogHandler();
		_log.setRequestLog(createRequestLog());

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts, _log });

		// JSP 相关
		// _ctx.setTempDirectory(temp);
		// _ctx.setAttribute("javax.servlet.context.tempdir", temp);
		// 解决9.3不能解析jsp
		// _ctx.setAttribute("org.eclipse.jetty.containerInitializers",
		// Arrays.asList(new ContainerInitializer(new JettyJasperInitializer(), null)));
		// _ctx.setAttribute(InstanceManager.class.getName(), new
		// SimpleInstanceManager());
		return _result;
	}

	private RequestLog createRequestLog() {
		NCSARequestLog _log = new NCSARequestLog();

		File _logPath = new File(LOG_PATH);
		_logPath.getParentFile().mkdirs();

		_log.setFilename(_logPath.getPath());
		_log.setRetainDays(90);
		_log.setExtended(false);
		_log.setAppend(true);
		_log.setLogTimeZone("GMT");
		_log.setLogLatency(true);
		return _log;
	}

	// ---------------------------
	// Discover the war path
	// ---------------------------

	private boolean isRunningInShadedJar() {
		try {
			Class.forName(CLASS_ONLY_AVAILABLE_IN_IDE);
			return false;
		} catch (ClassNotFoundException anExc) {
			return true;
		}
	}

	private URL getResource(String aResource) {
		return Thread.currentThread().getContextClassLoader().getResource(aResource);
	}

	private String getShadedWarUrl() {
		String _urlStr = getResource(WEB_XML).toString();
		// Strip off "WEB-INF/web.xml"
		return _urlStr.substring(0, _urlStr.length() - 15);
	}
}
