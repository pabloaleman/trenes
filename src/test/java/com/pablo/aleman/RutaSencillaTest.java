package com.pablo.aleman;

import com.pablo.aleman.excepciones.RutaSencillaMalDefinidaExcecion;
import com.pablo.aleman.modelo.RutaSencilla;
import org.junit.Assert;
import org.junit.Test;

public class RutaSencillaTest {
    private static final String DEFINICION_RUTA_SENCILLA = "AB3";
    private static final String DEFINICION_RUTA_SENCILLA_NO_VALIDA = "ABC";
    private static final String DEFINICION_RUTA_SENCILLA_NO_VALIDA_TAMANIO = "AB";

    @Test
    public void creacionRuta() {
        try {
            new RutaSencilla(DEFINICION_RUTA_SENCILLA);
        } catch (RutaSencillaMalDefinidaExcecion e) {
            Assert.fail();
        }
    }

    @Test(expected = RutaSencillaMalDefinidaExcecion.class)
    public void creacionRutaNoDistancia() throws RutaSencillaMalDefinidaExcecion {
        new RutaSencilla(DEFINICION_RUTA_SENCILLA_NO_VALIDA);
    }

    @Test(expected = RutaSencillaMalDefinidaExcecion.class)
    public void creacionRutaNoTamanio() throws RutaSencillaMalDefinidaExcecion {
        new RutaSencilla(DEFINICION_RUTA_SENCILLA_NO_VALIDA_TAMANIO);
    }

    @Test(expected = RutaSencillaMalDefinidaExcecion.class)
    public void creacionRutaNull() throws RutaSencillaMalDefinidaExcecion {
        new RutaSencilla(null);
    }

}
