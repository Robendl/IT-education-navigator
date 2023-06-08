package se.rijksoverheid.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Data Transfer Object used for sending responses containing course data.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LimitedCourseResponseDTO extends CourseResponseDTO implements Serializable {
}