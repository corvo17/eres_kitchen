package kitchen.eres.com.kitchen.net.Pojos;

public class Category {
    private int id;
    private String name;
    private int code;
    private String imageUrl;
    private int primaryCategoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrimaryCategoryId() {
        return primaryCategoryId;
    }

    public void setPrimaryCategoryId(int primaryCategoryId) {
        this.primaryCategoryId = primaryCategoryId;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", imageUrl='" + imageUrl + '\'' +
                ", primaryCategoryId=" + primaryCategoryId +
                '}';
    }
    // "products": []
}
