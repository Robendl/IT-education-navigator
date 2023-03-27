package se.rijksoverheid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link se.rijksoverheid.model.Course} entity
 */
@Data
public class CourseResponseDTO implements Serializable {
    private long id;
    private Boolean archived;
    private String name;
    private String institution;
    private String location;
    private ProvinceDTO province;
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