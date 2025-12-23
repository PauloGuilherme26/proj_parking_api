package com.parking.proj_parking_api.jwt;

import com.parking.proj_parking_api.entity.Usuario;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private Usuario Usuario;

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.Usuario = usuario;
    }

    public long getId() {
        return this.Usuario.getId();
    }

    public String getRole() {
        return this.Usuario.getRole().name();
    }


}
