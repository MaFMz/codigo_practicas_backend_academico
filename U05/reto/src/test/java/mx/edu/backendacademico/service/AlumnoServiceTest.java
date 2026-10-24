package mx.edu.backendacademico.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import mx.edu.backendacademico.domain.Alumno;
import mx.edu.backendacademico.domain.EstatusAlumno;
import mx.edu.backendacademico.repository.AlumnoRepositoryEnMemoria;
import org.junit.jupiter.api.Test;

class AlumnoServiceTest {
    @Test
    void listarConFiltroNoModificaElRepositorio() {
        var repository = new AlumnoRepositoryEnMemoria();
        var service = new AlumnoService(repository);
        var activo = repository.guardar(
            new Alumno(null, "A001", "Ada", "ada@u.mx", EstatusAlumno.ACTIVO));
        var baja = repository.guardar(
            new Alumno(null, "B001", "Grace", "grace@u.mx", EstatusAlumno.BAJA));
        var estadoOriginal = repository.buscarTodos();

        assertEquals(List.of(activo, baja), service.listar());
        assertEquals(List.of(activo), service.listar(EstatusAlumno.ACTIVO));
        assertEquals(List.of(baja), service.listar(EstatusAlumno.BAJA));
        assertEquals(estadoOriginal, repository.buscarTodos());
    }
}
