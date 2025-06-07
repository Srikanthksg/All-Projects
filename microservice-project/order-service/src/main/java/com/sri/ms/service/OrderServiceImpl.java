package com.sri.ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sri.ms.dto.OrderResponseDTO;
import com.sri.ms.dto.ProductDTO;
import com.sri.ms.entity.Order;
import com.sri.ms.repository.OrderRepository;

import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Override
	public Mono<ResponseEntity<OrderResponseDTO>> placeOrder(Order order) {

		return webClientBuilder.build().get().uri("http://localhost:8081/products/"+order.getProductId())
				.retrieve()
				.bodyToMono(ProductDTO.class)
				.map(productDTO  ->  {
					OrderResponseDTO responseDTO = new OrderResponseDTO();
					
					responseDTO.setProductId(order.getProductId());
					responseDTO.setQuantity(order.getQuantity());

					// set product details
					responseDTO.setProductName(productDTO.getName());
					responseDTO.setProductPrice(productDTO.getPrice());
					responseDTO.setTotalPrice(order.getQuantity()* productDTO.getPrice());

					// save order details 
					orderRepository.save(order);
					responseDTO.setOrderId(order.getId());
					
					return ResponseEntity.ok(responseDTO);


				});
		 
	}

	@Override
	public List<Order> getAllOrders() {
		
		return orderRepository.findAll();
	}
	
	
	
	
}
