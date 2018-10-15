package com.company.Entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "cities")
@AllArgsConstructor
@Builder

@Entity
@Table(name = "country")
public class Country extends BaseEntity {
    @Column(name = "country_name", nullable = false, unique = true)
    private String name;

@OneToMany(mappedBy = "country")
    private List<City> cities;
}
