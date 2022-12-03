package ru.gb.backendautomation.model.complexSearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ClassifyCuisineResponse {
    private String cuisine;
    private List<String> cuisines = null;
    private Double confidence;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
