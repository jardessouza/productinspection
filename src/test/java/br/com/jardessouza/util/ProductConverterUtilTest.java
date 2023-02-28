package br.com.jardessouza.util;

import br.com.jardessouza.domain.Product;
import br.com.jardessouza.dto.ProductInputDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductConverterUtilTest {
    @Test
    public void productToProductOutputDtoTest() {
        var product = new Product(1L, "product name", 10.0, 10);
        var producctOutputDto = ProductConverterUtil
                .productToProductOutputDto(product);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(producctOutputDto);
    }

    @Test
    public void productInputToProductTest() {
        var productInput = new ProductInputDTO("product name", 10.0, 10);
        var product = ProductConverterUtil
                .productInputDtoToProduct(productInput);

        Assertions.assertThat(productInput)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }
}
