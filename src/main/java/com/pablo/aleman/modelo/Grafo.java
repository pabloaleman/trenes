package com.pablo.aleman.modelo;

import com.pablo.aleman.constantes.Constantes;
import com.pablo.aleman.excepciones.BuscarRutaExcepcion;
import com.pablo.aleman.excepciones.CiudadEnGrafoNoEncontradaExcepcion;
import com.pablo.aleman.excepciones.DefinicionGrafoErrorExcepcion;
import com.pablo.aleman.excepciones.NoExisteRutaExcepcion;
import com.pablo.aleman.excepciones.RutaSencillaMalDefinidaExcecion;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Grafo {
    private static final String DEFINICION_SIN_RUTAS_VALIDAS_MENSAJE = "no hay rutas validos en la definicion";
    private static final String INICIO_DIFERENTE_FIN_MENSAJE = "La ciudad de inicio debe ser diferente a la de fin";
    private static final Logger LOGGER = LoggerFactory.getLogger(Grafo.class);

    @Getter
    @Setter
    private Set<Ciudad> ciudades;


    public Grafo(String definicionGrafo) throws DefinicionGrafoErrorExcepcion {

        if (Objects.isNull(definicionGrafo) || definicionGrafo.length() < Constantes.TAMANIO_MINIMO_DEFINICION) {
            throw new DefinicionGrafoErrorExcepcion(definicionGrafo, DEFINICION_SIN_RUTAS_VALIDAS_MENSAJE);
        }


        ciudades = new HashSet<>();
        Set<RutaSencilla> rutasSencillas = new HashSet<>();
        String [] rutasString = definicionGrafo.split(Constantes.SEPARADOR_RUTAS_DEFINICION_GRAFO);

        // creo las rutas y ciudades vacias
        for (String rutaString :  rutasString) {
            try {
                RutaSencilla rutaSencilla = new RutaSencilla(rutaString);
                rutasSencillas.add(rutaSencilla);
                //agrego las ciudades
                ciudades.add(rutaSencilla.getCiudadInicio());
                ciudades.add(rutaSencilla.getCiudadFin());
            } catch (RutaSencillaMalDefinidaExcecion e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (rutasSencillas.isEmpty() || ciudades.isEmpty()) {
            throw new DefinicionGrafoErrorExcepcion(definicionGrafo, DEFINICION_SIN_RUTAS_VALIDAS_MENSAJE);
        }

        //proceso las rutas obtenidas
        for (RutaSencilla rutaSencilla : rutasSencillas) {
            Ciudad ciudadInicio = ciudades.stream()
                    .filter(c -> c.equals(rutaSencilla.getCiudadInicio()))
                    .findAny().get();

            Ciudad ciudadFin = ciudades.stream()
                    .filter(c -> c.equals(rutaSencilla.getCiudadFin()))
                    .findAny().get();

            rutaSencilla.setCiudadInicio(ciudadInicio);
            rutaSencilla.setCiudadFin(ciudadFin);

            if (Objects.isNull(ciudadInicio.getRutaSencillas())) {
                ciudadInicio.setRutaSencillas(new HashSet<>());
            }
            ciudadInicio.getRutaSencillas().add(rutaSencilla);

            ciudades.add(ciudadInicio);
            ciudades.add(ciudadFin);
        }
    }

    public void buscarRutas(Ciudad inicio, Ciudad fin,
                                              Queue<RutaSencilla> rutasVisitadas,
                                              Set<Queue<RutaSencilla>> rutasEncontradas) {

        for (RutaSencilla rutaSencilla : inicio.getRutaSencillas()) {
            Queue<RutaSencilla> rutasTemporales = new LinkedList<>(rutasVisitadas);

            // Si ya se ha visitado la ruta debe llegar hasta ahi para evitar lazos infinitos
            if (rutasVisitadas.contains(rutaSencilla)) {
                break;
            }

            rutasTemporales.add(rutaSencilla);

            // Cuando ya se encuentra la ciudad destino
            if (rutaSencilla.getCiudadFin().equals(fin)) {
                rutasEncontradas.add(rutasTemporales);
                break;
            } else {
                buscarRutas(rutaSencilla.getCiudadFin(), fin, rutasTemporales, rutasEncontradas);
            }
        }
    }

    public Integer calcularDistanciaDeRuta(String definicionRuta)
            throws CiudadEnGrafoNoEncontradaExcepcion, RutaSencillaMalDefinidaExcecion, NoExisteRutaExcepcion {
        Queue<RutaSencilla> rutas = evaluarRuta(definicionRuta);
        return rutas.stream().map(RutaSencilla::getDistancia).collect(Collectors.toList())
                .stream().reduce(0, Integer :: sum);
    }

    public Queue<RutaSencilla> evaluarRuta(String definicionRuta)
            throws CiudadEnGrafoNoEncontradaExcepcion, RutaSencillaMalDefinidaExcecion, NoExisteRutaExcepcion {

        Queue<RutaSencilla> rutas = new LinkedList<>();
        String [] ciudades = definicionRuta.split(Constantes.SEPARADOR_RUTAS_DEFINICION_RUTA);
        for (int indice = 0; indice < ciudades.length - 1; indice++) {
            Ciudad ciudadInicio = obtenerCiudadDeGrafoPorNombre(ciudades[indice]);
            Ciudad ciudadFin = obtenerCiudadDeGrafoPorNombre(ciudades[indice + 1]);
            RutaSencilla rutaSencilla = ciudadInicio.evaluarExisteRuta(ciudadFin, definicionRuta);
            rutas.add(rutaSencilla);
        }
        return rutas;
    }

    public Ciudad obtenerCiudadDeGrafoPorNombre(String nombreCiudad) throws CiudadEnGrafoNoEncontradaExcepcion {
        Ciudad ciudad = new Ciudad(nombreCiudad);
        Optional<Ciudad> ciudadOptional = ciudades.stream()
                .filter(c -> c.equals(ciudad))
                .findAny();
        if (ciudadOptional.isPresent()) {
            return ciudadOptional.get();
        } else {
            throw new CiudadEnGrafoNoEncontradaExcepcion(nombreCiudad);
        }
    }

    public int calcularDistanciaCorta(Set<Queue<RutaSencilla>> rutas) {
        int menor = Integer.MAX_VALUE;
        for (Queue<RutaSencilla> rutasSencillas : rutas) {
            int distancia = calcularDistanciaColaRuta(rutasSencillas);
            if (distancia < menor) {
                menor = distancia;
            }
        }
        return menor;
    }

    public int calcularDistanciaColaRuta(Queue<RutaSencilla> rutas) {
        return rutas.stream().map(r -> r.getDistancia()).collect(Collectors.toList()).stream().reduce(0, Integer :: sum);
    }


}
