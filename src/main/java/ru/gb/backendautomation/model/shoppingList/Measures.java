package ru.gb.backendautomation.model.shoppingList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Measures {
    private Original original;
    private Metric metric;
    private Us us;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
