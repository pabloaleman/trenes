package com.pablo.aleman.excepciones;

public class DefinicionGrafoErrorExcepcion extends Exception {

    private static final long serialVersionUID = 7610144605603348533L;

    private static final String MENSAJE = "La cadena %s no pertenece a una definicion valida de ruta sencilla, %s.";

    public DefinicionGrafoErrorExcepcion(String definicion, String mensaje) {
        super(String.format(MENSAJE, definicion, mensaje));
    }
}
