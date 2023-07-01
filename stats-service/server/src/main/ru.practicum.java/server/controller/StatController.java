package server.controller;

import dto.HitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.OutStats;
import server.service.StatsService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*", allowedHeaders = "*")
public class StatController {
    private final StatsService service;

    @PostMapping("/hit")
    public ResponseEntity<?> addHit(@RequestBody HitDto hitDto) {
        service.saveHit(hitDto);
        return ResponseEntity.created(URI.create("")).build();
    }

    @GetMapping("/stats")
    public List<OutStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {

        return service.getStats(start, end, uris, unique);
    }
}
