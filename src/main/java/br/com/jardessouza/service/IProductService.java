package br.com.jardessouza.service;

import br.com.jardessouza.dto.ProductInputDTO;
import br.com.jardessouza.dto.ProductOutputDTO;

import java.util.List;

public interface IProductService {
    ProductOutputDTO create(ProductInputDTO inputDTO);
    ProductOutputDTO findById(Long id);
    void delete(Long id);
    List<ProductOutputDTO> findAll();
}
