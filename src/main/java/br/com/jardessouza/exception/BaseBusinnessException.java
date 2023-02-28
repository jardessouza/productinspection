package br.com.jardessouza.exception;

import io.grpc.Status;

public abstract class BaseBusinnessException extends RuntimeException {
    public BaseBusinnessException(String message) {
        super(message);
    }

    public abstract Status getStatusCode();
    public abstract String getErrorMessage();
}
