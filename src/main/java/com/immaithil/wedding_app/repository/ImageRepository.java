package com.immaithil.wedding_app.repository;


import com.immaithil.wedding_app.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    // Custom query to fetch images sorted by the Reddit-style algorithm (upvotes - downvotes)
    @Query("SELECT i FROM Image i ORDER BY (i.upvotes - i.downvotes) DESC")
    List<Image> findAllOrderByScoreDesc();
}
