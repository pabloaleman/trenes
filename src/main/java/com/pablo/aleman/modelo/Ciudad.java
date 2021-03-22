package com.pablo.aleman.modelo;


import com.pablo.aleman.excepciones.NoExisteRutaExcepcion;
import com.pablo.aleman.excepciones.RutaSencillaMalDefinidaExcecion;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class Ciudad {

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private Set<RutaSencilla> rutaSencillas;

    public Ciudad(String  nombre) {
        this.nombre = nombre;
    }

    public RutaSencilla evaluarExisteRuta(Ciudad ciudadDestino, String rutaOriginal) throws NoExisteRutaExcepcion {
        Optional<RutaSencilla> rutaSencillaOptional = rutaSencillas.stream()
                .filter(c -> c.getCiudadFin().equals(ciudadDestino)).findAny();

        if (rutaSencillaOptional.isPresent()) {
            return rutaSencillaOptional.get();
        } else {
            throw new NoExisteRutaExcepcion(rutaOriginal);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (Objects.nonNull(o) && o instanceof Ciudad) {
            Ciudad otra = (Ciudad) o;
            return Objects.equals(this.nombre, otra.nombre);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
