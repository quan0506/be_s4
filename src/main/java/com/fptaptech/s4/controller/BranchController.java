package com.fptaptech.s4.controller;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.service.impl.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')&& hasRole('USER')")
public class BranchController {

    private final BranchService branchService;

    // API thêm chi nhánh cho khách sạn dựa vào hotelId
    @PostMapping("/admin/add/{id}")
    public ResponseEntity<Branch> addBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch newBranch = branchService.addBranch(id, branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBranch);
    }

    // API cập nhật thông tin chi nhánh dựa vào branchId
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch updatedBranch = branchService.updateBranch(id, branch);
        return ResponseEntity.ok(updatedBranch);
    }

    // API xóa chi nhánh dựa vào branchId
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok("Branch deleted successfully.");
    }

    // API lấy danh sách tất cả các chi nhánh của một khách sạn
    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<Branch>> getAllBranchesByHotel(@PathVariable Long id) {
        List<Branch> branches = branchService.getBranchesByHotel(id);
        return ResponseEntity.ok(branches);
    }

    // API lấy thông tin chi nhánh dựa trên branchId
    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Branch branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    // API lấy tất cả chi nhánh
    @GetMapping("/all")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }
}