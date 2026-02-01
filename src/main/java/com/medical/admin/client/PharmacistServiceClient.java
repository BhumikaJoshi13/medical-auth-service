package com.medical.admin.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "PHARMACIST",
    url = "${pharmacist.service.url:http://localhost:8089}",
    configuration = PharmacistServiceClientConfig.class,
    path = "/pharmacists"
)
public interface PharmacistServiceClient {

    // ✅ Get Pharmacist Profile
    @GetMapping("/profile/pharmacist/{pharmacistId}")
    Map<String, Object> getPharmacistProfile(@PathVariable("pharmacistId") Long pharmacistId);

    // ✅ Fulfill Prescription
    @PostMapping(value = "/fulfill-prescription", consumes = "application/json")
    Map<String, Object> fulfillPrescription(
        @RequestParam("pharmacistId") Long pharmacistId,
        @RequestBody Map<String, Object> prescriptionData
    );

    // ✅ Add Medicine Stock
    @PostMapping(value = "/medicine-stock", consumes = "application/json")
    Map<String, Object> addMedicineStock(
        @RequestParam("pharmacistId") Long pharmacistId,
        @RequestBody Map<String, Object> stockData
    );

    // ✅ Get Fulfilled Prescriptions
    @GetMapping("/fulfilled-prescriptions/pharmacist/{pharmacistId}")
    Map<String, Object> getFulfilledPrescriptions(@PathVariable("pharmacistId") Long pharmacistId);

    // ✅ Get Medicine Stock
    @GetMapping("/medicine-stock/pharmacist/{pharmacistId}")
    Map<String, Object> getMedicineStock(@PathVariable("pharmacistId") Long pharmacistId);
}