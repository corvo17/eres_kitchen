package kitchen.eres.com.kitchen.net.Pojos.send_new_added_removed_products;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products.GetAllProducts;
import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.SpecialDesire;

public class ProductToSend {

    private int categoryId;

    private int isDish;

    private String description;

    private int price;

    private String imageUrl;

    private int id;

    private int departmentId;

    private int code;

    private String name;
    private int excluded;
    private ArrayList<SpecialDesire> specialDesires;
    private ArrayList<ProdIngredient> prodIngredients;

    public ProductToSend(int categoryId, int isDish, String description, int price, String imageUrl, int id, int departmentId, int code, String name, int excluded, ArrayList<SpecialDesire> specialDesires, ArrayList<ProdIngredient> prodIngredients) {
        this.categoryId = categoryId;
        this.isDish = isDish;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.id = id;
        this.departmentId = departmentId;
        this.code = code;
        this.name = name;
        this.excluded = excluded;
        this.specialDesires = specialDesires;
        this.prodIngredients = prodIngredients;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsDish() {
        return isDish;
    }

    public void setIsDish(int isDish) {
        this.isDish = isDish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExcluded() {
        return excluded;
    }

    public void setExcluded(int excluded) {
        this.excluded = excluded;
    }

    public ArrayList<SpecialDesire> getSpecialDesires() {
        return specialDesires;
    }

    public void setSpecialDesires(ArrayList<SpecialDesire> specialDesires) {
        specialDesires = specialDesires;
    }

    public ArrayList<ProdIngredient> getProdIngredients() {
        return prodIngredients;
    }

    public void setProdIngredients(ArrayList<ProdIngredient> prodIngredients) {
        this.prodIngredients = prodIngredients;
    }
}