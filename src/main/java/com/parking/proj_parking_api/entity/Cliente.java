package com.parking.proj_parking_api.entity;

import java.io.Serializable;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;   //É uma biblioteca do JPA (Java Persistence API)
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity                     //@Entity - Transforma uma classe em entidade para o banco de dados.
@Table(name = "clientes")   //@Table - cria tabela para o banco de dados.
                            //@Data - Cria tambem os Getters e Setters
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cliente implements Serializable {

    @Id                     //@Id - Cria um Id automático toda vez que criar uma tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue() -  Cria a estratégia de como será o Id.
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;   

    @Column(name = "password", nullable = false, length = 200)
    private String password;




}
