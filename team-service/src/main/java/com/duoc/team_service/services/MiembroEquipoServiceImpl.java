package com.duoc.team_service.services;

import com.duoc.team_service.clients.UsuarioClient;
import com.duoc.team_service.exceptions.MiembroEquipoException;
import com.duoc.team_service.models.MiembroEquipo;
import com.duoc.team_service.models.dtos.MiembroEquipoDTO;
import com.duoc.team_service.models.dtos.UsuarioDTO;
import com.duoc.team_service.repositories.MiembroEquipoRepository;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiembroEquipoServiceImpl implements MiembroEquipoService {

    @Autowired
    private MiembroEquipoRepository miembroEquipoRepository;

    @Autowired
    private UsuarioClient usuarioClient;



    @Transactional(readOnly = true)
    public MiembroEquipo findByMiembroId(Long miembroId) {
        return this.miembroEquipoRepository.findByMiembroId(miembroId).orElseThrow(
                () -> new MiembroEquipoException("Miembro del equipo no encontrado")
        );
    }

    @Override
    public List<MiembroEquipoDTO> findAll() {
        return this.miembroEquipoRepository.findAll().stream().map(m -> {
            MiembroEquipoDTO miembroEquipoDTO = new MiembroEquipoDTO();
            miembroEquipoDTO.setMiembroId(m.getMiembroId());
            miembroEquipoDTO.setRolEnEquipo(m.getRolDentroEquipo());
            try{
                UsuarioDTO usuarioDTO = usuarioClient.findById(m.getUsuarioId());
            }catch(FeignException e){
                throw new MiembroEquipoException(e.getMessage());
            }
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setIdUsuario(m.getUsuarioId());
            miembroEquipoDTO.setUsuarioId(usuarioDTO.getIdUsuario());
            return miembroEquipoDTO;
        }).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MiembroEquipo> findByUsuarioId(Long usuarioId) {
        return this.miembroEquipoRepository.findByUsuarioId(usuarioId).stream().toList();
    }

    @Transactional(readOnly = true)
    @Override
    public MiembroEquipo findByRolDentroEquipo(String rolDentroEquipo) {
        return this.miembroEquipoRepository.findByRolDentroEquipo(rolDentroEquipo).orElseThrow(
                () -> new MiembroEquipoException("Rol dentro del equipo no encontrado")
        );
    }
    @Transactional(readOnly = true)
    @Override
    public MiembroEquipo findBymEquipoId(Long mEquipoId) {
        return this.miembroEquipoRepository.findBymEquipoId(mEquipoId).orElseThrow(
                () -> new MiembroEquipoException("Equipo no encontrado")
        );
    }

    @Transactional
    @Override
    public MiembroEquipo save(MiembroEquipo miembroEquipo) {
        if(this.miembroEquipoRepository.findByUsuarioId(miembroEquipo.getUsuarioId()).isPresent()){
            throw new MiembroEquipoException("Usuario ya es miembro de un equipo");
        }
        return this.miembroEquipoRepository.save(miembroEquipo);
    }

    @Transactional
    @Override
    public void deleteByMiembroId(MiembroEquipo miembroEquipo) {
      this.miembroEquipoRepository.delete(miembroEquipo);
    }

    @Transactional
    @Override
    public MiembroEquipo update(Long miembroEquipoId,MiembroEquipo miembroEquipo) {
        return this.miembroEquipoRepository.findByMiembroId(miembroEquipoId).map(m -> {
            m.setRolDentroEquipo(miembroEquipo.getRolDentroEquipo());
            m.setMiembroId(miembroEquipo.getMiembroId());
            m.setUsuarioId(miembroEquipo.getUsuarioId());
            m.setMEquipoId(miembroEquipo.getMEquipoId());
            return this.miembroEquipoRepository.save(m);
        }).orElseThrow(
                () -> new MiembroEquipoException("Miembro del equipo no encontrado")
        );
    }
}
