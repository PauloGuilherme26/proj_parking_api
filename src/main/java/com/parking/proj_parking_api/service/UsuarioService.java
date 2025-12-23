package com.parking.proj_parking_api.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.parking.proj_parking_api.entity.Usuario;
import com.parking.proj_parking_api.exception.EntityNotFoundException;
import com.parking.proj_parking_api.exception.PasswordInvalidException;
import com.parking.proj_parking_api.exception.UsernameUniqueViolationException;
import com.parking.proj_parking_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

private final UsuarioRepository usuarioRepository;
private final PasswordEncoder passwordEncoder;

@Transactional
public Usuario salvar(Usuario usuario) {
    try {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    } catch(org.springframework.dao.DataIntegrityViolationException ex) {
       throw new UsernameUniqueViolationException(String.format("Username {%s} já cadastrado", usuario.getUsername())); 
     }
}

@Transactional(readOnly = true)
public Usuario buscarPorId(long id) {
    return usuarioRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Usuario {id=%s} não encontrado! ", id)));
}

@Transactional
public Usuario editarSenha(long id, String senhaAtual, String novaSenha, String confirmaSenha) {
    Usuario user = buscarPorId(id);
    if (!passwordEncoder.matches(senhaAtual, user.getPassword())){
        throw new PasswordInvalidException("Sua senha não confere!");
    }

    if (!novaSenha.equals(confirmaSenha)){
        throw new PasswordInvalidException("Nova senha não confere com a confirmação da senha!");
    }

    user.setPassword(passwordEncoder.encode(novaSenha));
    return user;
}

@Transactional(readOnly = true)
public List<Usuario> buscarTodos() {
    return usuarioRepository.findAll();
}

@Transactional(readOnly = true)
public Usuario buscarPorUsername(String username) {
   return usuarioRepository.findByUsername(username).orElseThrow(
        () -> new EntityNotFoundException(String.format("Usuario com %s não encontrado! ", username)));
}

@Transactional(readOnly = true)
public Usuario.Role buscarRolePorUsername(String username) {
    return usuarioRepository.findRoleByUsername(username); 
}
    
}
