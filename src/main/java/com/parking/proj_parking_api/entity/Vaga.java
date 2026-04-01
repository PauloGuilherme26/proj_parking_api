package com.parking.proj_parking_api.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;

@Entity
@Table (name = "vagas")
@EntityListeners (AuditingEntityListener.class)
public class Vaga implements Serializable {

@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long Id;

@Column (name = "codigo", nullable = false, unique = true, length = 4)
private String codigo;

@Enumerated (EnumType.STRING)
private Statusvaga status;

private enum Statusvaga {
    LIVRE, OCUPADA
}

}
