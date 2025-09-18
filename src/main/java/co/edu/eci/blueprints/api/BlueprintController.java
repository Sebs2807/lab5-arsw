package co.edu.eci.blueprints.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import co.edu.eci.blueprints.model.Blueprint;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blueprints")
public class BlueprintController {

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
    @Operation(
        summary = "Obtener todos los blueprints",
        description = "Este endpoint devuelve un conjunto de todos los blueprints disponibles."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de blueprints",
        content = @Content(schema = @Schema(implementation = Blueprint.class))
    )
    public List<Map<String, String>> list() {
        return List.of(
            Map.of("id", "b1", "name", "Casa de campo"),
            Map.of("id", "b2", "name", "Edificio urbano")
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_blueprints.write')")
    @Operation(
        summary = "Crear un nuevo blueprint",
        description = "Este endpoint permite crear un nuevo blueprint en el sistema."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Blueprint creado exitosamente",
        content = @Content(schema = @Schema(implementation = Blueprint.class))
    )
    public Map<String, String> create(@RequestBody Map<String, String> in) {
        return Map.of("id", "new", "name", in.getOrDefault("name", "nuevo"));
    }
}
