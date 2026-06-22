package com.parking.proj_parking_api.entity;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="clientes_tem_vagas")
@EntityListeners(AuditingEntityListener.class)
public class ClienteVaga {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="numero_recibo", nullable = false, unique = true, length = 15)
    private String recibo;

    @Column(name="placa", nullable = false, length = 8)
    private String placa;

    @Column(name="marca", nullable = false, length = 45)
    private String marca;

    @Column(name="modelo", nullable = false, length = 45)
    private String modelo;
    
    @Column(name="cor", nullable = false, length = 45)
    private String cor;

    @Column(name="data_entrada", nullable = false)
    private LocalDateTime dataEntrada;

    @Column(name="data_saida")
    private LocalDateTime dataSaida;

    @Column(name="valor", columnDefinition = "decimal(7,2)")    //7 dígitos com duas casas decimais
    private BigDecimal valor;

    @Column(name="desconto", columnDefinition = "decimal(7,2)")
    private BigDecimal desconto;

    @ManyToOne
    @JoinColumn(name="id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="id_vaga", nullable = false)
    private Vaga vaga;


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
        ClienteVaga other = (ClienteVaga) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }    
}
