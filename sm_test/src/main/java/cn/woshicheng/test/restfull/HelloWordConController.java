package cn.woshicheng.test.restfull;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;

import com.woshicheng.utils.JsonUtil;

/**
 * * jax-rs规范用法: http://www.vogella.com/tutorials/REST/article.html resteasy
 * 教程:http://code.freedomho.com/9541.html
 * 
 * @author chenjianfan
 *
 */
@Path("/hello")
@RequestScoped
@Produces("application/json;charset=UTF-8")
@Controller
public class HelloWordConController {

	/*****
	 * 浏览http://localhost:9497/restfull/hello/hellWord
	 * 
	 * @return
	 */
	@GET
	@Path("/hellWord")
	public String hellWord() {
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
		return JsonUtil.getInstance().obj2json(new HelloWrodClass("hellWord,您好，美丽的世界"));
	}

	public class HelloWrodClass {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public HelloWrodClass(String name) {
			super();
			this.name = name;
		}

	}

}
