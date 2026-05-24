package eSports_Arena_Manager.auth_service.services;

import eSports_Arena_Manager.auth_service.models.Cuenta;
import eSports_Arena_Manager.auth_service.models.dtos.CuentaDTO;

import java.util.List;

public interface CuentaService {
    List<CuentaDTO> findAll();
    Cuenta findById(Long id);
    Cuenta findByCorreo(String correo);
    Cuenta save(Cuenta cuenta);
    Cuenta updateById(Long id, Cuenta cuenta);
    void deleteById(Long id);
    List<Cuenta>findByUsuarioId(Long idUsuario);

}
