# Book Recommendation System

Simple book recommendation service which is usable via REST API. 
It is possible to define a new user, who will be provided with 20 book recommendations. 
Feedback can be provided. The feedback can either be "liked the book", "disliked the book" or "not interested".

## Requirements:

* Users are identified by their username.
* The list of recommendations should contain exactly 20 entries if possible.


## Domain
![domain](https://raw.githubusercontent.com/Leomn138/book-recommendation-system/master/recommendation-service/files/Book-Recommendation-System-Domain.png)

## Recommendation Algorithm
One approach to the design of recommender systems that has wide use is collaborative filtering. Collaborative filtering methods are based on collecting and 
analyzing a large amount of information on users’ behaviors, activities or preferences and predicting what users will like based on their similarity to other users.

### Collaborative Filtering - Apache Spark RDD-based API
Spark.mllib currently supports model-based collaborative filtering, in which users and products are described by a small set of latent factors that can be used to predict missing entries. spark.mllib uses the alternating least squares (ALS) algorithm to learn these latent factors. The implementation in spark.mllib has the following parameters:

* numBlocks is the number of blocks used to parallelize computation (set to -1 to auto-configure).
* rank is the number of features to use (also referred to as the number of latent factors).
* iterations is the number of iterations of ALS to run. ALS typically converges to a reasonable solution in 20 iterations or less.
* lambda specifies the regularization parameter in ALS.
* implicitPrefs specifies whether to use the explicit feedback ALS variant or one adapted for implicit feedback data.
* alpha is a parameter applicable to the implicit feedback variant of ALS that governs the baseline confidence in preference observations.

In this approach explicit feedback was used as can be seen on model training:
```java
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
```

## API Specifications

#### /books/:asin
* `GET` : Get book by asin number.

##### Ok Response
```json
{
    "asin": "string",
    "author": "string",
    "genre": "string",
    "title": "string"
}
```

#### /books
* `GET` : Get all books.

##### Ok Response
```json
[
    {
        "asin": "string",
        "author": "string",
        "genre": "string",
        "title": "string"
    }
]
```

#### /users/:username/recommendations
* `GET` : Get top 20 book recommendations for a user.
##### Ok Response
```json
[
    {
        "asin": "string",
        "author": "string",
        "genre": "string",
        "title": "string"
    }
]
```

#### /users
* `POST` : Create a new user.
##### Request
```json
{
    "name": "string",
    "username": "string"
}
```
##### Created Response
```json
{
    "name": "string",
    "username": "string"
}
```

#### /users/:username/ratings
* `POST` : Create a new rating.

Rating Levels are LIKED, DISLIKED and NOT_INTERESTED. 

```json
{
    "asin": "string",
    "username": "string",
    "ratingLevel": "string"
}
```
##### Created Response
```json
{
    "asin": "string",
    "username": "string",
    "ratingLevel": "string"
}
```
More details can be found on {base-uri}/swagger-ui.html.

## How to Run Everything