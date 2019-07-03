package kitchen.eres.com.kitchen.net.Pojos.roam;

public class ProIngredientRecieve {
    private long id;
    private long productId;
    private  Ingredient ingredient;
    private long ingredientId;
    private float count;
    private int unit;

    public ProIngredientRecieve(long id, long productId, long ingredientId, float count, int unit, Ingredient ingredient) {
        this.id = id;
        this.ingredient = ingredient;
        this.productId = productId;
        this.ingredientId = ingredientId;
        this.count = count;
        this.unit = unit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public float getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
