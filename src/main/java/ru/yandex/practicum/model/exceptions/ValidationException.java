package ru.yandex.practicum.model.exceptions;

public class ValidationException extends Exception {
    public ValidationException(final String message) {
        super(message);
    }
}