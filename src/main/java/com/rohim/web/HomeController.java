package com.rohim.web;

import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import static com.rohim.web.Consts.*;

@Controller
public class HomeController {

	@RequestMapping(path = "/", method = RequestMethod.GET)
	@ResponseBody
	public String home() {
		return "<h1>Hello World!</h1>";
	}

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login() {
		return "login.html";
	}

	@RequestMapping(path = "/captcha", method = RequestMethod.GET)
	public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SpecCaptcha specCaptcha = new SpecCaptcha(CAPTCHA_DEFAULT_WIDTH, CAPTCHA_DEFAULT_HEIGHT, CAPTCHA_DEFAULT_LEN);
		request.getSession(true).setAttribute(CAPTCHA_SESSION_KEY, specCaptcha.text().toLowerCase());
		response.setContentType("image/gif");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		specCaptcha.out(response.getOutputStream());
	}

	@RequestMapping(path = "/public", method = RequestMethod.GET)
	@ResponseBody
	public String publicPage() {
		return "<h1>public</h1>";
	}

	@RequestMapping(path = "/protected", method = RequestMethod.GET)
	@ResponseBody
	public String protectedPage() {
		return "<h1>protected</h1>";
	}
}
