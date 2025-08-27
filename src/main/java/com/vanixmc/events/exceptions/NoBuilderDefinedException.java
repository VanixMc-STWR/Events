package com.vanixmc.events.exceptions;

public class NoBuilderDefinedException extends RuntimeException {
    public NoBuilderDefinedException(String type) {
        super("No builder was defined for type: " + type);
    }
}
