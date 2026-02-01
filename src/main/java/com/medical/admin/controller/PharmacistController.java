package com.medical.admin.controller;

import com.medical.admin.client.PharmacistServiceClient;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/pharmacist")
public class PharmacistController {

    private final PharmacistServiceClient pharmacistServiceClient;

    public PharmacistController(PharmacistServiceClient pharmacistServiceClient) {
        this.pharmacistServiceClient = pharmacistServiceClient;
    }

    //  GET - Pharmacist Profile
    @GetMapping("/profile/{pharmacistId}")
    public ResponseEntity<Map<String, Object>> getPharmacistProfile(@PathVariable Long pharmacistId) {
        try {
            Map<String, Object> profile = pharmacistServiceClient.getPharmacistProfile(pharmacistId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch pharmacist profile: " + e.getMessage()));
        }
    }

    // POST - Fulfill Prescription
    @PostMapping("/fulfill-prescription")
    public ResponseEntity<Map<String, Object>> fulfillPrescription(
            @RequestParam Long pharmacistId,
            @RequestBody Map<String, Object> prescriptionData) {
        try {
            Map<String, Object> result = pharmacistServiceClient.fulfillPrescription(pharmacistId, prescriptionData);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fulfill prescription: " + e.getMessage()));
        }
    }

    //  POST - Add Medicine Stock
    @PostMapping("/medicine-stock")
    public ResponseEntity<Map<String, Object>> addMedicineStock(
            @RequestParam Long pharmacistId,
            @RequestBody Map<String, Object> stockData) {
        try {
            Map<String, Object> result = pharmacistServiceClient.addMedicineStock(pharmacistId, stockData);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add medicine stock: " + e.getMessage()));
        }
    }

    //  GET - Fulfilled Prescriptions
    @GetMapping("/fulfilled-prescriptions/{pharmacistId}")
    public ResponseEntity<Map<String, Object>> getFulfilledPrescriptions(@PathVariable Long pharmacistId) {
        try {
            Map<String, Object> prescriptions = pharmacistServiceClient.getFulfilledPrescriptions(pharmacistId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch fulfilled prescriptions: " + e.getMessage()));
        }
    }

    //  GET - Medicine Stock
    @GetMapping("/medicine-stock/{pharmacistId}")
    public ResponseEntity<Map<String, Object>> getMedicineStock(@PathVariable Long pharmacistId) {
        try {
            Map<String, Object> stock = pharmacistServiceClient.getMedicineStock(pharmacistId);
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch medicine stock: " + e.getMessage()));
        }
    }
}
