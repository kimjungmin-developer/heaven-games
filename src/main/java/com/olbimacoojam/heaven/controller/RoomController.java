package com.olbimacoojam.heaven.controller;

import com.olbimacoojam.heaven.domain.Room;
import com.olbimacoojam.heaven.domain.RoomFactory;
import com.olbimacoojam.heaven.domain.RoomRepository;
import com.olbimacoojam.heaven.dto.RoomResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RoomController {
    private final RoomRepository roomRepository;
    private final RoomFactory roomFactory;

    public RoomController(RoomRepository roomRepository, RoomFactory roomFactory) {
        this.roomRepository = roomRepository;
        this.roomFactory = roomFactory;
    }

    @MessageMapping("/rooms/{roomId}")
    @SendTo("/topic/rooms/{roomId}")
    @ResponseBody
    public RoomResponseDto enterRoom(@DestinationVariable int roomId, RoomResponseDto roomResponseDto) {
        System.out.println("chicken");
        Room room = roomRepository.findById(roomId);
        return new RoomResponseDto(room.getId());
    }


    @PostMapping("/rooms")
    @ResponseBody
    public ResponseEntity save() {
        Room room = roomFactory.makeNextRoom();
        roomRepository.save(room);
        return ResponseEntity.created(URI.create("rooms/" + room.getId())).build();
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<RoomResponseDto> list() {
        List<Room> rooms = roomRepository.getAll();
        return rooms.stream()
                .mapToInt(Room::getId)
                .mapToObj(RoomResponseDto::new)
                .collect(Collectors.toList());
    }
}
