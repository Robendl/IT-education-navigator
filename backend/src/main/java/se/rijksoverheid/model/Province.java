package se.rijksoverheid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Entity class for Province data
 */
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
    @OneToMany(mappedBy = "province", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Course> courses;
}
