package com.bookrecommendationsystem.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication  extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/**/*").permitAll();
	}
}
