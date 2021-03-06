package com.bookrecommendationsystem.recommendation;

import com.bookrecommendationsystem.recommendation.service.infrastructure.RecommendationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@EnableSwagger2
public class BookRecommendationApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BookRecommendationApplication.class, args);
		boolean schedulingEnabled = Boolean.valueOf(context.getEnvironment().getProperty("recommendations.scheduling.enabled"));
		if (schedulingEnabled)
			context.getBean(RecommendationService.class).trainModel();
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}
