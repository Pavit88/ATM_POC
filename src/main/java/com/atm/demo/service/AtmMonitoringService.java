package com.atm.demo.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.demo.model.Failure;
import com.atm.demo.model.Transaction;
import com.atm.demo.repository.FailureRepository;
import com.atm.demo.repository.TransactionRepository;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@Service
public class AtmMonitoringService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FailureRepository failureRepository;

    public String getAtmStatus() {
        return "ATM is operating normally";  // Placeholder for real-time status
    }

    public List<Transaction> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }

    public int getTotalCustomersLast24Hours() {
        long last24Hours = System.currentTimeMillis() - 86400000L; // 24 hours in milliseconds
        return (int) transactionRepository.countByTimestampAfter(new Date(last24Hours));
    }

    public List<Failure> getFailures() {
        return failureRepository.findAll();
    }

    public String downloadMedia(String startTime, String endTime) {
        return "Downloading media from " + startTime + " to " + endTime;  // Placeholder for real media download
    }
    
    
    
    
    //service layer to slice the video 
    
    private static final String FFMPEG_PATH = "C:\\Users\\Dell\\Documents\\Zoom\\ffmpeg-2024-11-25-git-04ce01df0b-full_build\\bin\\ffmpeg.exe";
    public String processVideo(String inputFilePath, String outputFilePath, String start, String end) throws Exception {
        // Validate time parameters (Ensure correct hh:mm:ss format)
        if (!isValidTimeFormat(start) || !isValidTimeFormat(end)) {
            throw new IllegalArgumentException("Invalid time format. Please use hh:mm:ss.");
        }

        // Validate input and output paths
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("Input file does not exist: " + inputFilePath);
        }

        // Calculate duration
        int durationInSeconds = calculateDuration(start, end);
        if (durationInSeconds <= 0) {
            throw new IllegalArgumentException("Invalid duration. Start time must be less than end time.");
        }

        // Use FFmpeg to extract the desired segment
        FFmpeg ffmpeg = new FFmpeg(FFMPEG_PATH);

        FFmpegBuilder builder = new FFmpegBuilder()
                .addExtraArgs("-ss", start)    // Start time in hh:mm:ss format (before input)
                .addExtraArgs("-to", end)      // End time in hh:mm:ss format
                .setInput(inputFilePath)       // Input video file (-i flag)
                .addOutput(outputFilePath)     // Output video file
                .addExtraArgs("-c", "copy")    // Copy video stream (no re-encoding)
                .done();

        // Execute the FFmpeg command
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        executor.createJob(builder).run();

        return "Video processed successfully. Saved at: " + outputFilePath;
    }

    // Validate if time is in the correct hh:mm:ss format
    private boolean isValidTimeFormat(String time) {
        String regex = "^([0-9]{2}):([0-9]{2}):([0-9]{2})$"; // HH:MM:SS format
        return time.matches(regex);
    }

    // Calculate duration in seconds
    private int calculateDuration(String start, String end) {
        String[] startParts = start.split(":");
        String[] endParts = end.split(":");

        int startSeconds = Integer.parseInt(startParts[0]) * 3600 + Integer.parseInt(startParts[1]) * 60 + Integer.parseInt(startParts[2]);
        int endSeconds = Integer.parseInt(endParts[0]) * 3600 + Integer.parseInt(endParts[1]) * 60 + Integer.parseInt(endParts[2]);

        return endSeconds - startSeconds;
    }


}
