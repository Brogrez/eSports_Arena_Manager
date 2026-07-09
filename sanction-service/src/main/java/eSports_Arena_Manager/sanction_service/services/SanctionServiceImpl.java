package eSports_Arena_Manager.sanction_service.services;

import eSports_Arena_Manager.sanction_service.clients.TeamClient;
import eSports_Arena_Manager.sanction_service.clients.UserClient;
import eSports_Arena_Manager.sanction_service.exceptions.SanctionException;
import eSports_Arena_Manager.sanction_service.models.Sanction;
import eSports_Arena_Manager.sanction_service.repositories.SanctionRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SanctionServiceImpl implements SanctionService {

    @Autowired
    private SanctionRepository sanctionRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private TeamClient teamClient;

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findAll() {
        return this.sanctionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Sanction findById(Long id) {
        return this.sanctionRepository.findById(id).orElseThrow(
                () -> new SanctionException("ESA SANCION NO EXISTE")
        );
    }

    @Transactional
    @Override
    public Sanction save(Sanction sanction) {
        try{
            userClient.findById(sanction.getUsuarioId());
        } catch(FeignException e){
            throw new SanctionException("EL USUARIO NO EXISTE");
        }

        if(sanction.getEquipoId() != null){
            try{
                teamClient.findById(sanction.getEquipoId());
            } catch(FeignException e){
                throw new SanctionException("EL EQUIPO NO EXISTE");
            }
        }
        if(sanction.getFechaFin().isBefore(sanction.getFechaInicio())){
            throw new SanctionException("LA FECHA FIN DEBE SER POSTERIOR A LA FECHA INICIO");
        }
        sanction.setEstado("ACTIVA");
        return this.sanctionRepository.save(sanction);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        this.sanctionRepository.deleteById(id);
    }

    @Override
    public Sanction updateById(Long id, Sanction sanction) {
        return this.sanctionRepository.findById(id).map(s ->{
            s.setMotivo(sanction.getMotivo());
            s.setFechaFin(sanction.getFechaFin());
            s.setSeveridad(sanction.getSeveridad());
            s.setEstado(sanction.getEstado());

            return this.sanctionRepository.save(s);
        }).orElseThrow(
                () -> new SanctionException("sancion no existe")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findByUsuarioId(Long usuarioId) {
        return this.sanctionRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findByTeamId(Long teamId) {
        return this.sanctionRepository.findByEquipoId(teamId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findByEstado(String estado) {
        return this.sanctionRepository.findByEstado(estado);
    }

    @Transactional
    @Override
    public Sanction cerrar(Long id) {
        Sanction sanction = this.sanctionRepository.findById(id).orElseThrow(
                () -> new SanctionException("sancion no existe")
        );
        if(sanction.getEstado().equals("CERRADA")){
            throw new SanctionException("la sancion ya esta cerrada");
        }
        sanction.setEstado("CERRADA");
        return this.sanctionRepository.save(sanction);
    }
}