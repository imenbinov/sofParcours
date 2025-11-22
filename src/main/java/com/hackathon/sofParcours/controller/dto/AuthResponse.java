package com.hackathon.sofParcours.controller.dto;

public class AuthResponse {
    private String token;
    private String userId;
    private String username;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(String token, String userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
