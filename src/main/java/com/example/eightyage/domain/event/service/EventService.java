package com.example.eightyage.domain.event.service;

import com.example.eightyage.domain.event.dto.request.EventRequestDto;
import com.example.eightyage.domain.event.dto.response.EventResponseDto;
import com.example.eightyage.domain.event.entity.Event;
import com.example.eightyage.domain.event.entity.EventState;
import com.example.eightyage.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponseDto saveEvent(EventRequestDto eventRequestDto) {
        Event event = new Event(
                eventRequestDto.getName(),
                eventRequestDto.getDescription(),
                eventRequestDto.getQuantity(),
                eventRequestDto.getStartDate(),
                eventRequestDto.getEndDate()
        );

        checkEventState(event);

        Event savedEvent = eventRepository.save(event);
        return savedEvent.toDto();
    }

    public Page<EventResponseDto> getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = eventRepository.findAll(pageable);

        // 모든 events들 checkState로 state 상태 갱신하기
        events.forEach(this::checkEventState);

        return events.map(Event::toDto);
    }

    public EventResponseDto getEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        checkEventState(event);

        return event.toDto();
    }

    public EventResponseDto updateEvent(long eventId, EventRequestDto eventRequestDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        event.update(eventRequestDto);

        checkEventState(event);

        return event.toDto();
    }

    private void checkEventState(Event event) {
        LocalDateTime now = LocalDateTime.now();
        EventState newState =
                ( (event.getStartDate().isBefore(now) || event.getStartDate().isEqual(now)) &&
                        (event.getEndDate().isAfter(now) || event.getEndDate().isEqual(now)) )
                        ? EventState.VALID
                        : EventState.INVALID;

        if (event.getState() != newState) {
            event.setState(newState);
            eventRepository.save(event);
        }
    }
}
