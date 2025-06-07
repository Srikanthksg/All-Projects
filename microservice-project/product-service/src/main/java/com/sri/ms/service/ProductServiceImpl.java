package com.sri.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sri.ms.enticy.Product;
import com.sri.ms.productrepo.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product addProduct(Product product) {

		if(productRepository.existsByName(product.getName())) {
			throw new RuntimeException("Product already exist with name: "+product.getName());
		}
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product getProductById(Long id) {
		
		return productRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found with id: "+id));
	}
}
