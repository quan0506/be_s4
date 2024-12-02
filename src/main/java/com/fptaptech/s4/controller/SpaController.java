package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.SpaDTO;

import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.service.interfaces.ISpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spas")
public class SpaController {

    @Autowired
    private ISpaService spaService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> addNewSpaServiceName(@RequestParam Long branchId, @RequestParam String spaServiceName) {
        if (spaServiceName == null || spaServiceName.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a spa service name");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = spaService.addNewSpaServiceName(branchId, spaServiceName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

// lay tat ca loai spa
    @GetMapping("/all")
    public ResponseEntity<Response> getAllSpaServiceNames() {
        List<String> spaServiceNames = spaService.getAllSpaServiceNames();
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("successful");
        response.setSpaList(new ArrayList<>());

        // Convert List<String> to List<SpaDTO>
        List<SpaDTO> spaDTOList = spaServiceNames.stream().map(name -> {
            SpaDTO spaDTO = new SpaDTO();
            spaDTO.setSpaServiceName(name);
            return spaDTO;
        }).collect(Collectors.toList());

        response.setSpaList(spaDTOList);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

// lay ten dich vu spa theo id

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

// update dich vu spa
    @PutMapping("/update/{spaId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> updateSpaServiceName(@PathVariable Long spaId, @RequestParam String newSpaServiceName) {
        Response response = spaService.updateSpaServiceName(spaId, newSpaServiceName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

// xoa ten dich vu spa



    @DeleteMapping("/delete/{spaId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteSpaServiceName(@PathVariable Long spaId) {
        Response response = spaService.deleteSpaServiceName(spaId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
