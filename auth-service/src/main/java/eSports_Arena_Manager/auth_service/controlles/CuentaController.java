package eSports_Arena_Manager.auth_service.controlles;


import eSports_Arena_Manager.auth_service.models.Cuenta;
import eSports_Arena_Manager.auth_service.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cuentas")
@Validated
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<Cuenta>> findAll() {
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(cuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> findById(@PathVariable Long id){
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(cuentaService.findById(id));
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Cuenta>> findByRol(@PathVariable String rol){
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(cuentaService.findAll().stream().filter(c -> c.getRol().equalsIgnoreCase(rol)).toList());
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Cuenta> findByCorreo(@PathVariable String correo){
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(cuentaService.findByCorreo(correo));
    }
}
