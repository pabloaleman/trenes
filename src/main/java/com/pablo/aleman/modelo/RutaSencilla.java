package com.pablo.aleman.modelo;

import com.pablo.aleman.constantes.Constantes;
import com.pablo.aleman.excepciones.RutaSencillaMalDefinidaExcecion;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class RutaSencilla {

    @Getter
    @Setter
    private Ciudad ciudadInicio;

    @Getter
    @Setter
    private Ciudad ciudadFin;

    @Getter
    @Setter
    private Integer distancia;

    public RutaSencilla(String definicionNodo) throws RutaSencillaMalDefinidaExcecion {
        if (Objects.isNull(definicionNodo) || definicionNodo.length() < Constantes.TAMANIO_MINIMO_DEFINICION) {
            throw new RutaSencillaMalDefinidaExcecion(definicionNodo);
        }
        ciudadInicio = new Ciudad(definicionNodo.substring(Constantes.INICIO, Constantes.POSICION_CIUDAD_1));
        ciudadFin = new Ciudad(definicionNodo.substring(Constantes.POSICION_CIUDAD_1, Constantes.POSICION_CIUDAD_2));
        try {
            distancia = Integer.parseInt(definicionNodo.substring(Constantes.POSICION_CIUDAD_2));
        } catch (NumberFormatException e) {
            throw new RutaSencillaMalDefinidaExcecion(definicionNodo, e);
        }
    }


    @Override
    public String toString() {
        return String.format("[Inicio: %s, Fin: %s, Distancia: %d ]", ciudadInicio.toString(),
                ciudadFin.toString(),
                distancia);
    }

    @Override
    public boolean equals(Object o) {
        if (Objects.nonNull(o) && o instanceof RutaSencilla) {
            RutaSencilla otra = (RutaSencilla) o;
            return Objects.equals(this.ciudadInicio, otra.ciudadInicio)
                    && Objects.equals(this.ciudadFin, otra.ciudadFin)
                    && Objects.equals(this.distancia, otra.distancia);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(ciudadInicio, ciudadFin, distancia);
    }
}
