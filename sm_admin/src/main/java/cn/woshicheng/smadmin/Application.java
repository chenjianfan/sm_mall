package cn.woshicheng.smadmin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
	
	public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("applicationContext.xml");

		JettyEmbedServer jettyEmbedServer = (JettyEmbedServer)ct.getBean("jettyEmbedServer");
		jettyEmbedServer.start();
		jettyEmbedServer.join();
	}
}