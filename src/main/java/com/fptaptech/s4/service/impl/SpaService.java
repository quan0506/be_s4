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
import java.util.ArrayList;
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
    public Response addNewSpa(Long branchId, List<MultipartFile> photos, String spaServiceName, BigDecimal spaServicePrice, String spaDescription) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));

            List<String> imageUrls = uploadPhotos(photos); // Upload photos to Cloudinary

            Spa spa = new Spa();
            spa.setBranch(branch); // Set the branch
            spa.setPhotos(imageUrls); // Set the list of photos
            spa.setSpaServiceName(spaServiceName);
            spa.setSpaServicePrice(spaServicePrice);
            spa.setSpaDescription(spaDescription);
            Spa savedSpa = spaRepository.save(spa);

            SpaDTO spaDTO = Utils.mapSpaEntityToSpaDTO(savedSpa);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(spaDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving spa: " + e.getMessage());
        }
        return response;
    }



    @Override
    public Response getAllSpaServices(Long branchId) {
        Response response = new Response();
        try {
            List<Spa> spaList = spaRepository.findByBranchId(branchId);
            List<SpaDTO> spaDTOList = Utils.mapSpaListEntityToSpaListDTO(spaList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setSpaList(spaDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa services: " + e.getMessage());
        }
        return response;
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
    public Response updateSpa(Long branchId, Long spaId, String spaServiceName, BigDecimal spaServicePrice, String spaDescription, List<MultipartFile> photos) {
        Response response = new Response();
        try {
            List<String> imageUrls = null;
            if (photos != null && !photos.isEmpty()) {
                imageUrls = uploadPhotos(photos); // Upload photos to Cloudinary
            }

            Spa spa = spaRepository.findByIdAndBranchId(spaId, branchId).orElseThrow(() -> new OurException("Spa Not Found"));
            if (spaServiceName != null) spa.setSpaServiceName(spaServiceName);
            if (spaServicePrice != null) spa.setSpaServicePrice(spaServicePrice);
            if (spaDescription != null) spa.setSpaDescription(spaDescription);
            if (imageUrls != null && !imageUrls.isEmpty()) spa.setPhotos(imageUrls);

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
            response.setMessage("Error updating spa: " + e.getMessage());
        }
        return response;
    }



    private List<String> uploadPhotos(List<MultipartFile> photos) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile photo : photos) {
            if (photo != null && !photo.isEmpty()) {
                try {
                    Map uploadResult = cloudinaryService.upload(photo);
                    String imageUrl = (String) uploadResult.get("url");
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to upload photo", e);
                }
            }
        }
        return imageUrls;
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

    public Response getAllSpas() {
        Response response = new Response();
        try {
            List<Spa> spas = spaRepository.findAll();
            List<SpaDTO> spaDTOList = Utils.mapSpaListEntityToSpaListDTO(spas);
            response.setStatusCode(200);
            response.setMessage("Spas retrieved successfully.");
            response.setData(spaDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spas: " + e.getMessage());
        }
        return response;
    }
}
