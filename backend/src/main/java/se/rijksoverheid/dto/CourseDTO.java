package se.rijksoverheid.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import se.rijksoverheid.model.Province;

@Getter
@Setter
public class CourseDTO {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String institution;
    @NotNull
    @NotBlank
    private String location;
    @NotNull
    private long provinceId;
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
