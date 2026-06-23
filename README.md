# 🎮 eSports Arena Manager

Plataforma backend para organizar y gestionar torneos de videojuegos competitivos. Permite administrar juegos, jugadores, equipos, torneos, inscripciones, partidas, resultados, rankings, sanciones, premios y notificaciones.

Desarrollado como proyecto semestral para la asignatura **Desarrollo FullStack I (Backend) — DSY1103**, aplicando arquitectura de microservicios con Spring Boot.

---

## 👥 Integrantes

| Nombre | Microservicios desarrollados |
|--------|------------------------------|
| Nicolas Molina | user-service, tournament-service, match-service, ranking-service |
| Jose Hernandez | auth-service, team-service, registration-service, notification-service |
| Felipe Sanchez | game-service, sanction-service, result-service, prize-service |

---

## 🛠️ Tecnologías

| Tecnología | Uso |
|-----------|-----|
| Java 21 | Lenguaje base |
| Spring Boot 4.0.6 | Framework de cada microservicio |
| Spring Data JPA | Persistencia y acceso a datos |
| H2 Database | Base de datos en memoria por servicio |
| Spring Cloud OpenFeign | Comunicación entre microservicios |
| Bean Validation | Validaciones en entidades y DTOs |
| Maven | Gestión de dependencias y build |
| Postman | Pruebas de endpoints REST |
| GitHub | Control de versiones |
| IntelliJ IDEA | IDE de desarrollo |

---

## 🏗️ Arquitectura — Microservicios

| # | Microservicio | Puerto | Base de datos | Consume a |
|---|---------------|--------|---------------|-----------|
| 00 | `users-service` | 8000 | db_usuarios | Ninguno |
| 01 | `auth-service` | 8001 | db_auth | user-service |
| 02 | `game-service` | 8002 | db_juegos | Ninguno |
| 03 | `team-service` | 8003 | db_equipos | user-service, game-service |
| 04 | `tournament-service` | 8004 | db_torneos | game-service |
| 05 | `registration-service` | 8005 | db_inscripciones | tournament, team, user, sanction |
| 06 | `match-service` | 8006 | db_partidas | tournament-service, registration-service |
| 07 | `result-service` | 8007 | db_resultados | match-service |
| 08 | `ranking-service` | 8008 | db_rankings | tournament-service, result-service |
| 09 | `sanction-service` | 8009 | db_sanciones | user-service, team-service |
| 10 | `prize-service` | 8010 | db_premios | tournament-service, ranking-service |
| 11 | `notification-service` | 8011 | db_notificaciones | Ninguno |

---

## ✅ Requisitos previos

- Java 21 instalado
- Maven 3.8+ instalado
- IntelliJ IDEA (u otro IDE compatible con Spring Boot)
- Postman (para pruebas de endpoints)
- Git

---

## ▶️ Instrucciones de ejecución

> **Importante:** los microservicios deben levantarse en orden, ya que algunos dependen de otros para funcionar correctamente.

### Orden de inicio

```
1. users-service        (puerto 8000)
2. game-service         (puerto 8002)
3. auth-service         (puerto 8001)
4. team-service         (puerto 8003)
5. sanction-service     (puerto 8009)
6. tournament-service   (puerto 8004)
7. registration-service (puerto 8005)
8. match-service        (puerto 8006)
9. result-service       (puerto 8007)
10. ranking-service     (puerto 8008)
11. prize-service       (puerto 8010)
12. notification-service (puerto 8011)
```

### Pasos para levantar cada microservicio

1. Abrir IntelliJ IDEA
2. Ir a **File → Open** y seleccionar la carpeta del microservicio
3. Esperar que Maven descargue las dependencias
4. Ejecutar la clase `Application.java` del microservicio
5. Verificar en consola que aparezca `Started [Nombre]Application`

---

## 🌐 Endpoints principales

### users-service — `http://localhost:8000`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/usuarios` | Listar todos los usuarios |
| GET | `/api/v1/usuarios/{id}` | Buscar cuenta por ID |
| POST | `/api/v1/usuarios` | Crear cuenta |
| PUT | `/api/v1/usuarios/{id}` | Actualizar cuenta |
| DELETE | `/api/v1/usuarios/{id}` | Desactivar cuenta |

### auth-service — `http://localhost:8001`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/cuentas` | Listar cuentas |
| GET | `/api/v1/cuentas/{id}` | Buscar cuenta por ID |
| POST | `/api/v1/cuentas` | Crear cuenta |
| PUT | `/api/v1/cuentas/{id}` | Actualizar cuenta |
| DELETE | `/api/v1/cuentas/{id}` | Desactivar cuenta |

