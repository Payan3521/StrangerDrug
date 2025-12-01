package com.desarrollox.backend.api_posts.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.desarrollox.backend.api_photos.model.Photo;
import com.desarrollox.backend.api_sections.model.Section;
import com.desarrollox.backend.api_videos.model.Video;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "main_video_id", referencedColumnName = "id", nullable = false)
    private Video video;

    @ManyToOne
    @JoinColumn(name = "preview_video_id", referencedColumnName = "id", nullable = false)
    private Video videoPreview;

    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id", nullable = false)
    private Section section;

    @ManyToOne
    @JoinColumn(name = "thumbnail_id", referencedColumnName = "id", nullable = false)
    private Photo thumbnail;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "duration_minutes", nullable = false)
    private int duration;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
