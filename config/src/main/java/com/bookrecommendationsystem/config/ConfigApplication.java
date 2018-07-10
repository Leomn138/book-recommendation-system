package com.bookrecommendationsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableWebSecurity
@EnableConfigServer
public class ConfigApplication  extends WebSecurityConfigurerAdapter {

	@Value("${security.user.password}")
	private String securityPassword;
	@Value("${security.user.name}")
	private String securityUsername;
	@Value("${security.user.role}")
	private String securityRole;

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.httpBasic();
	}
	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception {

		builder.inMemoryAuthentication()
				.withUser(securityUsername)
				.password("{noop}" + securityPassword)
				.roles(securityRole);
	}
}