### game-service — `http://localhost:8002`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/juegos` | Listar todos los juegos |
| GET | `/api/v1/juegos/{id}` | Buscar juego por ID |
| POST | `/api/v1/juegos` | Crear juego |
| PUT | `/api/v1/juegos/{id}` | Actualizar juego |
| DELETE | `/api/v1/juegos/{id}` | Desactivar juego |

### team-service — `http://localhost:8003`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/equipos` | Listar todos los equipos |
| GET | `/api/v1/equipos/{id}` | Buscar equipo por ID |
| POST | `/api/v1/equipos` | Crear equipo |
| PUT | `/api/v1/equipos/{id}` | Actualizar equipo |
| DELETE | `/api/v1/equipos/{id}` | Desactivar equipo |

### tournament-service — `http://localhost:8004`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/torneos` | Listar todos los torneos |
| GET | `/api/v1/torneos/{id}` | Buscar torneo por ID |
| GET | `/api/v1/torneos/estado/{estado}` | Listar por estado |
| POST | `/api/v1/torneos` | Crear torneo |
| PUT | `/api/v1/torneos/{id}` | Actualizar torneo |
| PATCH | `/api/v1/torneos/{id}/cancelar` | Cancelar torneo |
| PATCH | `/api/v1/torneos/{id}/cerrar` | Cerrar torneo |

### registration-service — `http://localhost:8005`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/inscripciones` | Listar todas las inscripciones |
| GET | `/api/v1/inscripciones/{id}` | Buscar inscripción por ID |
| GET | `/api/v1/inscripciones/torneo/{torneoId}` | Listar por torneo |
| GET | `/api/v1/inscripciones/jugador/{jugadorId}` | Listar por jugador |
| POST | `/api/v1/inscripciones` | Crear inscripción |
| DELETE | `/api/v1/inscripciones/{id}` | Cancelar inscripción |

### match-service — `http://localhost:8006`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/matchs` | Listar todas las partidas |
| GET | `/api/v1/matchs/{id}` | Buscar partida por ID |
| GET | `/api/v1/matchs/torneo/{torneoId}` | Listar por torneo |
| GET | `/api/v1/matchs/estado/{estado}` | Listar por estado |
| POST | `/api/v1/matchs` | Crear partida |
| PUT | `/api/v1/matchs/{id}` | Actualizar partida |
| PATCH | `/api/v1/matchs/{id}/cancelar` | Cancelar partida |

### result-service — `http://localhost:8007`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/results` | Listar todos los resultados |
| GET | `/api/v1/results/{id}` | Buscar resultado por ID |
| GET | `/api/v1/results/partida/{partidaId}` | Buscar por partida |
| POST | `/api/v1/results` | Crear resultado |
| PUT | `/api/v1/results/{id}` | Actualizar resultado |
| DELETE | `/api/v1/results/{id}` | Eliminar resultado |

### ranking-service — `http://localhost:8008`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/rankings` | Listar todos los rankings |
| GET | `/api/v1/rankings/{id}` | Buscar ranking por ID |
| GET | `/api/v1/rankings/torneo/{torneoId}` | Listar por torneo ordenado |
| POST | `/api/v1/rankings` | Crear registro de ranking |
| PUT | `/api/v1/rankings/{id}` | Actualizar ranking |
| PATCH | `/api/v1/rankings/{id}/puntos` | Actualizar puntos |
| PATCH | `/api/v1/rankings/torneo/{torneoId}/cerrar` | Cerrar ranking |

### sanction-service — `http://localhost:8009`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/sanciones` | Listar todas las sanciones |
| GET | `/api/v1/sanciones/{id}` | Buscar sanción por ID |
| GET | `/api/v1/sanciones/cuenta/{usuarioId}` | Listar por cuenta |
| GET | `/api/v1/sanciones/equipo/{equipoId}` | Listar por equipo |
| POST | `/api/v1/sanciones` | Crear sanción |
| PUT | `/api/v1/sanciones/{id}` | Actualizar sanción |
| PATCH | `/api/v1/sanciones/{id}/cerrar` | Cerrar sanción |

### prize-service — `http://localhost:8010`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/premios` | Listar todos los premios |
| GET | `/api/v1/premios/{id}` | Buscar premio por ID |
| GET | `/api/v1/premios/torneo/{torneoId}` | Listar por torneo |
| POST | `/api/v1/premios` | Crear premio |
| PUT | `/api/v1/premios/{id}` | Actualizar premio |
| DELETE | `/api/v1/premios/{id}` | Eliminar premio |
| POST | `/api/v1/premios/{premioId}/asignar/{participanteId}` | Asignar premio |

