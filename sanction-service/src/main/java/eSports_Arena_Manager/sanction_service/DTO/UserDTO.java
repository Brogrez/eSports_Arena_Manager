package eSports_Arena_Manager.sanction_service.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String status;
}