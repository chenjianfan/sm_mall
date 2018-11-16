package cn.woshicheng.smadmin;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 内 嵌 jetty http 服务
 * 
 */
public class JettyEmbedServer {
	private static Logger logger = LoggerFactory.getLogger(JettyEmbedServer.class);
	private static final String LOG_PATH = "./logs/front/yyyy_mm_dd.log";
	private static final String WEB_XML = "src/main/webapp/WEB-INF/web.xml";
	private static final String CLASS_ONLY_AVAILABLE_IN_IDE = "cn.woshicheng.smadmintest.IDE";
	// private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP =
	// "src/main/webapp";
	private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/webapp";

	private int port;
	private Server server;
	private String bindInterface;

	public JettyEmbedServer() {
		super();
	}

	public JettyEmbedServer(int port) {
		super();
		this.port = port;
	}

	public void start() throws Exception {

		server = new Server(createThreadPool());
		NetworkTrafficServerConnector connector = createConnector();

		server.addConnector(connector);
		server.setHandler(createHandlersDev());
		server.setStopAtShutdown(true);
		server.start();

	}

	public void stop() throws Exception {
		server.stop();
	}

	public void join() throws Exception {
		server.join();
		logger.info("please visit web address: http://localhost" + this.port + "/index");
	}

	private HandlerCollection createHandlers() {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");

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

		return _result;
	}

	private HandlerCollection createHandlersDev() {

		// 设置web资源根路径以及访问web的根路径

		ProtectionDomain protectionDomain = JettyEmbedServer.class.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		String warFile = location.toExternalForm();
		WebAppContext context = new WebAppContext(warFile, "/");
		// 设置work dir,war包将解压到该目录，jsp编译后的文件也将放入其中。
		String currentDir = new File(location.getPath()).getParent();
		File workDir = new File(currentDir, "work");
		context.setTempDirectory(workDir);

		List<Handler> _handlers = new ArrayList<Handler>();
		_handlers.add(context);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[0]));

		RequestLogHandler _log = new RequestLogHandler();
		_log.setRequestLog(createRequestLog());

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts, _log });

		return _result;
	}

	private NetworkTrafficServerConnector createConnector() {

		NetworkTrafficServerConnector _connector = new NetworkTrafficServerConnector(server);
		_connector.setPort(port);
		_connector.setHost(bindInterface);
		return _connector;

	}

	// TODO 改造为使用spring ioc的方式
	private ThreadPool createThreadPool() {
		QueuedThreadPool _threadPool = new QueuedThreadPool();
		_threadPool.setMinThreads(10);
		_threadPool.setMaxThreads(100);
		return _threadPool;
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
		logger.info("web_xml:" + WEB_XML);
		String _urlStr = getResource(WEB_XML).toString();
		return _urlStr.substring(0, _urlStr.length() - 15);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
