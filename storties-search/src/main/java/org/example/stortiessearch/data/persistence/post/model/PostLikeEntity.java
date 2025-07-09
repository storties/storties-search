package org.example.stortiessearch.data.persistence.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(
    name = "tbl_post_like",
    indexes = {
        @Index(name = "idx_like_log_post_id", columnList = "post_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_like_post_id_user_id", columnNames = {"post_id", "user_id"})
    })
public class PostLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "liked_at")
    private LocalDateTime likedAt;

    @PrePersist
    public void prePersist() {
        if (likedAt == null) {
            this.likedAt = LocalDateTime.now();
        }
    }
}
