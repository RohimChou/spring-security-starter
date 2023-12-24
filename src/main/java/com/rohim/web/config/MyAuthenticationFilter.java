package com.rohim.web.config;

import com.rohim.web.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.util.Collection;
import java.util.List;

import static com.rohim.web.Consts.CAPTCHA_SESSION_KEY;

public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	public MyAuthenticationFilter(String loginUrl) {
		super(loginUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String secret = request.getParameter("secret");
		String captcha = request.getParameter("captcha");

		if (StringUtils.isEmpty(username)
			|| StringUtils.isEmpty(password)
			|| StringUtils.isEmpty(secret)
			|| StringUtils.isEmpty(captcha)) {
			throw new BadCredentialsException("Input fields cannot be empty");
		}

		if (!this.isCaptchaValid(request, captcha)) {
			throw new BadCredentialsException("Invalid captcha");
		}

		if (!this.isPasswordValid(username, password, secret)) {
			throw new BadCredentialsException("Invalid username or password");
		}

		Authentication authResult = new UsernamePasswordAuthenticationToken(
			new MyUser(username, false, false), null, this.getRolesFromDb(username));

		return authResult;
	}

	private boolean isCaptchaValid(HttpServletRequest request, String captcha) {
		if (StringUtils.isEmpty(captcha)) {
			return false;
		}

		String captchaInSession = (String) request.getSession(true).getAttribute(CAPTCHA_SESSION_KEY);
		if (StringUtils.isEmpty(captchaInSession)) {
			return false;
		}

		return captcha.equalsIgnoreCase(captchaInSession);
	}

	private boolean isPasswordValid(String username, String password, String secret) {
		// TODO: implement password validation
		return true;
	}

	private Collection<? extends GrantedAuthority> getRolesFromDb(String username) {
		// TODO: implement get roles from db
		return List.of(
			new SimpleGrantedAuthority("ROLE_USER"),
			new SimpleGrantedAuthority("ROLE_ADMIN")
		);
	}
}
