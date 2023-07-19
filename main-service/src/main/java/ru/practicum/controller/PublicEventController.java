package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.model.Sort;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*", allowedHeaders = "*")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsWithParamsByUser(@RequestParam(required = false) String text,
                                                         @RequestParam(required = false) List<Long> categories,
                                                         @RequestParam(required = false) Boolean paid,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                         LocalDateTime rangeStart,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                         LocalDateTime rangeEnd,
                                                         @RequestParam(required = false) boolean onlyAvailable,
                                                         @RequestParam(required = false) Sort sort,
                                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                                         HttpServletRequest request) {
        return eventService.publicGetEventsByFilters(text, categories, paid, onlyAvailable, rangeStart, rangeEnd, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getFullEventById(id, request);
    }
}