package com.pablo.aleman;

import com.pablo.aleman.excepciones.BuscarRutaExcepcion;
import com.pablo.aleman.excepciones.CiudadEnGrafoNoEncontradaExcepcion;
import com.pablo.aleman.excepciones.DefinicionGrafoErrorExcepcion;
import com.pablo.aleman.excepciones.NoExisteRutaExcepcion;
import com.pablo.aleman.excepciones.RutaSencillaMalDefinidaExcecion;
import com.pablo.aleman.modelo.Ciudad;
import com.pablo.aleman.modelo.Grafo;
import com.pablo.aleman.modelo.RutaSencilla;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class GrafoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrafoTest.class);

    private static final String DEFINICION_GRAFO = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7";
    private static final String DEFINICION_GRAFO_NO_VALIDO = "ABC";
    private static final String NOMBRE_CIUDAD_A = "A";
    private static final String NOMBRE_CIUDAD_B = "B";
    private static final String NOMBRE_CIUDAD_C = "C";
    private static final int N_CIUDADES = 5;
    private static final int N_RUTAS_DESDE_A = 3;
    private static final int N_RUTAS_DESDE_A_HASTA_B = 4;
    private static final int N_PARADAS_MAXIMAS = 30;

    private static final String RUTA_EVALUAR = "A-B-C";
    private static final int DISTANCIA_RUTA_EVALUAR = 9;
    private static final String RUTA_EVALUAR_NO_VALIDA = "A-E-D";
    private static final String RUTA_EVALUAR_CIUDAD_NO_EXISTENTE = "A-E-G";


    private Grafo grafo;

    @Before
    public void inicioGrafo() {
        try {
            grafo = new Grafo(DEFINICION_GRAFO);
        } catch (DefinicionGrafoErrorExcepcion e) {
            Assert.fail();
        }

    }

    @Test
    public void creacionGrafo() {
        try {
            Ciudad ciudadA = grafo.obtenerCiudadDeGrafoPorNombre(NOMBRE_CIUDAD_A);
            Assert.assertEquals(N_CIUDADES, grafo.getCiudades().size());
            Assert.assertEquals(N_RUTAS_DESDE_A, ciudadA.getRutaSencillas().size());
        } catch (CiudadEnGrafoNoEncontradaExcepcion e) {
            Assert.fail();
        }
    }

    @Test(expected = DefinicionGrafoErrorExcepcion.class)
    public void creacionGrafoNoValido() throws DefinicionGrafoErrorExcepcion {
        new Grafo(DEFINICION_GRAFO_NO_VALIDO);
    }

    @Test(expected = DefinicionGrafoErrorExcepcion.class)
    public void creacionGrafoNull() throws DefinicionGrafoErrorExcepcion {
        new Grafo(null);
    }

    @Test
    public void buscarRutas() {
        try {
            Set<Queue<RutaSencilla>> rutasEncontradas = calcularRutas(NOMBRE_CIUDAD_A, NOMBRE_CIUDAD_B, true);
            Assert.assertEquals(N_RUTAS_DESDE_A_HASTA_B, rutasEncontradas.size());
        } catch (BuscarRutaExcepcion | CiudadEnGrafoNoEncontradaExcepcion e) {
            Assert.fail();
        }
    }

    @Test
    public void evaluarRuta() {
        try {
            Queue<RutaSencilla> rutas = grafo.evaluarRuta(RUTA_EVALUAR);
            Assert.assertEquals(2, rutas.size());
        } catch (CiudadEnGrafoNoEncontradaExcepcion | RutaSencillaMalDefinidaExcecion | NoExisteRutaExcepcion e) {
            Assert.fail();
        }
    }

    @Test(expected = NoExisteRutaExcepcion.class)
    public void evaluarRutaNoExistente() throws NoExisteRutaExcepcion {
        try {
            grafo.evaluarRuta(RUTA_EVALUAR_NO_VALIDA);
        } catch (CiudadEnGrafoNoEncontradaExcepcion | RutaSencillaMalDefinidaExcecion e) {
            Assert.fail();
        }
    }

    @Test(expected = CiudadEnGrafoNoEncontradaExcepcion.class)
    public void evaluarRutaCiudadNoExistente() throws CiudadEnGrafoNoEncontradaExcepcion {
        try {
            grafo.evaluarRuta(RUTA_EVALUAR_CIUDAD_NO_EXISTENTE);
        } catch (RutaSencillaMalDefinidaExcecion | NoExisteRutaExcepcion e) {
            Assert.fail();
        }
    }

    @Test
    public void calcularDistanciaRuta() {
        try {
            int distanciaRuta = grafo.calcularDistanciaDeRuta(RUTA_EVALUAR);
            Assert.assertEquals(DISTANCIA_RUTA_EVALUAR, distanciaRuta);
        } catch (CiudadEnGrafoNoEncontradaExcepcion | RutaSencillaMalDefinidaExcecion | NoExisteRutaExcepcion e) {
            Assert.fail();
        }
    }

    @Test
    public void prueba() {
        try {
            // 1. The distance of the route A-B-C.
            int distanciaRuta = grafo.calcularDistanciaDeRuta("A-B-C");
            // Output #1: 9
            Assert.assertEquals(9, distanciaRuta);

            // 2. The distance of the route A-D.
            distanciaRuta = grafo.calcularDistanciaDeRuta("A-D");
            // Output #2: 5
            Assert.assertEquals(5, distanciaRuta);

            // 3. The distance of the route A-D-C.
            distanciaRuta = grafo.calcularDistanciaDeRuta("A-D-C");
            // Output #3: 13
            Assert.assertEquals(13, distanciaRuta);

            // 4. The distance of the route A-E-B-C-D.
            distanciaRuta = grafo.calcularDistanciaDeRuta("A-E-B-C-D");
            // Output #4: 22
            Assert.assertEquals(22, distanciaRuta);
        } catch (CiudadEnGrafoNoEncontradaExcepcion | RutaSencillaMalDefinidaExcecion | NoExisteRutaExcepcion e) {
            Assert.fail();
        }

        // 5. The distance of the route A-E-D.
        try {
            grafo.calcularDistanciaDeRuta("A-E-D");
            Assert.fail();
        } catch (NoExisteRutaExcepcion e) {
            // Output #5: NO SUCH ROUTE
        } catch (CiudadEnGrafoNoEncontradaExcepcion | RutaSencillaMalDefinidaExcecion e) {
            Assert.fail();
        }

        // 6. The number of trips starting at C and ending at C with a maximum of 3 stops.  In the sample data below,
        // there are two such trips: C-D-C (2 stops). and C-E-B-C (3 stops).
        try {
            Set<Queue<RutaSencilla>> rutasEncontradas = calcularRutas(NOMBRE_CIUDAD_C, NOMBRE_CIUDAD_C, true);
            // rutas encontradas con menos de 3 paradas.
            List<Queue<RutaSencilla>> rutasConMaximoTresParadas = rutasEncontradas.stream()
                    .filter(ruta -> ruta.size() <= 3)
                    .collect(Collectors.toList());
            // Output #6: 2
            Assert.assertEquals(2, rutasConMaximoTresParadas.size());
        } catch (BuscarRutaExcepcion | CiudadEnGrafoNoEncontradaExcepcion e) {
            Assert.fail();
        }


//        // 7. The number of trips starting at A and ending at C with exactly 4 stops.  In the sample data below,
//        // there are three such trips: A to C (via B,C,D); A to C (via D,C,D); and A to C (via D,E,B).
//        try {
//            Set<Queue<RutaSencilla>> rutasEncontradas = new HashSet();
//            Ciudad ciudadInicio = grafo.obtenerCiudadDeGrafoPorNombre(NOMBRE_CIUDAD_A);
//            Ciudad ciudadFin = grafo.obtenerCiudadDeGrafoPorNombre(NOMBRE_CIUDAD_C);
//            grafo.buscarRutas(ciudadInicio, ciudadFin, new LinkedList<>(), rutasEncontradas, N_PARADAS_MAXIMAS, false);
//            // rutas encontradas con 4 paradas.
//            List<Queue<RutaSencilla>> rutasConMaximoTresParadas = rutasEncontradas.stream()
//                    .filter(ruta -> ruta.size() == 4)
//                    .collect(Collectors.toList());
//            // Output #6: 2
//            Assert.assertEquals(3, rutasConMaximoTresParadas.size());
//
//
//            for (Queue<RutaSencilla> rutas : rutasConMaximoTresParadas) {
//                LOGGER.info("Ruta ");
//                for (RutaSencilla ruta : rutas) {
//                    LOGGER.info(ruta.toString());
//                }
//            }
//        } catch (BuscarRutaExcepcion | CiudadEnGrafoNoEncontradaExcepcion e) {
//            Assert.fail();
//        }

        // 8. The length of the shortest route (in terms of distance to travel) from A to C.
        // Output #8: 9
        calcularMenorDistancia(NOMBRE_CIUDAD_A, NOMBRE_CIUDAD_C, 9);

        // 9. The length of the shortest route (in terms of distance to travel) from B to B.
        // Output #9: 9
        calcularMenorDistancia(NOMBRE_CIUDAD_B, NOMBRE_CIUDAD_B, 9);

        // 10. The number of different routes from C to C with a distance of less than 30.
        // In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.
        // Output #10: 7
        try {
            Set<Queue<RutaSencilla>> rutasEncontradas = calcularRutas(NOMBRE_CIUDAD_C, NOMBRE_CIUDAD_C, false);
            int rutasDistanciaMenor30 = 0;
            for (Queue<RutaSencilla> rutas : rutasEncontradas) {
                int distanciaRuta = grafo.calcularDistanciaColaRuta(rutas);
                if (distanciaRuta < 30) {
                    rutasDistanciaMenor30++;
                }
            }
            Assert.assertEquals(7, rutasDistanciaMenor30);
        } catch (BuscarRutaExcepcion | CiudadEnGrafoNoEncontradaExcepcion e) {
            Assert.fail();
        }

    }

    private Set<Queue<RutaSencilla>> calcularRutas(String nombreCiudadInicio, String nombreCiudadFin, boolean controlaParadasVisitadas)
            throws BuscarRutaExcepcion, CiudadEnGrafoNoEncontradaExcepcion {

        Set<Queue<RutaSencilla>> rutasEncontradas = new HashSet();
        Ciudad ciudadInicio = grafo.obtenerCiudadDeGrafoPorNombre(nombreCiudadInicio);
        Ciudad ciudadFin = grafo.obtenerCiudadDeGrafoPorNombre(nombreCiudadFin);
        grafo.buscarRutas(ciudadInicio, ciudadFin, new LinkedList<>(), rutasEncontradas, N_PARADAS_MAXIMAS, controlaParadasVisitadas);
        return rutasEncontradas;
    }

    private void calcularMenorDistancia(String nombreInicio, String nombreFin, int menorDistanciaEsperada) {
        try {
            Set<Queue<RutaSencilla>> rutasEncontradas = calcularRutas(nombreInicio, nombreFin, true);
            int menorDistancia = grafo.calcularDistanciaCorta(rutasEncontradas);
            Assert.assertEquals(menorDistanciaEsperada, menorDistancia);
        } catch (BuscarRutaExcepcion | CiudadEnGrafoNoEncontradaExcepcion e) {
            Assert.fail();
        }
    }
}
