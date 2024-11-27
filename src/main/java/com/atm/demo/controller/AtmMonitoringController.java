package com.atm.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atm.demo.model.Failure;
import com.atm.demo.model.Transaction;
import com.atm.demo.service.AtmMonitoringService;

@RestController
@RequestMapping("/atm")
public class AtmMonitoringController {

    @Autowired
    private AtmMonitoringService atmMonitoringService;

    // Endpoint for monitoring ATM status and behaviors
    @GetMapping("/monitoring")
    public String getAtmMonitoring() {
        return atmMonitoringService.getAtmStatus();
    }

    // Endpoint for fetching transactions based on type (Deposit, Withdrawal, etc.)
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@RequestParam String type) {
        return atmMonitoringService.getTransactionsByType(type);
    }

    // Endpoint to get total customers in the last 24 hours
    @GetMapping("/customers")
    public int getTotalCustomers() {
        return atmMonitoringService.getTotalCustomersLast24Hours();
    }

    // Endpoint to get a list of failures (downtime, incidents)
    @GetMapping("/failures")
    public List<Failure> getFailures() {
        return atmMonitoringService.getFailures();
    }

//    // Endpoint to download images or videos from cameras by time range
//    @GetMapping("/media")
//    public String downloadMedia(@RequestParam String startTime, @RequestParam String endTime) {
//        return atmMonitoringService.downloadMedia(startTime, endTime);
//    }
    
    //Endpoint to slice the video from the start time to end time
    @GetMapping("/camera-logs")
    public ResponseEntity<String> processVideo(
            @RequestParam String inputFilePath,
            @RequestParam String outputFilePath,
            @RequestParam String start,
            @RequestParam String end
    ) {
        try {
            String result = atmMonitoringService.processVideo(inputFilePath, outputFilePath, start, end);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing video: " + e.getMessage());
        }
    }
}