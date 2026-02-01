
package com.medical.admin.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "RECEPTIONIST",
    url = "${receptionist.service.url:http://localhost:8087}",
    configuration = ReceptionistServiceClientConfig.class,
    path = "/receptionists"
)
public interface ReceptionistServiceClient {

    //  Get Receptionist Profile
    @GetMapping("/profile/receptionist/{receptionistId}")
    Map<String, Object> getReceptionistProfile(@PathVariable("receptionistId") Long receptionistId);

    // Assign Patient to Receptionist
    @PostMapping(value = "/assign-patient", consumes = "application/json")
    Map<String, Object> assignPatient(
        @RequestParam("receptionistId") Long receptionistId,
        @RequestBody Map<String, Object> assignmentData
    );

    //  Get Assigned Patients List
    @GetMapping("/assigned-patients/receptionist/{receptionistId}")
    Map<String, Object> getAssignedPatients(@PathVariable("receptionistId") Long receptionistId);

    // ✅ Get All Receptionists
    @GetMapping("/all")
    Map<String, Object> getAllReceptionists();

    // ✅ Update Receptionist Availability
    @PutMapping(value = "/availability", consumes = "application/json")
    Map<String, Object> updateAvailability(
        @RequestParam("receptionistId") Long receptionistId,
        @RequestBody Map<String, Object> availabilityData
    );
}