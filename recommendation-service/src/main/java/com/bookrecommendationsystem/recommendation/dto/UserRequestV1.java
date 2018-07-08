package com.bookrecommendationsystem.recommendation.dto;

import javax.validation.constraints.NotNull;

public class UserRequestV1 {
    @NotNull
    private String username;
    @NotNull
    private String name;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
