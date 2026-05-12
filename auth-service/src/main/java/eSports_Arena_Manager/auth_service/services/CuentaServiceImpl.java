package eSports_Arena_Manager.auth_service.services;

import eSports_Arena_Manager.auth_service.exceptions.CuentaException;
import eSports_Arena_Manager.auth_service.models.Cuenta;
import eSports_Arena_Manager.auth_service.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Cuenta> findAll() {
        return this.cuentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Cuenta findById(Long id) {
        return this.cuentaRepository.findById(id).orElseThrow(
                () -> new CuentaException("Cuenta no encontrada")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Cuenta findByCorreo(String correo) {
        return this.cuentaRepository.findByCorreo(correo).orElseThrow(
                () -> new CuentaException("Cuenta no encontrada")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Cuenta findByUsuario(String usuario) {
        return this.cuentaRepository.findByUsuario(usuario).orElseThrow(
                () -> new CuentaException("Cuenta no encontrada")
        );
    }

    @Transactional
    @Override
    public Cuenta save(Cuenta cuenta) {
        if (this.findByCorreo(cuenta.getCorreo()) != null) {
            throw new CuentaException("Cuenta ya existente");
        }
        if(this.findByUsuario(cuenta.getUsuario()) != null) {
            throw new CuentaException("Cuenta ya existe");
        }
        return this.cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta updateById(Long id, Cuenta cuenta) {
        return this.cuentaRepository.findById(id).map(c -> {
            c.setUsuario(cuenta.getUsuario());
            c.setCorreo(cuenta.getCorreo());
            c.setContrasenia(cuenta.getContrasenia());
            return this.cuentaRepository.save(c);
        }).orElseThrow(
                () -> new CuentaException("Cuenta no encontrada")
        );
    }

    @Override
    public void deleteById(Long id) {
        this.cuentaRepository.deleteById(id);
    }
}
