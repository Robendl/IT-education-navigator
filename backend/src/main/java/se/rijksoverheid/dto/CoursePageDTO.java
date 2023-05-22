package se.rijksoverheid.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoursePageDTO {
    private List<CourseResponseDTO> content;
    private long totalElements;
    private int totalPages;
}
