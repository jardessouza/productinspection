package br.com.jardessouza.resource;

import br.com.jardessouza.*;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

        assertThat(productRequest)
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

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.serviceBlockingStub.create(productRequest))
                .withMessage("ALREADY_EXISTS: Produto Product A já cadastrado no sistema.");
    }

    @Test
    @DisplayName("when FindById method is call with valid id a product is returned")
    public void findByIdSuccessTest(){
        var resquest = RequestById.newBuilder()
                .setId(1L).build();

        var productResponse = this.serviceBlockingStub.findById(resquest);

        assertThat(productResponse.getId())
                .isEqualTo(resquest.getId());

        assertThat(productResponse.getName())
                .isEqualTo("Product A");
    }

    @Test
    @DisplayName("When findById is call with invalid throw ProductNotFoundException")
    public void findByIdExceptionTest(){
        var resquest = RequestById.newBuilder()
                .setId(100L).build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.serviceBlockingStub.findById(resquest))
                .withMessage("NOT_FOUND: Produto com ID 100 nao encontrado.");
    }

    @Test
    @DisplayName("When delete is call with od should does not throw ")
    public void deleteSuccessTest(){
        var resquest = RequestById.newBuilder()
                .setId(1L).build();

        Assertions.assertThatNoException().isThrownBy(() -> this.serviceBlockingStub.delete(resquest));
    }

    @Test
    @DisplayName("When delete is call with invalid throw ProductNotFoundException")
    public void deleteExceptionTest(){
        var resquest = RequestById.newBuilder()
                .setId(100L).build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.serviceBlockingStub.delete(resquest))
                .withMessage("NOT_FOUND: Produto com ID 100 nao encontrado.");
    }

    @Test
    @DisplayName("When findAll method is call a product list is returned")
    public void findAllSuccessTest(){
        EmptyRequest request = EmptyRequest.newBuilder().build();

        var responseList = this.serviceBlockingStub.findAll(request);

        assertThat(responseList).isInstanceOf(ProductResponseList.class);
        assertThat(responseList.getProductsCount()).isEqualTo(2);
        Assertions.assertThat(responseList.getProductsList())
                .extracting("id","name", "price", "quantityInStock")
                .contains(
                        Tuple.tuple(1L , "Product A", 10.99, 10),
                        Tuple.tuple(2L, "Product B", 10.99, 10)
                );
    }


}
