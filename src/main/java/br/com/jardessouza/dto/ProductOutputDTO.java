package br.com.jardessouza.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductOutputDTO {
    private final Long id;
    private final String name;
    private final Double price;
    private final Integer quantityInStock;
}
