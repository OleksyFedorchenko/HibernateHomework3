package com.company.Entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@Builder

@Entity
@Table(name = "person")
public class Person extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String first_name;
    @Column(nullable = false, unique = true)
    private String last_name;
    @Column(nullable = false)
    private int age;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

}
