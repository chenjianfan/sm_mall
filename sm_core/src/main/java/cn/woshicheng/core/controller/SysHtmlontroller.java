package cn.woshicheng.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.woshicheng.core.aop.aop.CheckResubmitApiAop;

@Controller
public class SysHtmlontroller {

	private final Logger logger = LoggerFactory.getLogger(CheckResubmitApiAop.class);

	@RequestMapping("{module}/{url}.html")
	public ModelAndView page(@PathVariable("module") String module, @PathVariable("url") String url) {

		// StringBuffer sb = new StringBuffer();
		//
		// sb.append(module);
		// sb.append("/");
		// sb.append(url);
		// sb.append(".html");
		//
		// logger.info("访问模块的html=" + sb.toString());

		ModelAndView mv = new ModelAndView(module + "/" + url);

		return mv;

	}

}
