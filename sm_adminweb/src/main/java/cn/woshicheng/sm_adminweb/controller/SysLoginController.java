package cn.woshicheng.sm_adminweb.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Producer;

import cn.woshicheng.common.util.json.JsonR;
import cn.woshicheng.core.cache.J2CacheAnnotation;
import cn.woshicheng.core.shiro.ShiroUtils;
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

	// 获取验证码

	@J2CacheAnnotation
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
			session.setAttribute(Constants.KAPTCHA_Login_SESSION_KEY, capText);
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

	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public JsonR login(String username, String password, String captcha) throws IOException {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_Login_SESSION_KEY);
		if (null == kaptcha) {
			return JsonR.error("验证码已失效");
		}
		if (!captcha.equalsIgnoreCase(kaptcha)) {
			return JsonR.error("验证码不正确");
		}
		try {
			Subject subject = ShiroUtils.getSubject();
			// sha256加密
			password = new Sha256Hash(password).toHex();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		} catch (UnknownAccountException e) {
			return JsonR.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return JsonR.error(e.getMessage());
		} catch (LockedAccountException e) {
			return JsonR.error(e.getMessage());
		} catch (AuthenticationException e) {
			return JsonR.error("请重新输入帐号或者密码");
		}
		return JsonR.ok();
	}

}
