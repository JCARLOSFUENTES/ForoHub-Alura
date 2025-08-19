# ForoHub (Spring Boot + PostgreSQL + JWT)

## Requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL 15+

## Configuración rápida
1. Crear base de datos:
   ```sql
   CREATE DATABASE forohub;
   ```
2. Edita `src/main/resources/application.properties` con tus credenciales de Postgres.
3. Ejecuta:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints
- `POST /auth/register` – body: `{ "username":"juan", "password":"1234" }`
- `POST /auth/login` – devuelve `{ "token": "..." }`
- Usa `Authorization: Bearer <token>` para:
  - `POST /topicos`
  - `GET /topicos`
  - `GET /topicos/{id}`
  - `PUT /topicos/{id}`
  - `DELETE /topicos/{id}`
