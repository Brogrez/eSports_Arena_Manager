package eSports_Arena_Manager.sanction_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto {
    private Long userId;
    private String nickname;
    private String estado;
    private String rol;
}
