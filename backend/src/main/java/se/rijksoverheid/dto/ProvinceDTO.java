package se.rijksoverheid.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import se.rijksoverheid.model.Province;

import java.io.Serializable;

/**
 * A DTO for the {@link Province} entity
 */
@Data
public class ProvinceDTO implements Serializable {
    private long id;
    private String name;
}