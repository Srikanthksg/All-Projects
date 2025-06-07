package com.sri.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sri.ms.dto.OrderResponseDTO;
import com.sri.ms.entity.Order;
import com.sri.ms.service.OrderService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping("/placeOrder")
	public Mono<ResponseEntity<OrderResponseDTO>> placeOrder(@RequestBody Order order){
		
		return orderService.placeOrder(order);
	}
	
	@GetMapping
	public List<Order> getAllOrders(){
		return orderService.getAllOrders();
	}
	
	
	
}
