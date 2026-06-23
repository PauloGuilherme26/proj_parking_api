package com.parking.proj_parking_api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.proj_parking_api.entity.Cliente;
import com.parking.proj_parking_api.entity.ClienteVaga;
import com.parking.proj_parking_api.entity.Vaga;
import com.parking.proj_parking_api.util.EstacionamentoUtils;

import lombok.*;

@RequiredArgsConstructor
@Service
public class EstacionamentoService {

    private ClienteVagaService clienteVagaService;
    private ClienteService clienteService;
    private VagaService vagaService;

    @Transactional
    public ClienteVaga checkIn (ClienteVaga clienteVaga) {
        Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf()); 
        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.buscarPorVagaLivre(); 
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());

        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

        return clienteVagaService.salvar(clienteVaga);
    }
}
