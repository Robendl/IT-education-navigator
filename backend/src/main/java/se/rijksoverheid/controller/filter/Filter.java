package se.rijksoverheid.controller.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Filter {
    private String search;
    private boolean archived;
    private List<String> levels;
    private List<String> regions;
    private List<Long> provinceIds;
    private List<String> courseTypes;
}
