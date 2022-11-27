package ru.gb.backendautomation.model.complexSearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.gb.backendautomation.model.complexSearch.Nutrient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Nutrition {
    private List<Nutrient> nutrients = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
