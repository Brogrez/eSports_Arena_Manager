package eSports_Arena_Manager.auth_service_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Servicio de usuarios: guarda usuarios y roles, valida login y EMITE el JWT que usa todo el sistema.
@SpringBootApplication
public class MsvcUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcUsuariosApplication.class, args);
	}

}
