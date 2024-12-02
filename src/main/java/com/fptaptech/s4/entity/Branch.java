package com.fptaptech.s4.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
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

    @Column(name = "branch_name", nullable = false, length = 100, unique = true)
    private String branchName;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "photo")
    private String photo;

    @Column(name = "address",nullable = false)
    private String address;

    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms; // Thêm trường này

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}



