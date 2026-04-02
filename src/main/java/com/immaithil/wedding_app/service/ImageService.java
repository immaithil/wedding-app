package com.immaithil.wedding_app.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.immaithil.wedding_app.model.Image;
import com.immaithil.wedding_app.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private Cloudinary cloudinary;


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


    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {
            // 1. Grab the exact field from your Image model
            String fileIdentifier = image.getDriveFileId();

            // 2. Safely delete from Cloudinary
            if (fileIdentifier.startsWith("http")) {
                // If it's a full URL, extract the public_id
                String[] urlParts = fileIdentifier.split("/");
                String filename = urlParts[urlParts.length - 1];
                String publicId = filename.substring(0, filename.lastIndexOf("."));
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } else {
                // If you just stored the public_id directly, destroy it directly
                cloudinary.uploader().destroy(fileIdentifier, ObjectUtils.emptyMap());
            }

            // 3. Delete from Neon Database
            imageRepository.delete(image);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}