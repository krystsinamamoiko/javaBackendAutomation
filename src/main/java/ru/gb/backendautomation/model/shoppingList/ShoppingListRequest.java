package ru.gb.backendautomation.model.shoppingList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ShoppingListRequest {
    private String item;
    private String aisle;
    private Boolean parse;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ShoppingListRequest(String item, String aisle, Boolean parse) {
        this.item = item;
        this.aisle = aisle;
        this.parse = parse;
    }
}
