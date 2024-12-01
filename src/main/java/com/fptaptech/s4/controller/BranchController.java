package com.fptaptech.s4.controller;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.response.BranchResponse;
import com.fptaptech.s4.service.impl.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Branch> addBranch(@RequestBody Branch branch) {
        Branch newBranch = branchService.addBranch(branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBranch);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch updatedBranch = branchService.updateBranch(id, branch);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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


