package eSports_Arena_Manager.auth_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class CuentaDTO {

    private Long id;
    private String correo;
    private String rol;
    private String estado;
    private LocalDate fechaCreacion;


}
