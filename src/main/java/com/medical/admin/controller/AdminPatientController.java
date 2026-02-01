package com.medical.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.admin.client.PatientServiceClient;

@RestController
@RequestMapping("/admin/patients")
public class AdminPatientController {

    @Autowired
    private PatientServiceClient patientServiceClient;

    //  Add Allergy via Feign (Token Auto Forward)
    @PostMapping("/{patientId}/allergies")
    public Map<String, Object> addPatientAllergy(
            @PathVariable Long patientId,
            @RequestBody Map<String, Object> allergy,
            Authentication authentication) {
        
        return patientServiceClient.addAllergy(patientId, allergy);
    }

    //  Add Medical History via Feign
    @PostMapping("/{patientId}/history")
    public Map<String, Object> addPatientHistory(
            @PathVariable Long patientId,
            @RequestBody Map<String, Object> history,
            Authentication authentication) {
        
        return patientServiceClient.addMedicalHistory(patientId, history);
    }

    
    // GET ALL PATIENTS (MISSING ENDPOINT)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPatients() {
        // TODO: Patient service se list call
        return ResponseEntity.ok(new ArrayList<>());
    }
    // Get Complete Profile
    @GetMapping("/{patientId}/profile")
    public Map<String, Object> getPatientProfile(
            @PathVariable Long patientId,
            Authentication authentication) {
        
        return patientServiceClient.getPatientProfile(patientId);
    }
    
 // GET SINGLE PATIENT
    @GetMapping("/{patientId}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable Long patientId) {
        Map<String, Object> response = patientServiceClient.getPatientProfile(patientId);
        return ResponseEntity.ok(response);
    }
}






