package com.rohim.web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	public MyAuthenticationSuccessHandler() {
		super.setDefaultTargetUrl("/");
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// store user info in session, again
		MyUser user = (MyUser) authentication.getPrincipal();

		HttpSession session = request.getSession(true);
		session.setAttribute("user", user);
		session.setAttribute("locked", user.isLocked());

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
