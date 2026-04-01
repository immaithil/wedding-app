package com.immaithil.wedding_app.controller;

import com.immaithil.wedding_app.model.Image;
import com.immaithil.wedding_app.repository.ImageRepository;
import com.immaithil.wedding_app.service.CloudinaryService;
import com.immaithil.wedding_app.service.ImageService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*") // 🚨 CRUCIAL: Allows your React app (usually localhost:3000) to talk to Spring Boot (localhost:8080)
public class ImageController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    // 1. UPLOAD ENDPOINT
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploaderName") String uploaderName) {

        try {
            // Step 1: Upload the physical file to Google Drive
            String driveFileId = cloudinaryService.uploadImage(file);

            // Step 2: Save the metadata (Name & Drive ID) to the H2 Database
            Image newImage = new Image(uploaderName, driveFileId);
            imageRepository.save(newImage);

            return ResponseEntity.ok("Image uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    // 2. FETCH GALLERY ENDPOINT
    @GetMapping
    public ResponseEntity<List<Image>> getGallery() {
        // Fetches all images sorted by (upvotes - downvotes) descending
        List<Image> images = imageRepository.findAllOrderByScoreDesc();
        return ResponseEntity.ok(images);
    }

    // 3. VOTE ENDPOINT (Clean and simple)
    @PostMapping("/{id}/vote")
    public ResponseEntity<?> voteImage(
            @PathVariable Long id,
            @RequestParam("action") String action,
            @RequestParam(value = "previousAction", required = false) String previousAction) {

        try {
            // Let the Service handle all the business logic
            Image updatedImage = imageService.handleVote(id, action, previousAction);
            return ResponseEntity.ok(updatedImage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
