package com.hackathon.sofParcours.controller.dto;

import java.util.List;

public class RoomResponse {
    private String id;
    private String code;
    private String name;
    private String status;
    private List<String> participantIds;
    private String message;

    public RoomResponse() {
    }

    public RoomResponse(String id, String code, String name, String status, List<String> participantIds) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.participantIds = participantIds;
    }

    public RoomResponse(String message) {
        this.message = message;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
