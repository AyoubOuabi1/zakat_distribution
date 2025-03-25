package org.zakat.distribution.controllers;

import org.zakat.distribution.dtos.HistoryItemDTO;
import org.zakat.distribution.services.HistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<HistoryItemDTO> getCombinedHistory() {
        return historyService.getCombinedHistory();
    }
}