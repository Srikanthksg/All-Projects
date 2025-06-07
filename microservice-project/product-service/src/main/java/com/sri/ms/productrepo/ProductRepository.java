package com.sri.ms.productrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.ms.enticy.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	boolean existsByName(String name);

}
