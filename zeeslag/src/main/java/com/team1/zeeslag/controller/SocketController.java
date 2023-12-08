package com.team1.zeeslag.controller;

import com.team1.zeeslag.DTO.AttackSquareDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @MessageMapping("/attack/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public String greeting(@DestinationVariable String gameId, AttackSquareDTO request) throws Exception {
        return "{\"gameId\": " + gameId + ", " + "\"attackedSquareId\": " +  request.attackedSquareId() + "}";
    }
}
