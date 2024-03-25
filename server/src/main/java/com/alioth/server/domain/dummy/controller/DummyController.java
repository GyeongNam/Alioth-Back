package com.alioth.server.domain.dummy.controller;

import com.alioth.server.domain.dummy.service.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    private final DummyService dummyService;
    @Autowired
    public DummyController(DummyService dummyService) {
        this.dummyService = dummyService;
    }
}
