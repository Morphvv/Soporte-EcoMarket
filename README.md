# Soporte EcoMarket

Microservicio encargado de gestionar la atención y el soporte posterior a una compra dentro del ecosistema **EcoMarket**. Permite administrar tickets, personal de soporte, mensajes, evidencias, reclamos, solicitudes de devolución, resoluciones y el historial de cambios de estado.

**Autor:** Juan Pablo Jofre  
**Puerto:** `9004`

## Funcionalidades principales

- Crear, consultar, clasificar, actualizar y cerrar tickets de soporte.
- Buscar tickets por cliente o estado.
- Registrar y administrar al personal de soporte.
- Enviar mensajes y respuestas dentro de un ticket.
- Adjuntar evidencias mediante una URL de archivo.
- Registrar y revisar reclamos asociados a tickets.
- Gestionar solicitudes de devolución de productos.
- Registrar resoluciones como reembolso, reemplazo, devolución o rechazo.
- Mantener un historial de los cambios de estado de cada ticket.
- Integrarse con los microservicios de **Usuarios** y **Pedidos**.
- Exponer documentación interactiva mediante Swagger/OpenAPI.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.4.1
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Cloud OpenFeign
- Resilience4j Circuit Breaker
- MySQL
- Lombok
- Swagger / Springdoc OpenAPI
- Maven
- JUnit y H2 para pruebas
- JaCoCo para cobertura
- Docker

## Requisitos

Antes de ejecutar el proyecto se necesita:

- Java 21
- MySQL
- Git
- Maven o el Maven Wrapper incluido
- Microservicio de Usuarios disponible en `http://localhost:9090`
- Microservicio de Pedidos disponible en `http://localhost:8084`

## Clonar el proyecto

```bash
git clone https://github.com/Morphvv/Soporte-EcoMarket.git
cd Soporte-EcoMarket/SoporteM
```

## Configuración de la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE SoporteDB
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

El perfil local se encuentra en:

```text
src/main/resources/application-dev.yml
```

La configuración predeterminada apunta a:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/SoporteDB
    username: root
    password:
```

Se deben ajustar el usuario y la contraseña de acuerdo con la configuración local de MySQL.

## Ejecutar el microservicio

### Windows

```powershell
.\mvnw.cmd clean spring-boot:run
```

### Linux o macOS

```bash
chmod +x mvnw
./mvnw clean spring-boot:run
```

Cuando el servicio inicie correctamente estará disponible en:

```text
http://localhost:9004
```

## Swagger

La documentación interactiva de la API se encuentra en:

```text
http://localhost:9004/swagger-ui/index.html
```

La especificación OpenAPI puede consultarse en:

```text
http://localhost:9004/v3/api-docs
```

## Endpoints principales

### Tickets de soporte

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/ticketSoporte/listar` | Lista todos los tickets. |
| `GET` | `/api/v1/ticketSoporte/listar/{id}` | Obtiene un ticket por ID. |
| `GET` | `/api/v1/ticketSoporte/cliente/{rutCliente}` | Lista los tickets de un cliente. |
| `GET` | `/api/v1/ticketSoporte/estado/{estado}` | Lista tickets según su estado. |
| `POST` | `/api/v1/ticketSoporte/crear` | Crea un nuevo ticket. |
| `PUT` | `/api/v1/ticketSoporte/clasificar/{idTicket}` | Asigna prioridad y personal de soporte. |
| `PUT` | `/api/v1/ticketSoporte/cambiarEstado/{idTicket}` | Cambia el estado del ticket. |
| `PUT` | `/api/v1/ticketSoporte/cerrar/{idTicket}?usuarioResponsable={nombre}` | Cierra un ticket. |
| `DELETE` | `/api/v1/ticketSoporte/eliminar/{idTicket}` | Elimina un ticket. |

### Personal de soporte

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/personalSoporte/listar` | Lista todo el personal. |
| `GET` | `/api/v1/personalSoporte/obtener/{rutPersonal}` | Obtiene personal por RUT. |
| `POST` | `/api/v1/personalSoporte/crear` | Registra personal de soporte. |
| `PUT` | `/api/v1/personalSoporte/actualizar/{rutPersonal}` | Actualiza los datos del personal. |
| `DELETE` | `/api/v1/personalSoporte/eliminar/{rutPersonal}` | Elimina personal por RUT. |

### Mensajes

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/mensajeSoporte/listar/{idTicket}` | Lista los mensajes de un ticket. |
| `GET` | `/api/v1/mensajeSoporte/{idMensaje}` | Obtiene un mensaje por ID. |
| `POST` | `/api/v1/mensajeSoporte/enviar/{idTicket}` | Envía un mensaje al ticket. |
| `POST` | `/api/v1/mensajeSoporte/responder/{idMensaje}` | Responde un mensaje existente. |
| `DELETE` | `/api/v1/mensajeSoporte/eliminar/{idMensaje}` | Elimina un mensaje. |

