package ru.gb.backendautomation.model.shoppingList;

public class ShoppingListRequestBuilder {
    private String item;
    private String aisle;
    private Boolean parse;

    public ShoppingListRequestBuilder setItem(String item) {
        this.item = item;
        return this;
    }

    public ShoppingListRequestBuilder setAisle(String aisle) {
        this.aisle = aisle;
        return this;
    }

    public ShoppingListRequestBuilder setParse(Boolean parse) {
        this.parse = parse;
        return this;
    }

    public ShoppingListRequest build() {
        return new ShoppingListRequest(item, aisle, parse);
    }
}
