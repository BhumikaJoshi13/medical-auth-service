package com.medical.admin.controller;

import com.medical.admin.client.ReceptionistServiceClient;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/receptionist")
public class ReceptionistController {

    private final ReceptionistServiceClient receptionistServiceClient;

    public ReceptionistController(ReceptionistServiceClient receptionistServiceClient) {
        this.receptionistServiceClient = receptionistServiceClient;
    }

    // ✅ GET - Receptionist Profile
    @GetMapping("/profile/{receptionistId}")
    public ResponseEntity<Map<String, Object>> getReceptionistProfile(@PathVariable Long receptionistId) {
        try {
            Map<String, Object> profile = receptionistServiceClient.getReceptionistProfile(receptionistId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch receptionist profile: " + e.getMessage()));
        }
    }

    // ✅ POST - Assign Patient
    @PostMapping("/assign-patient")
    public ResponseEntity<Map<String, Object>> assignPatient(
            @RequestParam Long receptionistId,
            @RequestBody Map<String, Object> assignmentData) {
        try {
            Map<String, Object> result = receptionistServiceClient.assignPatient(receptionistId, assignmentData);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to assign patient: " + e.getMessage()));
        }
    }

    // ✅ GET - Assigned Patients
    @GetMapping("/assigned-patients/{receptionistId}")
    public ResponseEntity<Map<String, Object>> getAssignedPatients(@PathVariable Long receptionistId) {
        try {
            Map<String, Object> patients = receptionistServiceClient.getAssignedPatients(receptionistId);
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch assigned patients: " + e.getMessage()));
        }
    }

    // ✅ GET - All Receptionists
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllReceptionists() {
        try {
            Map<String, Object> receptionists = receptionistServiceClient.getAllReceptionists();
            return ResponseEntity.ok(receptionists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch receptionists: " + e.getMessage()));
        }
    }

    // ✅ PUT - Update Availability
    @PutMapping("/availability")
    public ResponseEntity<Map<String, Object>> updateAvailability(
            @RequestParam Long receptionistId,
            @RequestBody Map<String, Object> availabilityData) {
        try {
            Map<String, Object> result = receptionistServiceClient.updateAvailability(receptionistId, availabilityData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update availability: " + e.getMessage()));
        }
    }
}
