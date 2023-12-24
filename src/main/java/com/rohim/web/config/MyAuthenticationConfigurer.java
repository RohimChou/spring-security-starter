package com.rohim.web.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class MyAuthenticationConfigurer extends AbstractHttpConfigurer<MyAuthenticationConfigurer, HttpSecurity> {

	private String loginUrl;

	private AuthenticationSuccessHandler authnSuccessHandler;

	private AuthenticationFailureHandler authnFailureHandler;

	private RequestMatcher requiredAuthnRequestMatcher;

	private HttpSessionSecurityContextRepository securityContextRepository;

	private LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint;

	public MyAuthenticationConfigurer() {
		this.loginUrl = "/login";
		this.authnSuccessHandler = new MyAuthenticationSuccessHandler();
		this.authnFailureHandler = new SimpleUrlAuthenticationFailureHandler(this.loginUrl + "?error");
		this.requiredAuthnRequestMatcher = new AntPathRequestMatcher(this.loginUrl, "POST");
		this.securityContextRepository = new HttpSessionSecurityContextRepository();
		this.loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(this.loginUrl + "?abc");
	}

	public static MyAuthenticationConfigurer configure() {
		return new MyAuthenticationConfigurer();
	}

	/**
	 * if need to config other configurer, set it here
	 */
	@Override
	public void init(HttpSecurity http) {
		// if AuthorizationFilter raise AuthenticationException, redirect to /login
		ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
		if (exceptionHandling != null) {
			exceptionHandling.authenticationEntryPoint(this.loginUrlAuthenticationEntryPoint);
		}
	}

	@Override
	public void configure(HttpSecurity http) {
		MyAuthenticationFilter myAuthnFilter = new MyAuthenticationFilter(this.loginUrl);

		myAuthnFilter.setAuthenticationSuccessHandler(this.authnSuccessHandler);
		myAuthnFilter.setAuthenticationFailureHandler(this.authnFailureHandler);
		myAuthnFilter.setRequiresAuthenticationRequestMatcher(this.requiredAuthnRequestMatcher);
		myAuthnFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

		// for letting maximumSessions, maxSessionsPreventsLogin... works
		myAuthnFilter.setSessionAuthenticationStrategy(http.getSharedObject(SessionAuthenticationStrategy.class));

		// if not set, default AuthorizationFilter will throw AccessDeniedException cuz no SecurityContext found
		// in session attribute "SPRING_SECURITY_CONTEXT"
		// new Repository() yourself:
		// 	super.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
		// or could set it yourself:
		// 	SecurityContext secContext = SecurityContextHolder.getContext();
		// 	request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", secContext);
		SecurityContextRepository secureContextRepo = http.getSharedObject(SecurityContextRepository.class);
		if (secureContextRepo != null) {
			myAuthnFilter.setSecurityContextRepository(secureContextRepo);
		} else {
			myAuthnFilter.setSecurityContextRepository(this.securityContextRepository);
		}

		http.addFilterBefore(myAuthnFilter, UsernamePasswordAuthenticationFilter.class);
	}

	public MyAuthenticationConfigurer loginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
		return this;
	}

	public MyAuthenticationConfigurer successHandler(AuthenticationSuccessHandler authnSuccessHandler) {
		this.authnSuccessHandler = authnSuccessHandler;
		return this;
	}

	public MyAuthenticationConfigurer failureHandler(AuthenticationFailureHandler authnFailureHandler) {
		this.authnFailureHandler = authnFailureHandler;
		return this;
	}

	public MyAuthenticationConfigurer requiredAuthnRequestMatcher(RequestMatcher requiredAuthnRequestMatcher) {
		this.requiredAuthnRequestMatcher = requiredAuthnRequestMatcher;
		return this;
	}

	public MyAuthenticationConfigurer securityContextRepository(HttpSessionSecurityContextRepository securityContextRepository) {
		this.securityContextRepository = securityContextRepository;
		return this;
	}
}
