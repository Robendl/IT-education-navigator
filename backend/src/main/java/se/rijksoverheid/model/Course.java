package se.rijksoverheid.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "courses", schema = "rijksoverheid")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Boolean archived = false;
    private String name;
    private String institution;
    private String location;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="province_id", nullable=false)
    private Province province;
    private String level;
    private String courseType;
    private Boolean housekeepingRelated;
    private String timeOccupation;
    private String region;
    private Boolean collaboration;
    private String responsibleTaskForce;
    private String professor;
    private String contact;
    private String web;
    private String explanation;
}
