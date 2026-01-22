package ru.practicum.eventhub.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    @JsonIgnore
    private Long version;

    @Column(length = 100, nullable = false)
    private String title;

    private String description;

    @Column(name = "event_date")
    private OffsetDateTime eventDate;

    private String location;

    @ManyToMany
    @JoinTable(
            name = "event_tags",
            joinColumns = @JoinColumn(name = "event_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false)
    )
    private List<Tag> tags = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getEvents().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getEvents().remove(this);
    }
}
