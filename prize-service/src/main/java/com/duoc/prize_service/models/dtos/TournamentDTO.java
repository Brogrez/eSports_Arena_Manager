package com.duoc.prize_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TournamentDTO {
    private Long torneoId;
    private String nombre;
    private String juego;
    private String estado;
}