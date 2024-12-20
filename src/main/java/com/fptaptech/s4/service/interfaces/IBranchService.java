package com.fptaptech.s4.service.interfaces;


import com.fptaptech.s4.entity.Branch;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBranchService {

    Branch addBranch( Branch branch, List<MultipartFile> photos);
    Branch updateBranch(Long id, Branch branch, List<MultipartFile> photos);
    void deleteBranch(Long id);
    List<Branch> getAllBranches();
    Branch getBranchById(Long id);
    Branch getBranchWithRooms(Long id);
}