package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.RatingLevel;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    public static MatrixFactorizationModel model;
    public static SparkConf conf;
    public static JavaSparkContext jsc;

    @Override
    public void init(String environment, String appName) {
        conf = new SparkConf().setMaster(environment).setAppName(appName);
        jsc = new JavaSparkContext(conf);
    }
    @Override
    public void trainModel(List<Rating> ratings, int rank, int interactionsNumber, double lambda) {

        JavaRDD<Rating> ratingsRDD = jsc.parallelize(ratings);
        JavaRDD<org.apache.spark.mllib.recommendation.Rating> mllibRatings = ratingsRDD.map(rating -> {
            return new org.apache.spark.mllib.recommendation.Rating(
                    Integer.parseInt(rating.getUser().getId().toString()),
                    Integer.parseInt(rating.getBook().getAsin()),
                    RatingLevel.valueOf(rating.getRatingLevel()).getWeight());
        });

        model = ALS.train(JavaRDD.toRDD(mllibRatings), rank, interactionsNumber, lambda);
    }

    @Override
    public List<Integer> getTwentyRecommendationsForAnUser(int userId) {
        final int numberOfRecommendations = 20;
        org.apache.spark.mllib.recommendation.Rating[] response = model.recommendProducts(userId, numberOfRecommendations);
        List<Integer> asinList = new ArrayList();
        for (int i = 0; i < response.length; i++) {
            asinList.add(response[i].product());
        }
        return asinList;
    }
}
