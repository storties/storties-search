package org.example.stortiessearch.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import org.example.stortiessearch.persistence.util.StringListConverter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Table(name = "tbl_post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "VARCHAR(50)")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "tags", columnDefinition = "text[]")
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_published")
    private Boolean isPublished;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
