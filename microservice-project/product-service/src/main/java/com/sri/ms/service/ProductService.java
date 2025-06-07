package com.sri.ms.service;

import java.util.List;

import com.sri.ms.enticy.Product;

public interface ProductService {

	public Product addProduct(Product product);

	public List<Product> getAllProducts();

	public Product getProductById(Long id);
}
