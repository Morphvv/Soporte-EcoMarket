package com.SoporteMicroServicio.SoporteM.exception;

//Para cuando solicito un recurso y no existe en la base de datos
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String mensaje){
        super(mensaje);
    }

    public ResourceNotFoundException(String entidad, Object id) {
        super(entidad + " no encontrado(a) con id: " + id);
    }
}
