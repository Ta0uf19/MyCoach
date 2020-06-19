package io.mycoach.service;

public class BotResponse {

    private String fulfillmentText;

    public String getFulfillmentText() {
        return fulfillmentText;
    }

    public void setFulfillmentText(String fulfillmentText) {
        this.fulfillmentText = fulfillmentText;
    }

    @Override
    public String toString() {
        return "BotResponse{" +
                "fulfillmentText='" + fulfillmentText + '\'' +
                '}';
    }
}
