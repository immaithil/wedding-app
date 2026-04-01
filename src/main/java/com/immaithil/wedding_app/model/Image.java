package com.immaithil.wedding_app.model;


import jakarta.persistence.*;

@Entity
@Table(name = "gallery_images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploaderName;

    @Column(nullable = false, unique = true)
    private String driveFileId;

    private int upvotes = 0;

    private int downvotes = 0;

    // Default constructor required by JPA
    public Image() {}

    public Image(String uploaderName, String driveFileId) {
        this.uploaderName = uploaderName;
        this.driveFileId = driveFileId;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUploaderName() { return uploaderName; }
    public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }

    public String getDriveFileId() { return driveFileId; }
    public void setDriveFileId(String driveFileId) { this.driveFileId = driveFileId; }

    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }

    public int getDownvotes() { return downvotes; }
    public void setDownvotes(int downvotes) { this.downvotes = downvotes; }
}
