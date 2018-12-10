package cn.woshicheng.sm_adminweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.woshicheng.core.tomcat.EnvTomcatConfig;
import cn.woshicheng.core.tomcat.TomcatUtil;

public class TomcatMain {
	private static Logger log = LoggerFactory.getLogger(TomcatMain.class);

	public static void main(String[] args) {

		TomcatUtil.tomcatStart("sm_adminweb");
	}
}
