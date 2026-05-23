package com.example.match_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegistrationDto {
    private Long inscripcionId;
    private Long torneoId;
    private String estado;
}
