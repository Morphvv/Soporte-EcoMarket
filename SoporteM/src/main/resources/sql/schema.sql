CREATE TABLE IF NOT EXISTS personal_soporte (
    rut_personals BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    PRIMARY KEY (rut_personals),
    UNIQUE KEY uk_personal_soporte_email (email)
);

CREATE TABLE IF NOT EXISTS ticket_soporte (
    id_ticket BIGINT AUTO_INCREMENT PRIMARY KEY,
    run_cliente BIGINT NOT NULL,
    id_pedido BIGINT,
    asunto VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    tipo_solicitud VARCHAR(50) NOT NULL,
    canal VARCHAR(30) NOT NULL,
    prioridad VARCHAR(20) NOT NULL,
    estado_ticket VARCHAR(30) NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL,
    fecha_cierre DATETIME(6),
    personal_id BIGINT,
    CONSTRAINT fk_ticket_personal FOREIGN KEY (personal_id) REFERENCES personal_soporte (rut_personals)
);

CREATE TABLE IF NOT EXISTS mensaje_soporte (
    id_mensaje BIGINT AUTO_INCREMENT PRIMARY KEY,
    contenido VARCHAR(1000) NOT NULL,
    remitente VARCHAR(100) NOT NULL,
    tipo_remitente VARCHAR(30) NOT NULL,
    fecha_envio DATETIME(6) NOT NULL,
    ticket_id BIGINT NOT NULL,
    CONSTRAINT fk_mensaje_ticket FOREIGN KEY (ticket_id) REFERENCES ticket_soporte (id_ticket)
);

CREATE TABLE IF NOT EXISTS evidencia_adjunta (
    id_evidencia BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(200) NOT NULL,
    tipo_archivo VARCHAR(50) NOT NULL,
    url_archivo VARCHAR(500) NOT NULL,
    fecha_carga DATETIME(6) NOT NULL,
    id_ticket BIGINT NOT NULL,
    CONSTRAINT fk_evidencia_ticket FOREIGN KEY (id_ticket) REFERENCES ticket_soporte (id_ticket)
);

CREATE TABLE IF NOT EXISTS historial_estado_ticket (
    id_historial BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado_anterior VARCHAR(30),
    estado_nuevo VARCHAR(30) NOT NULL,
    fecha_cambio DATETIME(6) NOT NULL,
    usuario_responsable VARCHAR(100) NOT NULL,
    id_ticket BIGINT NOT NULL,
    CONSTRAINT fk_historial_ticket FOREIGN KEY (id_ticket) REFERENCES ticket_soporte (id_ticket)
);

CREATE TABLE IF NOT EXISTS reclamo (
    id_reclamo BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    motivo VARCHAR(200) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    estado_reclamo VARCHAR(30) NOT NULL,
    fecha_reclamo DATETIME(6) NOT NULL,
    ticket_id BIGINT NOT NULL,
    UNIQUE KEY uk_reclamo_ticket (ticket_id),
    CONSTRAINT fk_reclamo_ticket FOREIGN KEY (ticket_id) REFERENCES ticket_soporte (id_ticket)
);

CREATE TABLE IF NOT EXISTS solicitud_devolucion (
    id_solicitudd BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    cantidad INT NOT NULL,
    motivo VARCHAR(300) NOT NULL,
    estado_solicitud VARCHAR(30) NOT NULL,
    fecha_solicitud DATETIME(6) NOT NULL,
    ticket_id BIGINT NOT NULL,
    UNIQUE KEY uk_solicitud_devolucion_ticket (ticket_id),
    CONSTRAINT fk_solicitud_devolucion_ticket FOREIGN KEY (ticket_id) REFERENCES ticket_soporte (id_ticket)
);

CREATE TABLE IF NOT EXISTS resolucion_soporte (
    id_resolucion BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_resolucion VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    aprobado_por VARCHAR(100) NOT NULL,
    fecha_resolucion DATETIME(6) NOT NULL,
    id_ticket BIGINT NOT NULL,
    UNIQUE KEY uk_resolucion_soporte_ticket (id_ticket),
    CONSTRAINT fk_resolucion_soporte_ticket FOREIGN KEY (id_ticket) REFERENCES ticket_soporte (id_ticket)
);
