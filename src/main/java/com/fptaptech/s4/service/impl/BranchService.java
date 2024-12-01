package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.service.interfaces.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService implements IBranchService {
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;

    @Override
    public Branch addBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public Branch updateBranch(Long id, Branch branch) {
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        existingBranch.setBranchName(branch.getBranchName());
        existingBranch.setLocation(branch.getLocation());
        existingBranch.setPhoto(branch.getPhoto());
        existingBranch.setDescription(branch.getDescription());
        existingBranch.setCreatedAt(branch.getCreatedAt());
        existingBranch.setDescription(branch.getDescription());
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