### notification-service — `http://localhost:8011`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/notificaciones` | Listar todas las notificaciones |
| GET | `/api/v1/notificaciones/{id}` | Buscar notificación por ID |
| GET | `/api/v1/notificaciones/cuenta/{usuarioId}` | Listar por cuenta |
| POST | `/api/v1/notificaciones` | Crear notificación |
| PATCH | `/api/v1/notificaciones/{id}/leer` | Marcar como leída |
| DELETE | `/api/v1/notificaciones/{id}` | Archivar notificación |

---

## 🔄 Flujo de prueba completo (Postman)

Para probar el flujo completo del sistema, seguir este orden:

### 1. Crear cuenta
```json
POST http://localhost:8000/api/v1/users
{
    "name": "Nicolas Molina",
    "nickname": "nico123",
    "email": "nico@gmail.com",
    "rol": "JUGADOR",
    "estado": "ACTIVO"
}
```

### 2. Crear juego
```json
POST http://localhost:8002/api/v1/games
{
    "name": "Valorant",
    "genero": "FPS",
    "modalidad": "5v5",
    "jugadoresPorEquipo": 5
}
```

### 3. Crear equipo
```json
POST http://localhost:8003/api/v1/equipos
{
    "nombreEquipo": "Team Alpha",
    "capitanId": 1,
    "juegoPrincipalId": 1
}
```

### 4. Crear torneo
```json
POST http://localhost:8004/api/v1/tournaments
{
    "name": "Copa Verano 2026",
    "gameId": 1,
    "fechaInicio": "2026-08-01",
    "fechaFin": "2026-08-15",
    "cupoMaximo": 16,
    "modalidad": "5v5"
}
```

### 5. Cambiar torneo a EN_CURSO
```json
PUT http://localhost:8004/api/v1/tournaments/1
{
    "name": "Copa Verano 2026",
    "gameId": 1,
    "fechaInicio": "2026-08-01",
    "fechaFin": "2026-08-15",
    "cupoMaximo": 16,
    "modalidad": "5v5",
    "estado": "EN_CURSO"
}
```

### 6. Crear inscripción
```json
POST http://localhost:8005/api/v1/inscripciones
{
    "torneoId": 1,
    "equipoId": 1,
    "jugadorId": 1,
    "tipoParticipante": "EQUIPO",
    "fechaInscripcion": "2026-07-01"
}
```

### 7. Crear partida
```json
POST http://localhost:8006/api/v1/matchs
{
    "tourId": 1,
    "participanteAId": 1,
    "participanteBId": 2,
    "round": "Cuartos de final",
    "fechaHora": "2026-08-05T15:00:00"
}
```

### 8. Registrar resultado
```json
POST http://localhost:8007/api/v1/results
{
    "partidaId": 1,
    "teamAId": 1,
    "teamBId": 2,
    "scoreA": 13,
    "scoreB": 7
}
```

### 9. Registrar rankings
```json
POST http://localhost:8008/api/v1/rankings
{
    "tourId": 1,
    "participanteId": 1,
}
```

### 10. Registrar prizes
```json
POST http://localhost:8010/api/v1/prizes
{
    "torneoId": 1,
    "participanteId": 1,
    "posicion": 1,
    "descripcion": "torneo valorant 5v5",
    "valor": 1000.0

}
```

---

## 📁 Estructura del repositorio

```
eSports_Arena_Manager/
├── users-service/
├── auth-service/
├── game-service/
├── team-service/
├── tournament-service/
├── registration-service/
├── match-service/
├── result-service/
├── ranking-service/
├── sanction-service/
├── prize-service/
├── notification-service/
└── README.md
```

Cada microservicio sigue la misma estructura interna:

```
msvc-nombre/
└── src/main/java/com/duoc/nombre/
    ├── clients/          ← FeignClients hacia otros servicios
    ├── controllers/      ← Endpoints REST
    ├── exceptions/       ← Excepción personalizada + GlobalExceptionHandler
    ├── models/
    │   ├── dtos/         ← DTOs de otros servicios
    │   ├── Entidad.java  ← Entidad JPA
    │   └── Audit.java    ← createdAt / updatedAt automáticos
    ├── repositories/     ← JpaRepository
    ├── services/         ← Interfaz + ServiceImpl con lógica de negocio
    └── Application.java
```
---

## 📌 Notas importantes

- Todos los microservicios usan **H2 en memoria** (`jdbc:h2:mem`), por lo que los datos se pierden al reiniciar el servicio.
- Cada microservicio tiene su **propio esquema de base de datos independiente**.
- La comunicación entre servicios se realiza mediante **OpenFeign** con manejo de `FeignException` para errores de conexión y `FeignException.NotFound` para recursos inexistentes.