### Evidencias adjuntas

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/evidenciaAdjunta/listar/{idTicket}` | Lista las evidencias de un ticket. |
| `GET` | `/api/v1/evidenciaAdjunta/obtener/{idEvidencia}` | Obtiene una evidencia por ID. |
| `POST` | `/api/v1/evidenciaAdjunta/adjuntar/{idTicket}` | Adjunta una evidencia al ticket. |
| `DELETE` | `/api/v1/evidenciaAdjunta/eliminar/{idEvidencia}` | Elimina una evidencia. |

### Reclamos

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/reclamo/listar` | Lista todos los reclamos. |
| `GET` | `/api/v1/reclamo/obtener/{idReclamo}` | Obtiene un reclamo por ID. |
| `POST` | `/api/v1/reclamo/registrar/{idTicket}` | Registra un reclamo para un ticket. |
| `PUT` | `/api/v1/reclamo/revisar/{idReclamo}` | Marca un reclamo como revisado. |
| `PUT` | `/api/v1/reclamo/actualizar/{idReclamo}?nuevoEstado={estado}` | Actualiza el estado del reclamo. |

### Solicitudes de devolución

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/solicitudDevolucion/listar` | Lista todas las solicitudes. |
| `GET` | `/api/v1/solicitudDevolucion/listar/{idSolicitud}` | Obtiene una solicitud por ID. |
| `POST` | `/api/v1/solicitudDevolucion/registrar/{idTicket}` | Registra una solicitud para un ticket. |
| `GET` | `/api/v1/solicitudDevolucion/validar/{idSolicitud}` | Valida los datos del producto. |
| `PUT` | `/api/v1/solicitudDevolucion/aprobar/{idSolicitud}` | Aprueba una solicitud. |
| `PUT` | `/api/v1/solicitudDevolucion/rechazar/{idSolicitud}` | Rechaza una solicitud. |

### Resoluciones

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/resolucionSoporte/listar` | Lista todas las resoluciones. |
| `GET` | `/api/v1/resolucionSoporte/listar/{idResolucion}` | Obtiene una resolución por ID. |
| `POST` | `/api/v1/resolucionSoporte/registrar/{idTicket}` | Registra una resolución para un ticket. |
| `PUT` | `/api/v1/resolucionSoporte/modificar/{idResolucion}` | Modifica una resolución. |

