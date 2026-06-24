package com.parking.proj_parking_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.proj_parking_api.entity.ClienteVaga;
import com.parking.proj_parking_api.repository.ClienteVagaRepository;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {

    private final ClienteVagaRepository repository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga) {
        return repository.save(clienteVaga);
    }
}
