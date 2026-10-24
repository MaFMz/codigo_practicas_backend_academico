package mx.edu.backendacademico.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import mx.edu.backendacademico.exception.MatriculaDuplicadaException;
import mx.edu.backendacademico.repository.AlumnoRepositoryEnMemoria;
import org.junit.jupiter.api.Test;

class AlumnoServiceConcurrencyTest {
    @Test
    void crearMismaMatriculaEnParaleloGuardaUnaYDevuelveUnConflicto()
            throws InterruptedException, ExecutionException {
        var repository = new AlumnoRepositoryEnMemoria();
        var service = new AlumnoService(repository);
        var listos = new CountDownLatch(2);
        var inicio = new CountDownLatch(1);
        Callable<Resultado> intento = () -> {
            listos.countDown();
            inicio.await();
            try {
                service.crear("A001", "Ada", "ada@u.mx");
                return Resultado.GUARDADO;
            } catch (MatriculaDuplicadaException ex) {
                return Resultado.CONFLICTO;
            }
        };
        var executor = Executors.newFixedThreadPool(2);

        try {
            var primero = executor.submit(intento);
            var segundo = executor.submit(intento);
            listos.await();
            inicio.countDown();

            var resultados = List.of(primero.get(), segundo.get());
            assertEquals(1L, resultados.stream().filter(r -> r == Resultado.GUARDADO).count());
            assertEquals(1L, resultados.stream().filter(r -> r == Resultado.CONFLICTO).count());
            var guardados = repository.buscarTodos();
            assertEquals(1, guardados.size());
            assertEquals("A001", guardados.getFirst().matricula());
        } finally {
            inicio.countDown();
            executor.shutdownNow();
        }
    }

    private enum Resultado {
        GUARDADO,
        CONFLICTO
    }
}
