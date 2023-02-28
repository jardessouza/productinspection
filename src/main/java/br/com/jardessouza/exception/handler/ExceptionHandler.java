package br.com.jardessouza.exception.handler;

import br.com.jardessouza.exception.BaseBusinnessException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ExceptionHandler {
    @GrpcExceptionHandler(BaseBusinnessException.class)
    public StatusRuntimeException handlerBusinessException(BaseBusinnessException e){
        return e.getStatusCode()
                .withCause(e.getCause())
                .withDescription(e.getErrorMessage())
                .asRuntimeException();
    }
}
