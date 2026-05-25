package com.duoc.users_service.services;

import com.duoc.users_service.exceptions.UserException;
import com.duoc.users_service.models.User;
import com.duoc.users_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new UserException("User no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public User findByNickname(String nickname) {
        return this.userRepository.findByNickname(nickname).orElseThrow(
                () -> new UserException("User no encontrado")
        );
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(
                () -> new UserException("User no encontrado")
        );
    }

    @Transactional
    @Override
    public User save(User user) {
       if(this.userRepository.findByEmail(user.getEmail()).isPresent()){
           throw  new UserException("User ya existente");
       }
       if(this.userRepository.findByNickname(user.getNickname()).isPresent()){
           throw new UserException("User ya existe");
       }
       user.setFechaRegistro(LocalDate.now());
       user.setEstado("ACTIVO");
       return this.userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
    this.userRepository.deleteById(id);
    }

    @Override
    public User updateById(Long id, User user) {
        return this.userRepository.findById(id).map(e ->{
            e.setEmail(user.getEmail());
            e.setName(user.getName());
            e.setRol(user.getRol());
            e.setEstado(user.getEstado());
            return this.userRepository.save(e);
        }).orElseThrow(
                () -> new UserException("User no encontraod")
        );
    }
}
