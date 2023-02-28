package br.com.jardessouza.util;

import br.com.jardessouza.domain.Product;
import br.com.jardessouza.dto.ProductInputDTO;
import br.com.jardessouza.dto.ProductOutputDTO;

public class ProductConverterUtil {
    public  static ProductOutputDTO productToProductOutputDto(Product product){
        return new ProductOutputDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }

    public  static Product productInputDtoToProduct(ProductInputDTO product){
        return new Product(
                null,
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock());
    }
}
