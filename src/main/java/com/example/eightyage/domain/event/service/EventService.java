package com.example.eightyage.domain.event.service;

import com.example.eightyage.domain.event.dto.request.EventRequestDto;
import com.example.eightyage.domain.event.dto.response.EventResponseDto;
import com.example.eightyage.domain.event.entity.Event;
import com.example.eightyage.domain.event.entity.EventState;
import com.example.eightyage.domain.event.repository.EventRepository;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final StringRedisTemplate stringRedisTemplate;

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

        stringRedisTemplate.opsForValue().set("event:quantity:" + savedEvent.getId(), String.valueOf(savedEvent.getQuantity()));

        return savedEvent.toDto();
    }

    public Page<EventResponseDto> getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Event> events = eventRepository.findAll(pageable);

        // 모든 events들 checkState로 state 상태 갱신하기
        events.forEach(this::checkEventState);

        return events.map(Event::toDto);
    }

    public EventResponseDto getEvent(long eventId) {
        Event event = findByIdOrElseThrow(eventId);

        checkEventState(event);

        return event.toDto();
    }

    public EventResponseDto updateEvent(long eventId, EventRequestDto eventRequestDto) {
        Event event = findByIdOrElseThrow(eventId);

        event.update(eventRequestDto);

        checkEventState(event);

        return event.toDto();
    }

    private void checkEventState(Event event) {
        EventState prevState = event.getState();
        event.updateStateAt(LocalDateTime.now());

        if(event.getState() != prevState) {
            eventRepository.save(event);
        }
    }

    public Event getValidEventOrThrow(Long eventId) {
        Event event = findByIdOrElseThrow(eventId);

        event.updateStateAt(LocalDateTime.now());

        if(event.getState() != EventState.VALID) {
            throw new BadRequestException(ErrorMessage.INVALID_EVENT_PERIOD.getMessage());
        }

        return event;
    }

    public Event findByIdOrElseThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.EVENT_NOT_FOUND.getMessage()));
    }
}
