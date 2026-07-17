package com.parking.proj_parking_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.proj_parking_api.entity.ClienteVaga;
import com.parking.proj_parking_api.exception.ReciboCheckInNotFoundException;
import com.parking.proj_parking_api.jwt.JwtUserDetails;
import com.parking.proj_parking_api.repository.ClienteVagaRepository;
import com.parking.proj_parking_api.repository.projection.ClienteVagaProjection;

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
        ClienteVaga cv = repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new ReciboCheckInNotFoundException(recibo)              
        );
        
        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = jwtUserDetails.getRole().equals("ROLE_ADMIN");
        boolean isClienteDonoRecibo = cv.getCliente().getUsuario().getUsername().equals(jwtUserDetails.getUsername());

        if (!isAdmin && !isClienteDonoRecibo) {
            throw new AccessDeniedException("Acesso negado, este recibo não pertence a você");
        }

        return cv;
    }

    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return repository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorClienteCpf(String cpf, Pageable pageable) {
        return repository.findAllByClienteCpf(cpf, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorUsuarioId(long id, Pageable pageable) {
        return repository.findAllByClienteUsuarioId(id, pageable);
    }
}
