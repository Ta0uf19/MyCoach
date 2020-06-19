package io.mycoach.service;

public class BotRequest {

    final String sessionId;
    final QueryInput queryInput;

    public BotRequest(String sessionId, String text) {
        this.sessionId = sessionId;
        // QueryInput
        QueryInput queryInput = new QueryInput();
        queryInput.text = new TextInput();
        queryInput.text.text = text;
        queryInput.text.languageCode = "fr-FR";
        this.queryInput = queryInput;
    }


    class QueryInput {
        TextInput text;
    }
    class TextInput {
        String text;
        String languageCode;
    }

}
