package kitchen.eres.com.kitchen.net.Pojos.roam;

import android.content.Context;

import java.util.ArrayList;

public class SendAddedProduct implements Cloneable {
    private long id;
    private String name;
    private int code;
    private int isDish;
    private float price;
    private String description;
    private String imageUrl;
    private int excluded;
    private long departmentId;
    private long categoryId;
    private boolean confirmed;
    private ArrayList<SpecialDesire> specialDesires;
    private ArrayList<ProdIngredient> prodIngredients;


    public SendAddedProduct(long id, String name, int code, int isDish, float price, String description, String imageUrl, int excluded, long departmentId, long categoryId, boolean confirmed, ArrayList<SpecialDesire> specialDesires, ArrayList<ProdIngredient> prodIngredients) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isDish = isDish;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.excluded = excluded;
        this.departmentId = departmentId;
        this.categoryId = categoryId;
        this.confirmed = confirmed;
        this.specialDesires = specialDesires;
        this.prodIngredients = prodIngredients;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getIsDish() {
        return isDish;
    }

    public void setIsDish(int isDish) {
        this.isDish = isDish;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getExcluded() {
        return excluded;
    }

    public void setExcluded(int excluded) {
        this.excluded = excluded;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public ArrayList<SpecialDesire> getSpecialDesires() {
        return specialDesires;
    }

    public void setSpecialDesires(ArrayList<SpecialDesire> specialDesires) {
        this.specialDesires = specialDesires;
    }

    public ArrayList<ProdIngredient> getProdIngredients() {
        return prodIngredients;
    }

    public void setProdIngredients(ArrayList<ProdIngredient> prodIngredients) {
        this.prodIngredients = prodIngredients;
    }
}
