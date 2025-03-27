package com.example.eightyage.domain.event.controller;

import com.example.eightyage.domain.event.dto.request.EventRequestDto;
import com.example.eightyage.domain.event.dto.response.EventResponseDto;
import com.example.eightyage.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto eventRequestDto) {
        return ResponseEntity.ok(eventService.saveEvent(eventRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getEvents(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getEvents(page, size));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable long eventId, @RequestBody EventRequestDto eventRequestDto) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, eventRequestDto));
    }
}
