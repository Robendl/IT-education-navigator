package se.rijksoverheid.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "provinces", schema = "rijksoverheid")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private Set<Course> courses;
}
