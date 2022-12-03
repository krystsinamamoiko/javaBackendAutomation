package ru.gb.backendautomation.model.complexSearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private Integer id;
    private String title;
    private String image;
    private String imageType;
    private Nutrition nutrition;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
