package se.rijksoverheid.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class CourseResponseDTO implements Serializable {
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
    private String web;
    private String explanation;
}
