package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Hotel;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.HotelRepository;
import com.fptaptech.s4.service.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService implements IBranchService {

    private final BranchRepository branchRepository;
    private final HotelRepository hotelRepository;

    @Override
    public Branch addBranch(Long hotelId, Branch branch) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        branch.setHotel(hotel);  // Gán khách sạn cho chi nhánh
        return branchRepository.save(branch);
    }

    @Override
    public Branch updateBranch(Long id, Branch branch) {
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        existingBranch.setBranchName(branch.getBranchName());
        existingBranch.setLocation(branch.getLocation());
        existingBranch.setHotel(branch.getHotel());
        existingBranch.setCreatedAt(branch.getCreatedAt());
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
    public List<Branch> getBranchesByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        return branchRepository.findByHotel(hotel);
    }
}