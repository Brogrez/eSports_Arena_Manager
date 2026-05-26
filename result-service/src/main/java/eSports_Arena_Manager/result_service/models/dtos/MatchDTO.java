package eSports_Arena_Manager.result_service.models.dtos;

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
    private Long equipoA;
    private Long equipoB;
    private String round;
    private String estado;
}