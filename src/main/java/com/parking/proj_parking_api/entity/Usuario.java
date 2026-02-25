package com.parking.proj_parking_api.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;   //É uma biblioteca do JPA (Java Persistence API)
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity                     //@Entity - Transforma uma classe em entidade para o banco de dados.
@Table(name = "usuarios")   //@Table - cria tabela para o banco de dados.
                            //@Data - Cria tambem os Getters e Setters
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Usuario implements Serializable {

    @Id                     //@Id - Cria um Id automático toda vez que criar uma tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue() -  Cria a estratégia de como será o Id.
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;   

    @Column(name = "password", nullable = false, length = 200)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    private Role role = Role.ROLE_CLIENTE;

    //INSTRUÇÕES DE AUDITORIA
    @CreatedDate
    @Column(name = "data_criacao")    
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao; 

    @CreatedBy
    @Column(name = "criado_por")
    private String criadoPor;

    @LastModifiedBy
    @Column(name = "modificado_por")
    private String modificadoPor;

        
    public enum Role {      // enum é uma classe especial que representa um grupo fixo de constantes (valores imutáveis).  
        ROLE_ADMIN,
        ROLE_CLIENTE

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + "]";
    }

    
    


}
