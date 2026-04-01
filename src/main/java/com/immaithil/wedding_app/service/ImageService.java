package com.immaithil.wedding_app.service;


import com.immaithil.wedding_app.model.Image;
import com.immaithil.wedding_app.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image handleVote(Long id, String action, String previousAction) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // 1. Handle UPVOTE
        if ("up".equalsIgnoreCase(action)) {
            image.setUpvotes(image.getUpvotes() + 1);
            // If they switched from downvote, remove the old downvote
            if ("down".equalsIgnoreCase(previousAction) && image.getDownvotes() > 0) {
                image.setDownvotes(image.getDownvotes() - 1);
            }
        }
        // 2. Handle DOWNVOTE
        else if ("down".equalsIgnoreCase(action)) {
            image.setDownvotes(image.getDownvotes() + 1);
            // If they switched from upvote, remove the old upvote
            if ("up".equalsIgnoreCase(previousAction) && image.getUpvotes() > 0) {
                image.setUpvotes(image.getUpvotes() - 1);
            }
        }

        return imageRepository.save(image);
    }
}