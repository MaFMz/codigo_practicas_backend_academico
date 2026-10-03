package mx.edu.backendacademico.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SaludoController {

    @GetMapping("/saludo")
    public Map<String, String> saludar(
            @RequestParam(defaultValue = "Mundo") String nombre
    ) {
        // Construimos el saludo dinámico en Java
        String mensaje = "Hola " + nombre;
        
        // Jackson serializa este Map en JSON automáticamente
        return Map.of("mensaje", mensaje);
    }
}