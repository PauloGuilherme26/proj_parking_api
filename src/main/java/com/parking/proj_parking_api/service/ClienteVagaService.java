package com.parking.proj_parking_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.proj_parking_api.entity.ClienteVaga;
import com.parking.proj_parking_api.exception.EntityNotFoundException;
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

    @Transactional(readOnly = true)
    public ClienteVaga buscarPorRecibo(String recibo) {
        return repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException(
                    String.format("recibo '%s' não encontrado no sistema ou check-out já realizado", recibo)
                )
        );
    }

    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return repository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }
}
