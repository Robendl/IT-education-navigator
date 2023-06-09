package se.rijksoverheid.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Data Transfer Object used for sending responses containing course data.
 * Used for limited data consumer, which should not receive personal data.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LimitedCourseResponseDTO extends CourseResponseDTO implements Serializable {
}