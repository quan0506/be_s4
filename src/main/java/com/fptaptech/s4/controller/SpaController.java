package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.service.interfaces.ISpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/spas")
@RequiredArgsConstructor
public class SpaController {

    @Autowired
    private final ISpaService spaService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> addNewSpa(
            @RequestParam("branchId") Long branchId,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestParam(value = "spaServiceName", required = false) String spaServiceName,
            @RequestParam(value = "spaServicePrice", required = false) BigDecimal spaServicePrice,
            @RequestParam(value = "spaDescription", required = false) String spaDescription
    ) {
        if (photos == null || photos.isEmpty() || spaServiceName == null || spaServiceName.isBlank() || spaServicePrice == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields (branchId, photos, spaServiceName, spaServicePrice)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = spaService.addNewSpa(branchId, photos, spaServiceName, spaServicePrice, spaDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllSpaServices(@RequestParam("branchId") Long branchId) {
        Response response = spaService.getAllSpaServices(branchId);
        response.setStatusCode(200);
        response.setMessage("successful");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/spa-by-id/{spaId}")
    public ResponseEntity<Response> getSpaServiceNameById(@PathVariable Long spaId) {
        Response response = spaService.getSpaServiceNameById(spaId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/spa-by-name")
    public ResponseEntity<Response> getSpaServiceByName(@RequestParam String spaServiceName) {
        Response response = spaService.getSpaServiceByName(spaServiceName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{spaId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateSpa(
            @PathVariable Long spaId,
            @RequestParam("branchId") Long branchId,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestParam(value = "spaServiceName", required = false) String spaServiceName,
            @RequestParam(value = "spaServicePrice", required = false) BigDecimal spaServicePrice,
            @RequestParam(value = "spaDescription", required = false) String spaDescription
    ) {
        Response response = spaService.updateSpa(branchId, spaId, spaServiceName, spaServicePrice, spaDescription, photos);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{spaId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteSpaServiceName(@PathVariable Long spaId) {
        Response response = spaService.deleteSpaServiceName(spaId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-all-spas")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllSpas() {
        Response response = spaService.getAllSpas();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}


