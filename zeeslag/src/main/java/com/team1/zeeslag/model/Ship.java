package com.team1.zeeslag.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "ship")
    @JsonManagedReference
    private List<Square> squares;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "board_id")
    private Board board;

    @NotNull
    private int length;

    private Boolean placed;

    private Boolean destroyed;

    private int timesHit;
}
