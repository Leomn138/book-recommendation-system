# Book Recommendation System

Simple book recommendation service which is usable via REST API. 
It is possible to define a new user, who will be provided with 20 book recommendations. 
Feedback can be provided. The feedback can either be "liked the book", "disliked the book" or "not interested".

## Requirements:

* Users are identified by their username.
* The list of recommendations should contain exactly 20 entries if possible.


## Domain
![alt text](https://www.dropbox.com/s/46slbwf6gm4i2sw/Book-Recommendation-System-Domain%20%281%29.png?dl=0)

## Recommendation Algorithm

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