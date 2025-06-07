package com.sri.ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.ms.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
