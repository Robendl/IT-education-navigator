package se.rijksoverheid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseRequestDTO {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String institution;
    private Boolean archived;
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
