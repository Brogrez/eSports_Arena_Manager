package com.duoc.team_service.services;

import com.duoc.team_service.exceptions.MiembroEquipoException;
import com.duoc.team_service.models.MiembroEquipo;
import com.duoc.team_service.repositories.MiembroEquipoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiembroEquipoServiceImpl implements MiembroEquipoService {

    @Autowired
    private MiembroEquipoRepository miembroEquipoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<MiembroEquipo> findAll() {
        return this.miembroEquipoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public MiembroEquipo findByMiembroId(Long miembroId) {
        return this.miembroEquipoRepository.findByMiembroId(miembroId).orElseThrow(
                () -> new MiembroEquipoException("Miembro del equipo no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public MiembroEquipo findByUsuarioId(Long usuarioId) {
        return this.miembroEquipoRepository.findByUsuarioId(usuarioId).orElseThrow(
                () -> new MiembroEquipoException("Usuario no encontrado en el equipo")
        );
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
    public MiembroEquipo save(Long usuarioId,MiembroEquipo miembroEquipo) {
        if(this.miembroEquipoRepository.findByUsuarioId(usuarioId).isPresent()){
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
