package com.medical.admin.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import feign.Headers;

@FeignClient(
    name = "PATIENT",
    url = "${patient.service.url:http://localhost:8086}",
    configuration = PatientServiceClientConfig.class,
    path = "/patients"  // BASE PATH
)
public interface PatientServiceClient {

    // ✅ Get Patient Complete Profile
    @GetMapping("/profile/patient/{patientId}")
    Map<String, Object> getPatientProfile(@PathVariable("patientId") Long patientId);

    // ✅ Add Patient Allergy - FIXED!
    @PostMapping(value = "/allergies", consumes = "application/json")
    Map<String, Object> addAllergy(
        @RequestParam("patientId") Long patientId,
        @RequestBody Map<String, Object> allergyData
    );

    // ✅ Add Medical History - FIXED!
    @PostMapping("/history")
    Map<String, Object> addMedicalHistory(
        @RequestParam("patientId") Long patientId,
        @RequestBody Map<String, Object> historyData
    );

    // ✅ Get Patient Allergies List
    @GetMapping("/allergies/patient/{patientId}")
    Map<String, Object> getPatientAllergies(@PathVariable("patientId") Long patientId);

    // ✅ Get Patient History List  
    @GetMapping("/history/patient/{patientId}")
    Map<String, Object> getPatientHistory(@PathVariable("patientId") Long patientId);
}
