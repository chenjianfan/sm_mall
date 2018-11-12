package cn.woshicheng.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RestfullTest {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println("启动完成");
	}
}
