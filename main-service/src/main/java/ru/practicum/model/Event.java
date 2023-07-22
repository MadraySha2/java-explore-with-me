package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)

    private String annotation;
    private String title;
    @Column(length = 7000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Long participantLimit;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;
    private Float lat;
    private Float lon;
    private Boolean paid;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private Long views;

}
