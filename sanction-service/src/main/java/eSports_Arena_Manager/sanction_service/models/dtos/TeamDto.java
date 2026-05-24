package eSports_Arena_Manager.sanction_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TeamDto {
    private Long teamId;
    private String name;
    private String estado;
}
