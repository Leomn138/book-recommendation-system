package com.bookrecommendationsystem.recommendation.stub;

import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.UserRequestV1;
import com.bookrecommendationsystem.recommendation.dto.UserResponseV1;
import org.springframework.http.HttpStatus;

public class UserStub {
    public static User get() {
        User user = new User();
        user.setId(Long.parseLong("1"));
        user.setName("Leonardo Nascimento");
        user.setUsername("leomn138");

        return user;
    }

    public static UserResponseV1 getResponse() {
        UserResponseV1 user = new UserResponseV1();
        user.setName("Leonardo Nascimento");
        user.setUsername("leomn138");
        user.setStatusCode(HttpStatus.CREATED);
        return user;
    }

    public static UserRequestV1 getRequest() {
        UserRequestV1 user = new UserRequestV1();
        user.setName("Leonardo Nascimento");
        user.setUsername("leomn138");

        return user;
    }
}
