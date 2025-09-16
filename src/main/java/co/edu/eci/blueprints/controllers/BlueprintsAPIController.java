package co.edu.eci.blueprints.controllers;


// import co.edu.eci.arsw.blueprints.dto.ApiResponse;
// import edu.eci.arsw.blueprints.model.Blueprint;
// import edu.eci.arsw.blueprints.model.Point;
// import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
// import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
// import edu.eci.arsw.blueprints.services.BlueprintsServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.eci.blueprints.services.BlueprintsServices;
import co.edu.eci.blueprints.dto.ApiResponse;
import co.edu.eci.blueprints.model.*;
import co.edu.eci.blueprints.persistence.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/blueprints")
public class BlueprintsAPIController {

    private final BlueprintsServices services;

    public BlueprintsAPIController(BlueprintsServices services) { this.services = services; }

    // GET /api/v1/blueprints
        @GetMapping
        @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
        public ResponseEntity<ApiResponse<Set<Blueprint>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", services.getAllBlueprints()));
    }

    // GET /api/v1/blueprints/{author}
        @GetMapping("/{author}")
        @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
        public ResponseEntity<ApiResponse<?>> byAuthor(@PathVariable String author) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(200, "OK", services.getBlueprintsByAuthor(author)));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    // GET /api/v1/blueprints/{author}/{bpname}
        @GetMapping("/{author}/{bpname}")
        @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
        public ResponseEntity<ApiResponse<?>> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(200, "OK",services.getBlueprint(author, bpname)));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    // POST /api/v1/blueprints
        @PostMapping
        @PreAuthorize("hasAuthority('SCOPE_blueprints.write')")
        public ResponseEntity<ApiResponse<?>> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Created", bp));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, e.getMessage(), null));
        }
    }

    // PUT /api/v1/blueprints/{author}/{bpname}/points
        @PutMapping("/{author}/{bpname}/points")
        @PreAuthorize("hasAuthority('SCOPE_blueprints.write')")
        public ResponseEntity<ApiResponse<?>> addPoint(@PathVariable String author, @PathVariable String bpname,@RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>(202, "Accepted", null));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }
}
