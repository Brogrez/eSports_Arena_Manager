
package eSports_Arena_Manager.result_service.models.dtos;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResultDTO {

    private Long id;
    private Long partidaId;
    private String puntaje;
    private String ganador;
    private String estado;
    private LocalDateTime fechaCreacion;
}
