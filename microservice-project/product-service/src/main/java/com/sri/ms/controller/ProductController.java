package com.sri.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sri.ms.enticy.Product;
import com.sri.ms.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		Product createProduct =productService.addProduct(product);
		return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
	}
	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products= productService.getAllProducts();
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	@GetMapping("/{productId}")
	public ResponseEntity<Product> productById(@PathVariable("productId") Long id){
		Product product = productService.getProductById(id);
		return new ResponseEntity<>(product ,HttpStatus.OK);
		
	}
}
