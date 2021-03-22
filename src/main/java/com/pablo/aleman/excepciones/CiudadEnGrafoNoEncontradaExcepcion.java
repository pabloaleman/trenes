package com.pablo.aleman.excepciones;

public class CiudadEnGrafoNoEncontradaExcepcion extends Exception {
    private static final long serialVersionUID = 3945685088201770526L;
    private static final String MENSAJE = "Ciudad con nombre %s no encontrada en el grafo";

    public CiudadEnGrafoNoEncontradaExcepcion(String nombreCiudad) {
        super(String.format(MENSAJE, nombreCiudad));
    }
}
