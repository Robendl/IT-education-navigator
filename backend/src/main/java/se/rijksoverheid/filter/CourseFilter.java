package se.rijksoverheid.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Filter used for searching courses.
 */
@AllArgsConstructor
@Getter
@Setter
public class CourseFilter {
    private String search;
    private boolean archived;
    private List<String> levels;
    private List<String> regions;
    private List<Long> provinceIds;
    private List<String> courseTypes;
}
