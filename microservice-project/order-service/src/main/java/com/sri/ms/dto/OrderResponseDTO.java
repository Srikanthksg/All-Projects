package com.sri.ms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

	private Long orderId;
	private Long productId;
	private int quantity;
	private double totalPrice;
	
	// Product details
	
	private String productName;
	private double productPrice;
}
