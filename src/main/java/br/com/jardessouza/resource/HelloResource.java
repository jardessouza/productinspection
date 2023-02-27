package br.com.jardessouza.resource;

import br.com.jardessouza.HelloReq;
import br.com.jardessouza.HelloRes;
import br.com.jardessouza.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloResource extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloReq request, StreamObserver<HelloRes> responseObserver) {
        var response = HelloRes.newBuilder()
                .setMessage(request.getMessage())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
