package com.parking.proj_parking_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parking.proj_parking_api.entity.ClienteVaga;

public interface ClienteVagaRepository extends JpaRepository <ClienteVaga, Long> {

    Optional<ClienteVaga> findByReciboAndDataSaidaIsNull(String recibo);

    long countByClienteCpfAndDataSaidaIsNotNull(String cpf);

}
