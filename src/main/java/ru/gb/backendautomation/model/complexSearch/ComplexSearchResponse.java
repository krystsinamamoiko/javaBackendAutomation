package ru.gb.backendautomation.model.complexSearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ComplexSearchResponse {
    private List<Result> results = null;
    private Integer offset;
    private Integer number;
    private Integer totalResults;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
