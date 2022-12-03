package ru.gb.backendautomation.model.shoppingList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ShoppingListResponse {
    private List<Aisle> aisles = null;
    private Double cost;
    private Integer startDate;
    private Integer endDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
