package com.sri.ms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sri.ms.dto.OrderResponseDTO;
import com.sri.ms.entity.Order;

import reactor.core.publisher.Mono;

public interface OrderService {

	Mono<ResponseEntity<OrderResponseDTO>> placeOrder(Order order);

	List<Order> getAllOrders();

}
