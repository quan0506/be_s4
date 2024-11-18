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
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/add/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Branch> addBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch newBranch = branchService.addBranch(id, branch);
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

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<Branch>> getAllBranchesByHotel(@PathVariable Long id) {
        List<Branch> branches = branchService.getBranchesByHotel(id);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Branch branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }
}


/*@RestController
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/add/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Branch> addBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch newBranch = branchService.addBranch(id, branch);
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

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<Branch>> getAllBranchesByHotel(@PathVariable Long id) {
        List<Branch> branches = branchService.getBranchesByHotel(id);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Branch branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }
}*/




/*@RestController
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/add/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Branch> addBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch newBranch = branchService.addBranch(id, branch);
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

    @GetMapping("/hotel/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<Branch>> getAllBranchesByHotel(@PathVariable Long id) {
        List<Branch> branches = branchService.getBranchesByHotel(id);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'EMPLOYEE')")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Branch branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }
}*/





/*
@RestController
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BranchController {

    private final BranchService branchService;

    // API thêm chi nhánh cho khách sạn dựa vào hotelId
    @PostMapping("/add/{id}")
    public ResponseEntity<Branch> addBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch newBranch = branchService.addBranch(id, branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBranch);
    }

    // API cập nhật thông tin chi nhánh dựa vào branchId
    @PutMapping("/update/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch updatedBranch = branchService.updateBranch(id, branch);
        return ResponseEntity.ok(updatedBranch);
    }

    // API xóa chi nhánh dựa vào branchId
    @DeleteMapping("/delete/{id}")
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
*/
