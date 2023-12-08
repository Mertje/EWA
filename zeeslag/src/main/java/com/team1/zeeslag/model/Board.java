package com.team1.zeeslag.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "board")
    @JsonManagedReference
    private List<Ship> ships = new ArrayList<>(5);

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "board")
    @JsonManagedReference
    private List<Square> squares = new ArrayList<>(100);

    @NotNull
    private Long accountId;
}
