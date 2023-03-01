package br.com.jardessouza.service.impl;

import br.com.jardessouza.domain.Product;
import br.com.jardessouza.dto.ProductInputDTO;
import br.com.jardessouza.exception.ProductAlreadyExistsException;
import br.com.jardessouza.exception.ProductNotFoundException;
import br.com.jardessouza.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    @Test
    @DisplayName("when FindById product is call with valid id a product is returned")
    public void findByIdSuccessTest(){
        Long id = 1L;
        var product = new Product(1L, "product name", 10.00, 10);

        Mockito.when(this.productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        var outputDTO = this.productService.findById(id);

        Assertions.assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("when FindById product is call with invalid id throws ProductNotFoundException")
    public void findByIdExceptionTest(){
        Long id = 1L;

        Mockito.when(this.productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> this.productService.findById(id));

    }

    @Test
    @DisplayName("when delete product is call with id should does not throw")
    public void deleteSuccessTest(){
        Long id = 1L;
        var product = new Product(1L, "product name", 10.00, 10);

        Mockito.when(this.productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        Assertions.assertThatNoException().isThrownBy(() -> this.productService.delete(id));

    }

    @Test
    @DisplayName("when delete product is call with invalid id throws ProductNotFoundException")
    public void deleteExceptionTest(){
        Long id = 1L;

        Mockito.when(this.productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> this.productService.delete(id));

    }

    @Test
    @DisplayName("when findAll product is call a list product is returned ")
    public void findAllSuccessTest(){
        var products = List.of(
                new Product(1L, "product name", 10.00, 10),
                new Product(2L, "other product name", 10.00, 100)
        );

        Mockito.when(this.productRepository.findAll()).thenReturn(products);

        var outputDTOs = this.productService.findAll();

        Assertions.assertThat(outputDTOs)
                .extracting("id","name", "price", "quantityInStock")
                .contains(
                        Tuple.tuple(1L, "product name", 10.00, 10),
                        Tuple.tuple(2L, "other product name", 10.00, 100)
                );
    }


}
