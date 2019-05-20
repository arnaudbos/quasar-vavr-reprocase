package com.arnaudbos.quasar_vavr;

import co.paralleluniverse.actors.behaviors.RequestMessage;

public class StringMessage extends RequestMessage<String> {
    private String message;

    public StringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
