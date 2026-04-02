package com.immaithil.wedding_app.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gallery_images")
@Getter
@Setter
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

}
