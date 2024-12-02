package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.SpaDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Spa;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.SpaRepository;
import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.service.interfaces.ISpaService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaService implements ISpaService {
    private final SpaRepository spaRepository;
    private final BranchRepository branchRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Response addNewSpaServiceName(Long branchId, MultipartFile photo, String spaServiceName, BigDecimal spaServicePrice, String spaDescription) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));
            Map uploadResult = cloudinaryService.upload(photo);
            String imageUrl = (String) uploadResult.get("url");
            Spa spa = new Spa();
            spa.setSpaServiceName(spaServiceName);
            spa.setSpaServicePrice(spaServicePrice);
            spa.setSpaPhotoUrl(imageUrl);
            spa.setSpaDescription(spaDescription);
            spa.setBranch(branch); // Set the branch
            Spa savedSpa = spaRepository.save(spa);

            SpaDTO spaDTO = Utils.mapSpaEntityToSpaDTO(savedSpa);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(spaDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving spa service name: " + e.getMessage());
        }
        return response;
    }


    @Override
    public List<SpaDTO> getAllSpaServices() {
        List<Spa> spaList = spaRepository.findAll();
        return spaList.stream()
                .map(Utils::mapSpaEntityToSpaDTO)
                .collect(Collectors.toList());
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
    public Response updateSpaServiceName(Long spaId, MultipartFile newSpaPhoto, String newSpaServiceName, BigDecimal newSpaServicePrice, String newSpaDescription) {
        Response response = new Response();
        try {
            Spa spa = spaRepository.findById(spaId).orElseThrow(() -> new OurException("Spa Not Found"));

            // Upload new photo to Cloudinary if provided
            if (newSpaPhoto != null && !newSpaPhoto.isEmpty()) {
                Map uploadResult = cloudinaryService.upload(newSpaPhoto);
                String imageUrl = (String) uploadResult.get("url");
                spa.setSpaPhotoUrl(imageUrl);
            }

            spa.setSpaServiceName(newSpaServiceName);
            spa.setSpaServicePrice(newSpaServicePrice);
            spa.setSpaDescription(newSpaDescription);
            Spa updatedSpa = spaRepository.save(spa);

            SpaDTO spaDTO = Utils.mapSpaEntityToSpaDTO(updatedSpa);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(spaDTO);
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
            response.setData(spaDTO);
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
            response.setData(spaDTO);
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
