package cn.woshicheng.core.tomcat;

import java.io.File;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatUtil {
	private final static Logger log = LoggerFactory.getLogger(EnvTomcatConfig.class);

	public static void tomcatStart(String webName) {

		try {
			if (!EnvTomcatConfig.init()) {
				log.info("加載配置文件失敗。");
				System.exit(0);
			}
			Tomcat tomcat = new Tomcat();
			final Integer webPort = Integer.parseInt(EnvTomcatConfig.port);
			tomcat.setPort(Integer.valueOf(webPort));
			// 3.设置工作目录,tomcat需要使用这个目录进行写一些东西
			final String baseDir = EnvTomcatConfig.basedir;
			tomcat.setBaseDir(baseDir);
			tomcat.getHost().setAutoDeploy(false);
			// 4. 设置webapp资源路径
			String webappDirLocation = "src/main/java/META-INF/webapp/";
			StandardContext ctx = (StandardContext) tomcat.addWebapp("/",
					new File(webappDirLocation).getAbsolutePath());
			log.info("configuring app with basedir: " + new File("" + webappDirLocation).getAbsolutePath());
			log.info("project dir:" + new File("").getAbsolutePath());
			// 5. 设置上下文路每径
			String contextPath = "";
			ctx.setPath(contextPath);
			ctx.addLifecycleListener(new Tomcat.FixContextListener());
			ctx.setName(webName);
			System.out.println("child Name:" + ctx.getName());
			tomcat.getHost().addChild(ctx);
			log.info("服务器加载完配置，正在启动中……");
			tomcat.start();
			log.info("服务器启动成功");
			tomcat.getServer().await();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("服务器启动失敗", e);

		}

	}

}
