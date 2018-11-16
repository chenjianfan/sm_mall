package cn.woshicheng.smadmin.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Controller
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	//获取 获取spring ApplicationContext 方法一
	/*@Autowired
	private ApplicationContext applicationContext;*/
		
	@RequestMapping(value = "index",method =RequestMethod.GET)
	public void index(@RequestParam(value="name",required=false) String name,HttpServletRequest request,Model model) {
		
		if(null!=name && name.length()>0){						
			model.addAttribute("uname", name);
		} else {
			model.addAttribute("uname", "laochen");
		}
		
		logger.info("--you call spring mvc /index");
		//获取spring ApplicationContext 方法二
		/*
		ServletContext servletContext =request.getServletContext();
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
		
		Connection conn=null;
		Statement stmt=null;
		try {
			 conn = dataSource.getConnection();
			 stmt = conn.createStatement();			 
			 ResultSet result = stmt.executeQuery("select * from et_user_info where 1 limit 2");
			 while(result.next()){	
				 logger.info("email:"+result.getString("email"));
			 }
			 stmt.close();
			 conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
}