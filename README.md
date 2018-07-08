# Book Recommendation System

Simple book recommendation service which is usable via REST API. 
It is possible to define a new user, who will be provided with 20 book recommendations. 
Feedback can be provided. The feedback can either be "liked the book", "disliked the book" or "not interested".

## Requirements:

* Users are identified by their username.
* The list of recommendations should contain exactly 20 entries if possible.


## Domain
![domain](https://photos-3.dropbox.com/t/2/AAB82xKEpygEKyAJ5pIVegg52pZmuGpaaNBruU3h2wqGJQ/12/51974941/png/32x32/1/_/1/2/Book-Recommendation-System-Domain%20(1).png/EJKbhCgY1ooBIAcoBw/UdhYRNcbYRz5SBL58SR9RS7okYBXvcBjtat97jcAIsU?preserve_transparency=1&size=2048x1536&size_mode=3)

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