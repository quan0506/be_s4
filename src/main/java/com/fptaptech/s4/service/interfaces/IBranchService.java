package com.fptaptech.s4.service.interfaces;


import com.fptaptech.s4.entity.Branch;

import java.util.List;

public interface IBranchService {

    Branch addBranch( Branch branch);
    Branch updateBranch(Long id, Branch branch);
    void deleteBranch(Long id);
    List<Branch> getAllBranches();
    Branch getBranchById(Long id);
    Branch getBranchWithRooms(Long id);
}