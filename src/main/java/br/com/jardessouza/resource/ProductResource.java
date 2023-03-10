package br.com.jardessouza.resource;

import br.com.jardessouza.*;
import br.com.jardessouza.dto.ProductInputDTO;
import br.com.jardessouza.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

    private final IProductService productService;

    public ProductResource(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        var inputDTO = new ProductInputDTO(
                request.getName(),
                request.getPrice(),
                request.getQuantityInStock());

        var outputDTO = this.productService.create(inputDTO);

        var response = ProductResponse.newBuilder()
                .setId(outputDTO.getId())
                .setName(outputDTO.getName())
                .setPrice(outputDTO.getPrice())
                .setQuantityInStock(outputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
        var outputDTO = this.productService.findById(request.getId());

        var response = ProductResponse.newBuilder()
                .setId(outputDTO.getId())
                .setName(outputDTO.getName())
                .setPrice(outputDTO.getPrice())
                .setQuantityInStock(outputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RequestById request, StreamObserver<EmptyResponse> responseObserver) {
        this.productService.delete(request.getId());
        responseObserver.onNext(EmptyResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
        var outputDTOList = this.productService.findAll();
        List<ProductResponse> productResponseList = outputDTOList.stream()
                .map(product ->
                        ProductResponse.newBuilder()
                                .setId(product.getId())
                                .setName(product.getName())
                                .setPrice(product.getPrice())
                                .setQuantityInStock(product.getQuantityInStock())
                                .build())
                .collect(Collectors.toList());

        ProductResponseList response = ProductResponseList.newBuilder()
                .addAllProducts(productResponseList)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
