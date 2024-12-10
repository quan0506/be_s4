package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.SpaDTO;

import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.service.interfaces.ISpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/spas")
@RequiredArgsConstructor
public class SpaController {

    @Autowired
    private final ISpaService spaService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> addNewSpaService(@RequestParam Long branchId,
                                                     @RequestParam String spaServiceName,
                                                     @RequestParam BigDecimal spaServicePrice,
                                                     @RequestParam MultipartFile spaPhoto,
                                                     @RequestParam String spaDescription) {
        if (spaServiceName == null || spaServiceName.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a spa service name");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = spaService.addNewSpaServiceName(branchId, spaPhoto, spaServiceName, spaServicePrice, spaDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllSpaServices() {
        List<SpaDTO> spaDTOList = spaService.getAllSpaServices();
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("successful");
        response.setData(spaDTOList);  // Set the list of spa services in the response
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> updateSpaServiceName(@PathVariable Long spaId,
                                                         @RequestParam MultipartFile newSpaPhoto,
                                                         @RequestParam String newSpaServiceName,
                                                         @RequestParam BigDecimal newSpaServicePrice,
                                                         @RequestParam String newSpaDescription) {
        Response response = spaService.updateSpaServiceName(spaId, newSpaPhoto, newSpaServiceName, newSpaServicePrice, newSpaDescription);
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


