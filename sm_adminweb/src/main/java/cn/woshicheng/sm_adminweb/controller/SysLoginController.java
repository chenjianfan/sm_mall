package cn.woshicheng.sm_adminweb.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.code.kaptcha.Producer;

import cn.woshicheng.sm_adminweb.constants.Constants;

/**
 * 登录相关的操作
 * 
 * @author chenjf
 *
 */
@Controller
public class SysLoginController {

	@Autowired
	private Producer captchaProducer;

	@RequestMapping("captchaimg.jpg")
	public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream out = null;
		try {
			HttpSession session = request.getSession();
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("image/jpeg");
			// 生成验证码
			String capText = captchaProducer.createText();
			session.setAttribute(Constants.KAPTCHA_SESSION_KEY,capText);
			// 向客户端写出
			BufferedImage bi = captchaProducer.createImage(capText);
			out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
