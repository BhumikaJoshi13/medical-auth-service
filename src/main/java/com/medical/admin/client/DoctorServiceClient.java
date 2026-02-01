package com.medical.admin.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "DOCTOR",
    url = "${doctor.service.url:http://localhost:8088}",
    configuration = DoctorServiceClientConfig.class,
    path = "/doctors"
)
public interface DoctorServiceClient {

    // Get Doctor Profile
    @GetMapping("/profile/doctor/{doctorId}")
    Map<String, Object> getDoctorProfile(@PathVariable("doctorId") Long doctorId);

    // Add Prescription
    @PostMapping(value = "/prescriptions", consumes = "application/json")
    Map<String, Object> addPrescription(
        @RequestParam("doctorId") Long doctorId,
        @RequestBody Map<String, Object> prescriptionData
    );

    // Add Consultation Notes
    @PostMapping(value = "/consultation-notes", consumes = "application/json")
    Map<String, Object> addConsultationNotes(
        @RequestParam("doctorId") Long doctorId,
        @RequestBody Map<String, Object> notesData
    );

    //  Get Doctor Prescriptions
    @GetMapping("/prescriptions/doctor/{doctorId}")
    Map<String, Object> getDoctorPrescriptions(@PathVariable("doctorId") Long doctorId);

    // Get Doctor Consultation Notes
    @GetMapping("/consultation-notes/doctor/{doctorId}")
    Map<String, Object> getDoctorConsultationNotes(@PathVariable("doctorId") Long doctorId);
}