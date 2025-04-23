
package com.egallery.service;

import com.egallery.model.dto.ArtworkUploadRequest;
import com.egallery.model.entity.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface ArtworkService {
    Artwork create(Artwork entity);

    Artwork getById(UUID id);

    List<Artwork> getAll();

    void delete(UUID id);

    void uploadArtwork(ArtworkUploadRequest request);

    List<Artwork> findPaginated(int page, int size);

    List<Artwork> findLimited(int limit);

    Page<Artwork> getFeaturedArt(int page, int size);
}
