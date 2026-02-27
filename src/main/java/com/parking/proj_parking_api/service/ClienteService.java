package com.parking.proj_parking_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.parking.proj_parking_api.entity.Cliente;
import com.parking.proj_parking_api.exception.CpfUniqueViolationException;
import com.parking.proj_parking_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;  //Injeção do repositório a partir de um Atributo/variável
    
    @Transactional
    public Cliente salvar (Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                String.format("CPF '%S' não pode ser cadastrado, já existe no sistema", cliente.getCpf())
            );
        }

    }


}
