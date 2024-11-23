package com.fptaptech.s4.service;


import com.fptaptech.s4.entity.Branch;

import java.util.List;

public interface IBranchService {
    Branch addBranch(Long hotelId, Branch branch); // Thay đổi để thêm chi nhánh dựa trên hotelId
    Branch updateBranch(Long id, Branch branch);
    void deleteBranch(Long id);
    List<Branch> getAllBranches();
    Branch getBranchById(Long id);
    /*List<Branch> getBranchesByHotel(Long hotelId);*/
}