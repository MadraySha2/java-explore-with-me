package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.OutStatsDto;
import ru.practicum.server.service.StatsService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*", allowedHeaders = "*")
public class StatController {
    @Autowired
    private final StatsService service;

    @PostMapping("/hit")
    public ResponseEntity<?> addHit(@RequestBody HitDto hitDto) {
        System.out.println(hitDto);
        service.saveHit(hitDto);
        return ResponseEntity.created(URI.create("")).body(hitDto);
    }

    @GetMapping("/stats")
    public List<OutStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") boolean unique) {

        return service.getStats(start, end, uris, unique);
    }
}
