package se.rijksoverheid.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Transer Object used for sending Province related data
 */
@Data
public class ProvinceDTO implements Serializable {
    private long id;
    private String name;
}