### Historial de estados

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/historialEstadoTicket/listar/{idTicket}` | Lista el historial de un ticket. |
| `GET` | `/api/v1/historialEstadoTicket/{idHistorial}` | Obtiene un registro del historial. |

## Ejemplos de solicitudes

### 1. Crear personal de soporte

**POST** `/api/v1/personalSoporte/crear`

```json
{
  "rut": 12345678,
  "nombre": "Carlos",
  "apellido": "Ramírez",
  "email": "carlos.ramirez@ecomarket.cl",
  "rol": "ADMINISTRADOR",
  "estado": "ACTIVO"
}
```

### 2. Crear un ticket

**POST** `/api/v1/ticketSoporte/crear`

```json
{
  "runCliente": 12345678,
  "idPedido": 1,
  "asunto": "Problema con mi pedido recibido",
  "descripcion": "El producto llegó en mal estado y necesito ayuda para resolverlo.",
  "tipoSolicitud": "RECLAMO",
  "canal": "WEB",
  "prioridad": "ALTA"
}
```

> **Importante:** el atributo se llama `runCliente` porque ese es el nombre definido actualmente en el DTO del proyecto.

### 3. Clasificar un ticket

**PUT** `/api/v1/ticketSoporte/clasificar/1`

```json
{
  "prioridad": "ALTA",
  "idPersonal": 1
}
```

### 4. Cambiar el estado de un ticket

**PUT** `/api/v1/ticketSoporte/cambiarEstado/1`

```json
{
  "nuevoEstado": "EN_PROCESO",
  "usuarioResponsable": "Carlos Ramírez"
}
```

### 5. Enviar un mensaje

**POST** `/api/v1/mensajeSoporte/enviar/1`

```json
{
  "contenido": "Buenos días, adjunto fotos del producto dañado para su revisión.",
  "remitente": "Carlos Ramírez",
  "tipoRemitente": "CLIENTE"
}
```

### 6. Adjuntar una evidencia

**POST** `/api/v1/evidenciaAdjunta/adjuntar/1`

```json
{
  "nombreArchivo": "foto_producto_danado.jpg",
  "tipoArchivo": "IMAGEN",
  "urlArchivo": "https://storage.ecomarket.cl/evidencias/foto_producto_danado.jpg"
}
```

Este endpoint registra la información y la URL de la evidencia; no recibe directamente un archivo multipart.

### 7. Registrar un reclamo

**POST** `/api/v1/reclamo/registrar/1`

```json
{
  "idPedido": 1,
  "idProducto": 42,
  "motivo": "Producto no corresponde al pedido",
  "descripcion": "Recibí un producto diferente al que compré en la plataforma."
}
```

### 8. Registrar una solicitud de devolución

**POST** `/api/v1/solicitudDevolucion/registrar/1`

```json
{
  "idPedido": 1,
  "idProducto": 42,
  "cantidad": 2,
  "motivo": "El producto llegó roto y no corresponde a lo solicitado."
}
```

### 9. Registrar una resolución

**POST** `/api/v1/resolucionSoporte/registrar/1`

```json
{
  "tipoResolucion": "REEMBOLSO",
  "descripcion": "Se aprueba el reembolso total por el producto dañado.",
  "aprobadoPor": "Supervisor de Soporte"
}
```

## Valores permitidos

| Campo | Valores |
|---|---|
| Tipo de solicitud | `CONSULTA`, `RECLAMO`, `DEVOLUCION`, `SOPORTE_TECNICO` |
| Canal | `WEB`, `EMAIL`, `TELEFONO`, `CHAT` |
| Prioridad | `BAJA`, `MEDIA`, `ALTA`, `CRITICA` |
| Estado del ticket | `ABIERTO`, `EN_PROCESO`, `RESUELTO`, `CERRADO`, `CANCELADO` |
| Rol del personal | `AGENTE`, `SUPERVISOR`, `ADMINISTRADOR` |
| Estado del personal | `ACTIVO`, `INACTIVO` |
| Tipo de remitente | `CLIENTE`, `PERSONAL_SOPORTE`, `SISTEMA` |
| Tipo de evidencia | `IMAGEN`, `PDF`, `VIDEO`, `DOCUMENTO` |
| Tipo de resolución | `REEMBOLSO`, `REEMPLAZO`, `DEVOLUCION`, `RECHAZO` |

## Flujo sugerido de uso

1. Registrar al personal de soporte.
2. Crear un ticket para un cliente y, si corresponde, asociarlo a un pedido.
3. Clasificar el ticket y asignar al personal responsable.
4. Agregar mensajes y evidencias durante la atención.
5. Registrar un reclamo o una solicitud de devolución cuando corresponda.
6. Registrar la resolución aplicada.
7. Cambiar el estado y cerrar el ticket.
8. Consultar el historial para revisar la trazabilidad del caso.

## Pruebas

Ejecutar las pruebas automatizadas:

### Windows

```powershell
.\mvnw.cmd test
```

### Linux o macOS

```bash
./mvnw test
```

Para ejecutar las pruebas y generar el reporte de cobertura con JaCoCo:

```bash
./mvnw clean verify
```

El reporte se genera en:

```text
target/site/jacoco/index.html
```

## Compilar el proyecto

```bash
./mvnw clean package
```

El archivo ejecutable se genera dentro de la carpeta `target` y puede iniciarse con:

```bash
java -jar target/SoporteM-0.0.1-SNAPSHOT.jar
```

## Docker

El proyecto incluye un `Dockerfile` de construcción por etapas.

Crear la imagen:

```bash
docker build -t soporte-ecomarket .
```

Ejecutar usando el perfil de producción:

```bash
docker run --rm -p 9004:9004 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/SoporteDB \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=tu_clave \
  -e USUARIO_SERVICE_URL=http://host.docker.internal:9090 \
  -e PEDIDO_SERVICE_URL=http://host.docker.internal:8084 \
  soporte-ecomarket
```

Las direcciones de los servicios deben ajustarse según el entorno donde se ejecuten los contenedores.

## Variables de entorno para producción

| Variable | Descripción |
|---|---|
| `DB_URL` | URL de conexión a MySQL. |
| `DB_USERNAME` | Usuario de la base de datos. |
| `DB_PASSWORD` | Contraseña de la base de datos. |
| `USUARIO_SERVICE_URL` | URL del microservicio de Usuarios. |
| `PEDIDO_SERVICE_URL` | URL del microservicio de Pedidos. |
