package com.project.fasthrm.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="place", indexes = {
        @Index(name="idx_place_name", columnList = "place_name")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Place extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="place_id")
    private Long id;

    @Column(name="place_name", nullable = false)
    private String placeName;

    @Column(name ="place_type")
    private String placeType;

}
