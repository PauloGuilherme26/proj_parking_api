package com.parking.proj_parking_api.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstacionamentoResponseDto {       
    
    private String placa;
    
    private String marca;
    
    private String modelo;    
    
    private String cor;

    private String clienteCpf;

    private String recibo;
    
    private LocalDateTime dataEntrada;
    
    private LocalDateTime dataSaida;

    private String vagaCodigo;
    
    private BigDecimal valor;
    
    private BigDecimal desconto;

}
