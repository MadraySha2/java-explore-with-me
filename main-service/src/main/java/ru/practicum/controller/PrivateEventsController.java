package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*", allowedHeaders = "*")
@Validated
public class PrivateEventsController {
    private final EventService eventService;

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto addEvent(@PathVariable Long userId, @Valid @RequestBody EventEntryDto entryDto) {
        return eventService.addEvent(userId, entryDto);
    }

    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId, @RequestParam(defaultValue = "0", required = false) Integer from, @RequestParam(defaultValue = "10", required = false) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.getEventsByInitiator(userId, pageable);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEventByUser(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody EventUserUpdateDto updateEventUserDto) {
        return eventService.updateEventByOwner(userId, eventId, updateEventUserDto);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventByInitiatorAndId(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByOwnerOfEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getRequestOwnEvents(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResult updateRequests(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody RequestStatusUpdate requestStatusUpdateDto) {
        return requestService.confirmRequest(userId, eventId, requestStatusUpdateDto);
    }
}
