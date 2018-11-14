package cn.woshichegn.admin;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import cn.woshicheng.common.util.SystemIpUtil;

/**
 * Java -Xms2048m -Xmx2048m -XX:MaxPermSize=128m -jar sm_admin.war
 * 
 * @author Administrator
 *
 */

public class Start {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("Start");

		int port = 8081;
		Server server = new Server(port);
		WebAppContext webAppContext = new WebAppContext("webapp", "/");
		webAppContext.setDescriptor("webapp/WEB-INF/web.xml");
		webAppContext.setResourceBase("src/main/webapp");
		webAppContext.setDisplayName("sm_admin");
		webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		webAppContext.setConfigurationDiscovered(true);
		webAppContext.setParentLoaderPriority(true);
		try {
			server.setHandler(webAppContext);
			server.start();
			logger.info("sm_admin 启动成功：http://" + SystemIpUtil.INTERNET_IP + ":" + port);
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
