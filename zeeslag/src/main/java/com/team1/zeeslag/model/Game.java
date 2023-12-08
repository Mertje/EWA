package com.team1.zeeslag.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "game_players",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> playersList = new ArrayList<>();;

    @OneToMany(mappedBy = "game")
    @NotNull
    @JsonManagedReference
    private List<Board> playerBoards = new ArrayList<>();

    private Long turnPlayerID;

    private String winnerUsername;
}
