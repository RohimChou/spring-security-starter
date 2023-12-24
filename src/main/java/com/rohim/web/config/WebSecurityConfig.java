package com.rohim.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	public static final String LOGIN_URL = "/login";

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		// by default, when successfully authenticated, spring will redirect to the original request
		// and append `?continue` query parameter for optimization purpose. just don't need it.
		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
		requestCache.setMatchingRequestParameterName(null); // default is "continue"

		http
			.csrf((csrf) -> csrf.disable())
			.anonymous(anonymous -> anonymous.disable())
			.formLogin(form -> form.disable())
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers(LOGIN_URL).permitAll()
				.requestMatchers("/error").permitAll()
				.requestMatchers("/captcha").permitAll()
				.requestMatchers("/*.html").permitAll()
				.requestMatchers("/images/**").permitAll()
				.anyRequest().authenticated()
			)
			.requestCache(cache -> cache.requestCache(requestCache))
			.sessionManagement((session) -> session
				.maximumSessions(1)
				.maxSessionsPreventsLogin(true)
			)
			// .exceptionHandling((exception) -> exception
			// 	.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
			// )
			.with(MyAuthenticationConfigurer.configure(), (authn) -> authn
				.loginUrl(LOGIN_URL)
			);

		return http.build();
	}

	/**
	 * this is a no-op AuthenticationManager, just return the Authentication object as-is
	 * cuz the default one would use UserDetailsServiceAutoConfiguration which will generate the unnecessary password
	 * could also just exclude it in `@SpringBootApplication(exclude= { UserDetailsServiceAutoConfiguration.class })`
	 */
	@Bean
	public AuthenticationManager noOpAuthenticationManager() {
		return authentication -> authentication;
	}
}
