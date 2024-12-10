package com.fptaptech.s4.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"rooms"})
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_name", nullable = false,columnDefinition = "TEXT", unique = true)
    private String branchName;

    @Column(name = "location", nullable = false,columnDefinition = "TEXT")
    private String location;

    @ElementCollection
    @CollectionTable(name = "branch_photos", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(updatable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}



