package com.duoc.prize_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class Audit {

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}