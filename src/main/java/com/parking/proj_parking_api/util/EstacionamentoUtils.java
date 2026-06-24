package com.parking.proj_parking_api.util;

import java.time.LocalDateTime;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    //2023-03-16T15:23:48.616463500  -  LocalDateTime gera
    //20230316-152348

    public static String gerarRecibo() {
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);
        return recibo
                    .replace("-", "")
                    .replace(":", "")
                    .replace("T", "-");
    }                                              
}
