package com.parking.proj_parking_api.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;   //É uma biblioteca do JPA (Java Persistence API)
import lombok.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity                     //@Entity - Transforma uma classe em entidade para o banco de dados.
@Table(name = "clientes")   //@Table - cria tabela para o banco de dados.
                            //@Data - Cria tambem os Getters e Setters
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cliente implements Serializable {

    @Id                     //@Id - Cria um Id automático toda vez que criar uma tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue() -  Cria a estratégia de como será o Id.
    @Column(name = "id") // Atenção!!!
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;   

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    

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
        Cliente other = (Cliente) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
