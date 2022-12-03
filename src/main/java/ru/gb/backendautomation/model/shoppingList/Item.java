package ru.gb.backendautomation.model.shoppingList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Item {
    private Integer id;
    private String name;
    private Measures measures;
    private List<Object> usages = null;
    private List<Object> usageRecipeIds = null;
    private Boolean pantryItem;
    private String aisle;
    private Double cost;
    private Integer ingredientId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
