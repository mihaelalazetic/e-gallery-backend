package com.egallery.service.impl;

import com.egallery.model.dto.CreateEventRequest;
import com.egallery.model.entity.ApplicationUser;
import com.egallery.model.entity.Artwork;
import com.egallery.model.entity.Event;
import com.egallery.model.entity.Venue;
import com.egallery.model.enums.EventType;
import com.egallery.repository.ArtworkRepository;
import com.egallery.repository.EventRepository;
import com.egallery.repository.VenueRepository;
import com.egallery.service.ApplicationUserService;
import com.egallery.service.EventService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ArtworkRepository artworkRepository;
    private final VenueRepository venueRepository;
    private final ApplicationUserService applicationUserService;

    public EventServiceImpl(EventRepository eventRepository,
                            ArtworkRepository artworkRepository,
                            VenueRepository venueRepository, ApplicationUserService applicationUserService) {
        this.eventRepository = eventRepository;
        this.artworkRepository = artworkRepository;
        this.venueRepository = venueRepository;
        this.applicationUserService = applicationUserService;
    }

    @Override
    public Event createEventWithExhibition(CreateEventRequest req) throws BadRequestException {
        // 1. Lookup or create Venue
        Optional<Venue> byAddress = venueRepository.findByAddressContainingIgnoreCase(req.getVenueAddress());
        Optional<Venue> byName = venueRepository.findByNameContainingIgnoreCase(req.getVenueName());
        Venue venue;
        if (byAddress.isPresent()) {
            venue = byAddress.get();
        } else if (byName.isPresent()) {
            venue = byName.get();
        } else {
            Venue newVenue = Venue.builder()
                    .name(req.getVenueName())
                    .address(req.getVenueAddress())
                    .build();
            venue = venueRepository.save(newVenue);
        }

        ApplicationUser currentUser = applicationUserService.getById(UUID.fromString(req.getCreatedByUUID()));

        // 3. Parse and validate EventType
        EventType type;
        try {
            type = (req.getEventType() != null)
                    ? EventType.valueOf(req.getEventType().toUpperCase())
                    : EventType.OTHER;
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid eventType: " + req.getEventType());
        }

        // 4. Prepare dates
        LocalDateTime start = req.getStartDate();
        LocalDateTime end = req.getEndDate();

        // 5. Collect tags (ignore empty/null)
        Set<String> tags = Optional.ofNullable(req.getTags()).orElse(Collections.emptyList())
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // 6. Validate and collect Artworks
        Set<Artwork> artworks = new HashSet<>();
        if (req.getArtworkIds() != null && !req.getArtworkIds().isEmpty()) {
            List<Artwork> found = artworkRepository.findAllById(req.getArtworkIds());
            if (found.size() != req.getArtworkIds().size()) {
                throw new BadRequestException("One or more artworkIds not found");
            }
            artworks.addAll(found);
        }

        // 7. Build Event
        Event event = Event.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .startDate(start)
                .endDate(end)
                .slug(req.getTitle().toLowerCase().replace(" ", "-")
                        + "-" + UUID.randomUUID().toString().substring(0, 5))
                .bannerImageUrl(req.getBannerImage())
                .isPublic(req.getIsPublic())
                .shareableLink(UUID.randomUUID().toString())
                .venue(venue)
                .tags(tags)
                .artworks(artworks)
                .type(type)
                .createdBy(currentUser)
                .build();

        // 8. Persist
        return eventRepository.save(event);
    }

    public List<Event> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findAllByStartDateAfterAndIsPublicTrue(now);
        return events.stream()
                .sorted(Comparator.comparing(Event::getStartDate))
                .collect(Collectors.toList());
    }

    @Override
    public Event getById(UUID id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        eventRepository.deleteById(id);
    }
}
