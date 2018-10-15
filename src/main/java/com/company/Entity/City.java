package com.company.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "persons")
@AllArgsConstructor
@Builder

@Entity
@Table(name = "city")
public class City extends BaseEntity {
    @Column(name = "city_name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "city")
    private List<Person> persons;



}
