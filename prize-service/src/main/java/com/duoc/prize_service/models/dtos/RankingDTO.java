package com.duoc.prize_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RankingDTO {
    private Long rankingId;
    private Long torneoId;
    private Long participanteId;
    private Integer posicion;
    private Integer puntos;
}
