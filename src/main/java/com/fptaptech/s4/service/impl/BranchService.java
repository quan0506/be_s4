package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.service.interfaces.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BranchService implements IBranchService {
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
private final CloudinaryService cloudinaryService;



@Override
        public Branch addBranch(Branch branch, MultipartFile photo) {
            if (photo != null && !photo.isEmpty()) {
                try {
                    Map uploadResult = cloudinaryService.upload(photo);
                    String imageUrl = (String) uploadResult.get("url");
                    branch.setPhoto(imageUrl);
                    branch.setCreatedAt(LocalDate.now());
                } catch (Exception e) {
                    // Handle the exception
                    throw new RuntimeException("Failed to upload photo", e);
                }
            }
            return branchRepository.save(branch);
        }






@Override
public Branch updateBranch(Long id, Branch branch, MultipartFile photo) {
    Branch existingBranch = branchRepository.findById(branch.getId())
            .orElseThrow(() -> new OurException("Branch Not Found"));

    existingBranch.setId(branch.getId());
    existingBranch.setBranchName(branch.getBranchName());
    existingBranch.setLocation(branch.getLocation());
    existingBranch.setAddress(branch.getAddress());
    existingBranch.setDescription(branch.getDescription());

    if (photo != null && !photo.isEmpty()) {
        try {
            Map uploadResult = cloudinaryService.upload(photo);
            String imageUrl = (String) uploadResult.get("url");
            existingBranch.setPhoto(imageUrl);
        } catch (Exception e) {
                    // Handle the exception
            throw new RuntimeException("Failed to upload photo", e);
        }
    }

    return branchRepository.save(existingBranch);
}





    @Override
    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
    }

    @Override
    public Branch getBranchWithRooms(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        List<Room> rooms = roomRepository.findByBranch_Id(id);
        branch.setRooms(rooms); // Cần thêm trường rooms trong thực thể Branch
        return branch;
    }
}

