package com.cptls.websocket.ay.debug.tool.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "streaming")
@RequiredArgsConstructor
@Slf4j
public class DebugSolutionController {




    @PostMapping("you_start_her")
    public void lastPriceSubscribe() {
        log.info("Let's go.. good luck");
    }

}
