package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.dto.SpaDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Spa;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.SpaRepository;
import com.fptaptech.s4.service.interfaces.ISpaService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaService implements ISpaService {
    private final SpaRepository spaRepository;
    private final BranchRepository branchRepository; // Assuming you have a BranchRepository

    @Override
    public Response addNewSpaServiceName(Long branchId, String spaServiceName) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));
            Spa spa = new Spa();
            spa.setSpaServiceName(spaServiceName);
            spa.setBranch(branch); // Set the branch
            Spa savedSpa = spaRepository.save(spa);

            SpaDTO spaDTO = Utils.mapSpaEntityToSpaDTO(savedSpa);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setSpa(spaDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving spa service name: " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllSpaServiceNames() {
        return spaRepository.findSpaServiceNames();
    }

    @Override
    public Response deleteSpaServiceName(Long spaId) {
        Response response = new Response();
        try {
            Spa spa = spaRepository.findById(spaId).orElseThrow(() -> new OurException("Spa Not Found"));
            spaRepository.deleteById(spaId);
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting spa service name: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateSpaServiceName(Long spaId, String newSpaServiceName) {
        Response response = new Response();
        try {
            Spa spa = spaRepository.findById(spaId).orElseThrow(() -> new OurException("Spa Not Found"));
            spa.setSpaServiceName(newSpaServiceName);
            Spa updatedSpa = spaRepository.save(spa);

            SpaDTO spaDTO = Utils.mapSpaEntityToSpaDTO(updatedSpa);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setSpa(spaDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating spa service name: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSpaServiceNameById(Long spaId) {
        Response response = new Response();
        try {
            Spa spa = spaRepository.findById(spaId).orElseThrow(() -> new OurException("Spa Not Found"));
            SpaDTO spaDTO = Utils.mapSpaEntityToSpaDTO(spa);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setSpa(spaDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa service name: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSpaServiceByName(String spaServiceName) {
        Response response = new Response();
        try {
            Spa spa = spaRepository.findBySpaServiceName(spaServiceName)
                    .orElseThrow(() -> new OurException("Spa Service Name Not Found"));
            SpaDTO spaDTO = Utils.mapSpaEntityToSpaDTO(spa);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setSpa(spaDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa service name: " + e.getMessage());
        }
        return response;
    }

}
