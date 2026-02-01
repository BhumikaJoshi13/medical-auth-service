package com.medical.admin.controller;

import com.medical.admin.client.DoctorServiceClient;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/doctor")
public class DoctorController {

    private final DoctorServiceClient doctorServiceClient;

    public DoctorController(DoctorServiceClient doctorServiceClient) {
        this.doctorServiceClient = doctorServiceClient;
    }

    // GET - Doctor Profile
    @GetMapping("/profile/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorProfile(@PathVariable Long doctorId) {
        try {
            Map<String, Object> profile = doctorServiceClient.getDoctorProfile(doctorId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch doctor profile: " + e.getMessage()));
        }
    }

    //  POST - Add Prescription
    @PostMapping("/prescriptions")
    public ResponseEntity<Map<String, Object>> addPrescription(
            @RequestParam Long doctorId,
            @RequestBody Map<String, Object> prescriptionData) {
        try {
            Map<String, Object> result = doctorServiceClient.addPrescription(doctorId, prescriptionData);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add prescription: " + e.getMessage()));
        }
    }

    //  POST - Add Consultation Notes
    @PostMapping("/consultation-notes")
    public ResponseEntity<Map<String, Object>> addConsultationNotes(
            @RequestParam Long doctorId,
            @RequestBody Map<String, Object> notesData) {
        try {
            Map<String, Object> result = doctorServiceClient.addConsultationNotes(doctorId, notesData);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add consultation notes: " + e.getMessage()));
        }
    }

    //  GET - Doctor Prescriptions
    @GetMapping("/prescriptions/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorPrescriptions(@PathVariable Long doctorId) {
        try {
            Map<String, Object> prescriptions = doctorServiceClient.getDoctorPrescriptions(doctorId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch prescriptions: " + e.getMessage()));
        }
    }

    //  GET - Doctor Consultation Notes
    @GetMapping("/consultation-notes/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorConsultationNotes(@PathVariable Long doctorId) {
        try {
            Map<String, Object> notes = doctorServiceClient.getDoctorConsultationNotes(doctorId);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch consultation notes: " + e.getMessage()));
        }
    }
}