package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.RatingLevel;
import com.bookrecommendationsystem.recommendation.domain.User;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Override
    public void calculateAll(List<Rating> ratings) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Books Collaborative Filtering");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        JavaRDD<Rating> ratingsRDD = jsc.parallelize(ratings);
        JavaRDD<org.apache.spark.mllib.recommendation.Rating> mllibRatings = ratingsRDD.map(rating -> {
            return new org.apache.spark.mllib.recommendation.Rating(Integer.parseInt(rating.getBook().getAsin()),
                    Integer.parseInt(rating.getUser().getId().toString()),
                    RatingLevel.valueOf(rating.getRatingLevel()).getWeight());
        });

        int rank = 10;
        int numIterations = 10;
        MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(mllibRatings), rank, numIterations, 0.01);

        JavaRDD<Tuple2<Object, Object>> userProducts =
                mllibRatings.map(r -> new Tuple2<>(r.user(), r.product()));
        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
                model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD()
                        .map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating()))
        );

        JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD.fromJavaRDD(
                mllibRatings.map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating())))
                .join(predictions).values();
        double MSE = ratesAndPreds.mapToDouble(pair -> {
            double err = pair._1() - pair._2();
            return err * err;
        }).mean();
        System.out.println("Mean Squared Error = " + MSE);


    }

    @Override
    public List<Book> calculateSingle(User user) {
        return null;
    }

    @Override
    public List<Book> getSingle(User user) {
        return null;
    }
}
