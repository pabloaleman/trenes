package com.pablo.aleman.excepciones;

public class NoExisteRutaExcepcion extends Exception {

    private static final long serialVersionUID = -6572693881440238160L;
    private static final String MENSAJE = "No existe ruta desde %s hasta %s";
    private static final String MENSAJE_RUTA_COMPLETA = "No existe la ruta %s";

    public NoExisteRutaExcepcion(String ciudadInicio, String ciudadFin) {
        super(String.format(MENSAJE, ciudadInicio, ciudadFin));
    }

    public NoExisteRutaExcepcion(String rutaDefinida) {
        super(String.format(MENSAJE_RUTA_COMPLETA, rutaDefinida));
    }
}
