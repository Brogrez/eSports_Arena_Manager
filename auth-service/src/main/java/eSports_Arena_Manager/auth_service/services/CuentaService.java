package eSports_Arena_Manager.auth_service.services;

import eSports_Arena_Manager.auth_service.models.Cuenta;

import java.util.List;

public interface CuentaService {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    Cuenta findByCorreo(String correo);
    Cuenta save(Cuenta cuenta);
    Cuenta updateById(Long id, Cuenta cuenta);
    void deleteById(Long id);


}
