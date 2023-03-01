package br.com.jardessouza.service.impl;

import br.com.jardessouza.domain.Product;
import br.com.jardessouza.dto.ProductInputDTO;
import br.com.jardessouza.dto.ProductOutputDTO;
import br.com.jardessouza.exception.ProductAlreadyExistsException;
import br.com.jardessouza.exception.ProductNotFoundException;
import br.com.jardessouza.repository.ProductRepository;
import br.com.jardessouza.service.IProductService;
import br.com.jardessouza.util.ProductConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        checkDuplicity(inputDTO.getName());
        var product = ProductConverterUtil.productInputDtoToProduct(inputDTO);
        var productCreated = this.productRepository.save(product);
        return ProductConverterUtil.productToProductOutputDto(productCreated);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        var product = getProduct(id);
        return ProductConverterUtil.productToProductOutputDto(product);
    }

    @Override
    public void delete(Long id) {
        var product = getProduct(id);
        this.productRepository.delete(product);
    }

    @Override
    public List<ProductOutputDTO> findAll() {
        var product = this.productRepository.findAll();
        return product.stream()
                .map(ProductConverterUtil::productToProductOutputDto)
                .collect(Collectors.toList());
    }

    private void checkDuplicity(String name){
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> {
                    throw new ProductAlreadyExistsException(name);
                });
    }

    private Product getProduct(Long id) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return product;
    }


}
