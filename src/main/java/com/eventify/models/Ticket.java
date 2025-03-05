package com.eventify.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketType;
    private double price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}
