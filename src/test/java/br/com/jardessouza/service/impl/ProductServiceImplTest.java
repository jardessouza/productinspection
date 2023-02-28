package br.com.jardessouza.service.impl;

import br.com.jardessouza.domain.Product;
import br.com.jardessouza.dto.ProductInputDTO;
import br.com.jardessouza.exception.ProductAlreadyExistsException;
import br.com.jardessouza.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;
    @Test
    @DisplayName("when create product service is call with valid data a product is returned")
    public void createProductSuccessTest(){
        var product = new Product(1L, "product name", 10.00, 10);

        Mockito.when(this.productRepository.save(Mockito.any())).thenReturn(product);

        var inputDTO = new ProductInputDTO("product name", 10.00, 10);
        var outputDTO = this.productService.create(inputDTO);

        Assertions.assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }
    @Test
    @DisplayName("when create product service is call with duplicated name, throw ProductAlreadyExistsException")
    public void createProductExceptionTest(){
        var product = new Product(1L, "product name", 10.00, 10);

        Mockito.when(this.productRepository.findByNameIgnoreCase(Mockito.any())).thenReturn(Optional.of(product));

        var inputDTO = new ProductInputDTO("product name", 10.00, 10);
        Assertions.assertThatExceptionOfType(ProductAlreadyExistsException.class)
                        .isThrownBy(() -> this.productService.create(inputDTO));

    }
}
