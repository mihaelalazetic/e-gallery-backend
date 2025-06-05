
package com.egallery.service.impl;

import com.egallery.model.dto.CreateEventRequest;
import com.egallery.model.entity.Artwork;
import com.egallery.model.entity.Event;
import com.egallery.model.entity.Exhibition;
import com.egallery.repository.ArtworkRepository;
import com.egallery.repository.EventRepository;
import com.egallery.repository.ExhibitionRepository;
import com.egallery.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final ArtworkRepository artworkRepository;

    public EventServiceImpl(EventRepository eventRepository, ExhibitionRepository exhibitionRepository, ArtworkRepository artworkRepository) {
        this.eventRepository = eventRepository;
        this.exhibitionRepository = exhibitionRepository;
        this.artworkRepository = artworkRepository;
    }

    @Override
    public Event create(Event entity) {
        return eventRepository.save(entity);
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


    @Override
    public Event createEventWithExhibition(CreateEventRequest req) {
        String bannerImageUrl = req.getBannerImage();

        Exhibition exhibition = null;
        if (req.getArtworkIds() != null && !req.getArtworkIds().isEmpty()) {
            List<Artwork> artworks = artworkRepository.findAllById(req.getArtworkIds());

            exhibition = Exhibition.builder()
                    .title("Exhibition for " + req.getTitle())
                    .slug("exhibition-" + UUID.randomUUID().toString().substring(0, 5))
                    .description(req.getDescription())
                    .artworks(new HashSet<>(artworks))
                    .bannerImageUrl(bannerImageUrl)
                    .shareableLink(UUID.randomUUID().toString())
                    .isOnline(true)
                    .build();

            exhibition = exhibitionRepository.save(exhibition);
        }

        Event event = Event.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .slug(req.getTitle().toLowerCase().replace(" ", "-") + "-" + UUID.randomUUID().toString().substring(0, 5))
                .bannerImageUrl(bannerImageUrl)
                .meetingLink(req.getMeetingLink())
                .isPublic(Boolean.TRUE.equals(req.getIsPublic()))
                .shareableLink(UUID.randomUUID().toString())
                .exhibition(exhibition)
                .build();

        return eventRepository.save(event);
    }

}
