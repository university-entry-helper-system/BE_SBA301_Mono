package com.example.SBA_M.entity.commands;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserFollowNewsId implements Serializable {

    private UUID accountId;
    private Long newsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollowNewsId that)) return false;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(newsId, that.newsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, newsId);
    }
}
