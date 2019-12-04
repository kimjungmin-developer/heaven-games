package com.olbimacoojam.heaven.controller;

import com.olbimacoojam.heaven.dto.RoomResponseDto;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class CreateGameStompFrameHandler implements StompFrameHandler {
    private CompletableFuture<RoomResponseDto> completableFuture;

    public CreateGameStompFrameHandler(CompletableFuture<RoomResponseDto> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return RoomResponseDto.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
        completableFuture.complete((RoomResponseDto) o);
    }
}