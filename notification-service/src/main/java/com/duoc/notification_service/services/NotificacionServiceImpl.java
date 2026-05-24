package com.duoc.notification_service.services;

import com.duoc.notification_service.clients.*;
import com.duoc.notification_service.exceptions.NotificacionException;
import com.duoc.notification_service.models.Notificacion;
import com.duoc.notification_service.models.dtos.*;
import com.duoc.notification_service.repositories.NotificacionRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    private PartidaClient  partidaClient;

    private PremioClient premioClient;

    private RegistroClient registroClient;

    private ResultadoClient  resultadoClient;

    private SancionClient  sancionClient;

    @Transactional(readOnly = true)
    @Override
    public Notificacion findByUsuarioId(Long usuarioId) {
        return this.notificacionRepository.findByUsuarioId(usuarioId).orElseThrow(
                () -> new NotificacionException("Usuario no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Notificacion findByEquipoId(Long equipoId){
        return this.notificacionRepository.findByEquipoId(equipoId).orElseThrow(
                () -> new NotificacionException("Equipo no encontrado")
        );

    }

    @Transactional(readOnly = true)
    @Override
    public List<NotificacionDTO> findAll() {

        return this.notificacionRepository.findAll().stream().map(n->{
            NotificacionDTO notificacionDTO = new NotificacionDTO();
            notificacionDTO.setNotificacionId(n.getNotificacionId());
            notificacionDTO.setTipo(n.getTipo());
            PartidaDTO partidaDTO = null;
            PremioDTO premioDTO = null;
            ResultadoDTO  resultadoDTO = null;
            SancionDTO sancionDTO = null;
            try{
                if(n.getTipo().equalsIgnoreCase("partida")){
                    partidaDTO = partidaClient.findbyId(n.getEquipoId());;;
                    notificacionDTO.setPartidaId(partidaDTO.getPartidaId());
                }else if(n.getTipo().equalsIgnoreCase("premio")){
                    premioDTO = premioClient.findbyId(n.getEquipoId());
                    notificacionDTO.setPremioId(premioDTO.getPremioId());;
                }else if(n.getTipo().equalsIgnoreCase("resultado")){
                    resultadoDTO = resultadoClient.findbyId(n.getEquipoId());
                    notificacionDTO.setResultadoId(resultadoDTO.getResultadoId());
                }else if(n.getTipo().equalsIgnoreCase("sancion")){
                    sancionDTO = sancionClient.findbyId(n.getEquipoId());
                    notificacionDTO.setSancionId(sancionDTO.getSancionId());;
                }


        }catch(FeignException ex){
                throw new NotificacionException(ex.getMessage());
            }
            notificacionDTO.setEquipoId(n.getEquipoId());
            notificacionDTO.setMensaje(n.getMensaje());
            notificacionDTO.setUsuarioId(n.getUsuarioId());
            notificacionDTO.setLeido(n.isLeido());
            return notificacionDTO;
         }).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Notificacion findBynotificacionId(Long notificacionId) {
        return this.notificacionRepository.findByNotificacionId(notificacionId).orElseThrow(
                () -> new NotificacionException("Notificacion no encontrado")
        );
    }

    @Transactional
    @Override
    public void deleteBynotificacionId(Long notificacionId) {
        this.notificacionRepository.deleteById(notificacionId);
    }

    @Override
    public Notificacion save(Notificacion notificacion) {
        try{
            if(notificacion.getTipo().equalsIgnoreCase("partida")){
                PartidaDTO partidaDTO = partidaClient.findbyId(notificacion.getEquipoId());
            }else if(notificacion.getTipo().equalsIgnoreCase("premio")){
                PremioDTO premioDTO = premioClient.findbyId(notificacion.getEquipoId());
            }else if(notificacion.getTipo().equalsIgnoreCase("resultado")){
                ResultadoDTO resultadoDTO = resultadoClient.findbyId(notificacion.getEquipoId());
            }else if(notificacion.getTipo().equalsIgnoreCase("sancion")){
                SancionDTO sancionDTO = sancionClient.findbyId(notificacion.getEquipoId());
            }
        }catch(FeignException ex){
            throw new NotificacionException(ex.getMessage());
        }

        return this.notificacionRepository.save(notificacion);
    }

    @Transactional
    @Override
    public Notificacion update(Long notificacionId,Notificacion notificacion) {
        return this.notificacionRepository.findByNotificacionId(notificacionId).map(n ->{
            n.setTipo(notificacion.getTipo());
            n.setEquipoId(notificacion.getEquipoId());
            n.setMensaje(notificacion.getMensaje());
            n.setUsuarioId(notificacion.getUsuarioId());
            n.setLeido(notificacion.isLeido());
            return this.notificacionRepository.save(n);
        }).orElseThrow(
                ()-> new NotificacionException("notificacion no encontrada")
        );
    }



    @Transactional
    @Override
    public Notificacion marcarComoLeido(Long id) {

        return this.notificacionRepository.findByNotificacionId(id).map(notif -> {
            notif.setLeido(true);
            return this.notificacionRepository.save(notif);

        }).orElseThrow(
                () -> new RuntimeException("Notificación no encontrada")
        );
    }
}
