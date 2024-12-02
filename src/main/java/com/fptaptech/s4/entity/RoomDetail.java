package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_detail")
public class RoomDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "room_size")
    private int roomSize;

    @Column(name = "price")
    private int price;

    @Column(columnDefinition = "LONGBLOB")
    private byte photo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

}