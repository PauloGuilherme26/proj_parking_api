package com.parking.proj_parking_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.proj_parking_api.entity.Vaga;
import com.parking.proj_parking_api.exception.CodigoUniqueViolationException;
import com.parking.proj_parking_api.repository.VagaRepository;
import com.parking.proj_parking_api.exception.EntityNotFoundException;
import com.parking.proj_parking_api.exception.VagaDisponivelException;

import lombok.*;

@RequiredArgsConstructor
@Service
public class VagaService {
    
    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar (Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException("Vaga", vaga.getCodigo());            
        }
    }
    
    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo (String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
           () -> new EntityNotFoundException("Vaga", codigo)
        );
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorVagaLivre() {
        return vagaRepository.findFirstByStatus(Vaga.StatusVaga.LIVRE).orElseThrow(
            () -> new VagaDisponivelException()
        );
    }
}
