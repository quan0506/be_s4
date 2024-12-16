package com.fptaptech.s4.controller;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.response.BranchResponse;
import com.fptaptech.s4.service.impl.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Branch> addBranch(@RequestParam String branchName,
                                            @RequestParam String location,
                                            @RequestParam List<MultipartFile> photos,
                                            @RequestParam String address,
                                            @RequestParam String description) {
        Branch branch = Branch.builder()
                .branchName(branchName)
                .location(location)
                .address(address)
                .description(description)
                .build();
        Branch newBranch = branchService.addBranch(branch, photos);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBranch);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id,
                                               @RequestParam String branchName,
                                               @RequestParam String location,
                                               @RequestParam(required = false) List<MultipartFile> photos,
                                               @RequestParam String address,
                                               @RequestParam String description) {
        Branch branch = Branch.builder()
                .id(id)
                .branchName(branchName)
                .location(location)
                .address(address)
                .description(description)
                .build();
        Branch updatedBranch = branchService.updateBranch(id,branch, photos);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok("Branch deleted successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getBranchById(@PathVariable Long id) {
        Branch branch = branchService.getBranchWithRooms(id);
        BranchResponse branchResponse = new BranchResponse(branch);
        return ResponseEntity.ok(branchResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }
}
