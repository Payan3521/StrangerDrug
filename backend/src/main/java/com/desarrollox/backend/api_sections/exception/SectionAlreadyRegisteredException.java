package com.desarrollox.backend.api_sections.exception;

public class SectionAlreadyRegisteredException extends RuntimeException {
    public SectionAlreadyRegisteredException(String message) {
        super(message);
    }
}
