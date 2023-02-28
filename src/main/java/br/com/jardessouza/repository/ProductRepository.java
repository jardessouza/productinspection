package br.com.jardessouza.repository;

import br.com.jardessouza.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameIgnoreCase(String name);
}
