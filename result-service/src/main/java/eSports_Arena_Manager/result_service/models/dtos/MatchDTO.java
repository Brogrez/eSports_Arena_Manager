package com.duoc.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MatchDTO {

    private Long partidaId;
    private Long torneoId;
    private String equipoA;
    private String equipoB;
    private String estadoPartida;
}