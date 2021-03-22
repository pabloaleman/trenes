package com.pablo.aleman.excepciones;

public class RutaSencillaMalDefinidaExcecion extends Exception {
    private static final long serialVersionUID = -4621401206109593999L;

    private static final String MENSAJE = "La cadena %s no pertenece a una definicion valida de ruta sencilla.";

    public RutaSencillaMalDefinidaExcecion(String definicion) {
        super(String.format(MENSAJE, definicion));
    }

    public RutaSencillaMalDefinidaExcecion(String definicion, Throwable t) {
        super(String.format(MENSAJE, definicion), t);
    }
}
