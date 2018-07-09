package com.bookrecommendationsystem.recommendation;

import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.repository.RatingRepository;
import com.bookrecommendationsystem.recommendation.service.RecommendationService;
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

import java.util.ArrayList;
import java.util.Collection;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@EnableSwagger2
public class BookRecommendationApplication {
	private static ConfigurableApplicationContext context;
	public static void main(String[] args) {
		context = SpringApplication.run(BookRecommendationApplication.class, args);

		context.getBean(RecommendationService.class).init(getSparkEnvironment(), getSparkApplicationName());
		context.getBean(RecommendationService.class)
				.trainModel(new ArrayList<Rating>((Collection)context.getBean(RatingRepository.class).findAll()), getSparkModelTrainRank(), getSparkModelTrainInteractionsNumber(), getSparkModelTrainLambda());
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

	public static String getSparkEnvironment() {
		return context.getEnvironment().getProperty("spark.environment");
	}

	public static String getSparkApplicationName() {
		return context.getEnvironment().getProperty("spark.applicationName");
	}

	public static int getSparkModelTrainRank() {
		return Integer.parseInt(context.getEnvironment().getProperty("spark.model.train.rank"));
	}

	public static int getSparkModelTrainInteractionsNumber() {
		return Integer.parseInt(context.getEnvironment().getProperty("spark.model.train.interactionsNumber"));
	}

	public static double getSparkModelTrainLambda() {
		return Double.parseDouble(context.getEnvironment().getProperty("spark.model.train.lambda"));
	}

}
