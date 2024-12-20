package com.fptaptech.s4.response;

import com.fptaptech.s4.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponse {
    private Long id;
    private String branchName;
    private String location;
    private List<String> photos;
    private String address;
    private String description;
    private List<RoomResponse> rooms;

    public BranchResponse(Branch branch) {
        this.id = branch.getId();
        this.branchName = branch.getBranchName();
        this.location = branch.getLocation();
        this.photos = branch.getPhotos();
        this.address = branch.getAddress();
        this.description = branch.getDescription();
        this.rooms = branch.getRooms().stream()
                .map(RoomResponse::new) // This uses the constructor that does not include the branch reference
                .collect(Collectors.toList());
    }
}

