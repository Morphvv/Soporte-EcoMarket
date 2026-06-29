//Funciona en java 21
//Puerto 9004
//Juan Pablo Jofre

Soporte swagger: http://localhost:9004/swagger-ui/index.html

Primero se tiene que crear un ticket uwu

Crear personal de soporte body
{
  "rut": 12345678,
  "nombre": "Carlos",
  "apellido": "Ramírez",
  "email": "carlos.ramirez@ecomarket.cl",
  "rol": "ADMINISTRADOR",
  "estado": "ACTIVO"
}

Crear ticket de soporte body
{
  "runCliente": 12345678,
  "idPedido": 1,
  "asunto": "Problema con mi pedido recibido",
  "descripcion": "El producto llegó en mal estado y necesito ayuda para resolverlo.",
  "tipoSolicitud": "RECLAMO",
  "canal": "WEB",
  "prioridad": "ALTA"
}

Crear mensaje soporte body 
{
  "contenido": "Buenos días, adjunto fotos del producto dañado para su revisión.",
  "remitente": "Carlos Ramírez",
  "tipoRemitente": "CLIENTE"
}

Crear una evidencia body
{
  "nombreArchivo": "foto_producto_danado.jpg",
  "tipoArchivo": "IMAGEN",
  "urlArchivo": "https://storage.ecomarket.cl/evidencias/foto_producto_danado.jpg"
}

Crear una solicitud de devolucion body
{
  "idPedido": 1,
  "idProducto": 42,
  "cantidad": 2,
  "motivo": "El producto llegó roto y no corresponde a lo solicitado."
}

Crear un reclamo body
{
  "idPedido": 1,
  "idProducto": 42,
  "motivo": "Producto no corresponde al pedido",
  "descripcion": "Recibí un producto completamente diferente al que compré en la plataforma."
}


Crear una resolucion soporte body
{
  "tipoResolucion": "REEMBOLSO",
  "descripcion": "Se aprueba el reembolso total por producto dañado según política de devoluciones.",
  "aprobadoPor": "Supervisor Soporte"
}

