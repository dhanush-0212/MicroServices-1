package com.dhanush.inventoryservice.controller;

import brave.propagation.TraceIdContext;
import com.dhanush.inventoryservice.dto.InventoryResponse;
import com.dhanush.inventoryservice.service.InventoryService;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.TraceContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.tools.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;



import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ObservationRegistry observationRegistry;
    private final Logger log = LoggerFactory.getLogger(InventoryController.class);

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam List<String> code) {

        Observation observation = observationRegistry.getCurrentObservation();
        if (observation != null) {
            TraceContext traceContext = observation.getContextView().get(TraceContext.class);
            if (traceContext != null) {
                log.info("🔍 Trace ID: {}", traceContext.traceId());
            } else {
                log.warn("TraceContext is null in current observation");
            }
        } else {
            log.warn("No active observation found");
        }
        System.out.println("Received codes: " + code);
        return ResponseEntity.ok(inventoryService.IsInStock(code));
    }
}
