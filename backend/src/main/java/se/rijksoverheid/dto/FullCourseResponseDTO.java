package se.rijksoverheid.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Data Transfer Object used for sending responses containing course data.
 * Includes personal data on top of CourseResponseDTO.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FullCourseResponseDTO extends CourseResponseDTO implements Serializable {
    private String responsibleTaskForce;
    private String professor;
    private String contact;
}