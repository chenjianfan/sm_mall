package cn.woshicheng.core.tomcat;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvTomcatConfig {
	private final static Logger log = LoggerFactory.getLogger(EnvTomcatConfig.class);

	public static String port = null;

	public static String basedir = null;
	public static String filepath = null;

	public static boolean init() {
		Configuration config;

		try {
			String env = System.getProperty("env");
			if (env == null) {
				log.info("没有配置环境，使用本地配置local");
				env = "local";
			}
			log.info("当前的环境是: " + env);
			String fileName = "tomcat" + "-" + env + ".properties";
			config = new PropertiesConfiguration(fileName);
			if (port == null || port.isEmpty()) {
				port = "8080";
			}
			basedir = config.getString("tomcat.basedir");
			filepath = config.getString("filepath");
			log.info("==========================================");
			log.info("                    CONFIG                ");

			log.info("==========================================");
			log.info("port: " + port);
			log.info("docbase : " + basedir);
			log.info("filepath : " + filepath);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return false;

		}
	}

}
