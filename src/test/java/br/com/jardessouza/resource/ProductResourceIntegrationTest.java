package br.com.jardessouza.resource;

import br.com.jardessouza.ProductRequest;
import br.com.jardessouza.ProductServiceGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductResourceIntegrationTest {
    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp(){
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @DisplayName("When valid data is provided a product is created")
    public void createProductSuccessTest(){
        var productRequest = ProductRequest.newBuilder()
                .setName("product name")
                .setPrice(10.00)
                .setQuantityInStock(100)
                .build();
        var productResponse = this.serviceBlockingStub.create(productRequest);

        Assertions.assertThat(productRequest)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantity_in_stock")
                .isEqualTo(productResponse);
    }

    @Test
    @DisplayName("When create is called with duplicated name, throw ProductAllreadyExistsException")
    public void createProductAlReadyExistsExceptionTest(){
        var productRequest = ProductRequest.newBuilder()
                .setName("Product A")
                .setPrice(10.00)
                .setQuantityInStock(100)
                .build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.serviceBlockingStub.create(productRequest))
                .withMessage("ALREADY_EXISTS: Produto Product A já cadastrado no sistema.");
    }
}
