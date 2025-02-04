package org.zakat.distribution.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Zakat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amountReceived;
    private LocalDate dateReceived;

    @ManyToOne
    private User receiver;
}

