package com.example.SBA_M.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_follow_events")
public class UserFollowEvents {

    @EmbeddedId
    private UserFollowEventsId id = new UserFollowEventsId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id", nullable = false)
    private Events event;

    @CreationTimestamp
    @Column(name = "followed_at", nullable = false, updatable = false)
    private Instant followedAt;
}
