package com.parking.proj_parking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parking.proj_parking_api.entity.Cliente;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {
    
}
