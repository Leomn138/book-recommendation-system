package com.bookrecommendationsystem.recommendation.service.infrastructure;

import com.bookrecommendationsystem.recommendation.domain.*;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.repository.RatingRepository;
import com.bookrecommendationsystem.recommendation.repository.RecommendationRepository;
import com.bookrecommendationsystem.recommendation.repository.UserRepository;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SparkRecommendationServiceImpl implements RecommendationService {

    @Value("${spark.environment}")
    private String environment;
    @Value("${spark.applicationName}")
    private String appName;
    @Value("${spark.model.train.rank}")
    private int rank;
    @Value("${spark.model.train.interactionsNumber}")
    private int interactionsNumber;
    @Value("${spark.model.train.lambda}")
    private double lambda;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static MatrixFactorizationModel model;
    private static JavaSparkContext jsc;

    private void init() {
        SparkConf conf = new SparkConf().setMaster(environment).setAppName(appName);
        jsc = new JavaSparkContext(conf);
        log.info("Spark context initiated.");
    }
    @Override
    @Scheduled(cron = "${spark.model.train.cron}")
    @ConditionalOnProperty(value = "recommendations.scheduling.enabled", havingValue = "true")
    public void trainModel() {
        if (model == null)
            init();
        log.info("Training model.");
        ArrayList<UserBookRating> userBookRatings = new ArrayList<UserBookRating>((Collection) ratingRepository.findAll());
        JavaRDD<UserBookRating> ratingsRDD = jsc.parallelize(userBookRatings);
        JavaRDD<Rating> ratings = ratingsRDD.map(rating -> new Rating(
                Math.toIntExact(rating.getUser().getId()),
                Integer.parseInt(rating.getBook().getAsin()),
                RatingLevel.valueOf(rating.getRatingLevel()).getWeight()));

        model = ALS.train(JavaRDD.toRDD(ratings), rank, interactionsNumber, lambda);
        log.info("Model Trained.");
        saveAllRecommendations();
        jsc.close();
    }

    private void saveAllRecommendations() {
        int numberOfRecommendations = 20;
        ArrayList<User> userList = new ArrayList<User>((Collection) userRepository.findAll());
        for (User user : userList) {
            try {
                recommendationRepository.deleteByUser(user);
                Rating[] ratingResponseList = model.recommendProducts(Math.toIntExact(user.getId()), numberOfRecommendations);

                for (Rating ratingResponse : ratingResponseList) {
                    Book book = bookRepository.findByAsin(Integer.toString(ratingResponse.product()));
                    if (book != null) {
                        Recommendation recommendation = new Recommendation();
                        recommendation.setUser(user);
                        recommendation.setBook(book);
                        recommendation.setRating(ratingResponse.rating());
                        recommendationRepository.save(recommendation);
                    }
                }
            } catch (Exception e) {
                log.error("Couldn't get recommendation for: " + user.getUsername());
            }
        }
        log.info("Recommendation data saved.");
    }
}
