package com.parking.proj_parking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parking.proj_parking_api.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    
}
