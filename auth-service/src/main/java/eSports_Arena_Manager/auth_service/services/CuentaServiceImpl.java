package eSports_Arena_Manager.auth_service.services;

import eSports_Arena_Manager.auth_service.clients.UsuarioClient;
import eSports_Arena_Manager.auth_service.exceptions.CuentaException;
import eSports_Arena_Manager.auth_service.models.Cuenta;
import eSports_Arena_Manager.auth_service.models.dtos.CuentaDTO;
import eSports_Arena_Manager.auth_service.models.dtos.UsuarioDTO;
import eSports_Arena_Manager.auth_service.repositories.CuentaRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Transactional(readOnly = true)
    @Override
    public List<CuentaDTO> findAll() {
        return this.cuentaRepository.findAll().stream().map(c -> {
            CuentaDTO cuentaDTO = new CuentaDTO();
            cuentaDTO.setId(c.getCuentaId());
            cuentaDTO.setCorreo(c.getCorreo());
            cuentaDTO.setRol(c.getRol());
            cuentaDTO.setEstado(c.getEstado());
            cuentaDTO.setFechaCreacion(c.getFechaCreacion());
            UsuarioDTO usuarioDTO = null;
            try{
                usuarioDTO = usuarioClient.findById(c.getUserId());
            }catch(FeignException e){
                throw new CuentaException(e.getMessage());
            }
            return cuentaDTO;

        }).toList();


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



    @Transactional
    @Override
    public Cuenta save(Cuenta cuenta) {
        try{
            UsuarioDTO usuarioDTO = this.usuarioClient.findById(cuenta.getUserId());
        } catch (FeignException exception) {
            throw new CuentaException("el usuario con id"+ cuenta.getUserId()+" no existe");
        }
        cuenta.setFechaCreacion(LocalDateTime.now());
        return this.cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta updateById(Long id, Cuenta cuenta) {
        return this.cuentaRepository.findById(id).map(c -> {
            c.setPasswordHash(cuenta.getPasswordHash());
            c.setCorreo(cuenta.getCorreo());
            c.setEstado(cuenta.getEstado());
            c.setRol(cuenta.getRol());
            c.setFechaCreacion(cuenta.getFechaCreacion());
            try{
                UsuarioDTO usuarioDTO = this.usuarioClient.findById(cuenta.getUserId());
            } catch (FeignException exception) {
                throw new CuentaException("el usuario con id"+ cuenta.getUserId()+" no existe");
            }

            return this.cuentaRepository.save(c);
        }).orElseThrow(
                () -> new CuentaException("Cuenta no encontrada")
        );
    }

    @Override
    public void deleteById(Long id) {
        this.cuentaRepository.deleteById(id);
    }

    @Override
    public List<Cuenta> findByUserId(Long userId) {
        return this.cuentaRepository.findByUserId(userId);
    }
}
