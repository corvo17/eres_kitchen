package kitchen.eres.com.kitchen.model;

public class Product
{
    public String toString()
    {
        return getName();
    }
    private Product()
    {
        //SpecialDesires = new List<SpecialDesire>();
        confirmed = false;
        isDeleted = 0;
    }
    private long id ;
    private int ccde ;
    private int isDish ;
    private String name ;
    private float price ;
    private String description ;
    private String imageUrl ;
    private int excluded ;
    private long departmentId ;
    private long categoryId ;
//    private ArrayList<SpecialDesire> SpecialDesires;
//    private  ArrayList<ProdIngredient> ProdIngredients ;
    private boolean confirmed ;
    private int isDeleted ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCcde() {
        return ccde;
    }

    public void setCcde(int ccde) {
        this.ccde = ccde;
    }

    public int getIsDish() {
        return isDish;
    }

    public void setIsDish(int isDish) {
        this.isDish = isDish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
