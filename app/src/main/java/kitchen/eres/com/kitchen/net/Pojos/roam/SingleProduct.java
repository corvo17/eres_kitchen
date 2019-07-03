package kitchen.eres.com.kitchen.net.Pojos.roam;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SingleProduct{

	@SerializedName("excluded")
	private int excluded;

	@SerializedName("code")
	private int code;

	@SerializedName("isDish")
	private int isDish;

	@SerializedName("price")
	private int price;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("departmentId")
	private int departmentId;

	@SerializedName("prodIngredients")
	private List<ProdIngredient> prodIngredients;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("categoryId")
	private int categoryId;

	@SerializedName("specialDesires")
	private List<Object> specialDesires;

	public SingleProduct(int excluded, int code, int isDish, int price, String imageUrl, int departmentId, List<ProdIngredient> prodIngredients, String name, String description, int id, int categoryId, List<Object> specialDesires) {
		this.excluded = excluded;
		this.code = code;
		this.isDish = isDish;
		this.price = price;
		this.imageUrl = imageUrl;
		this.departmentId = departmentId;
		this.prodIngredients = prodIngredients;
		this.name = name;
		this.description = description;
		this.id = id;
		this.categoryId = categoryId;
		this.specialDesires = specialDesires;
	}

	public void setExcluded(int excluded){
		this.excluded = excluded;
	}

	public int getExcluded(){
		return excluded;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setIsDish(int isDish){
		this.isDish = isDish;
	}

	public int getIsDish(){
		return isDish;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setDepartmentId(int departmentId){
		this.departmentId = departmentId;
	}

	public int getDepartmentId(){
		return departmentId;
	}

	public void setProdIngredients(List<ProdIngredient> prodIngredients){
		this.prodIngredients = prodIngredients;
	}

	public List<ProdIngredient> getProdIngredients(){
		return prodIngredients;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setSpecialDesires(List<Object> specialDesires){
		this.specialDesires = specialDesires;
	}

	public List<Object> getSpecialDesires(){
		return specialDesires;
	}

	@Override
 	public String toString(){
		return 
			"SingleProduct{" + 
			"excluded = '" + excluded + '\'' + 
			",code = '" + code + '\'' + 
			",isDish = '" + isDish + '\'' + 
			",price = '" + price + '\'' + 
			",imageUrl = '" + imageUrl + '\'' + 
			",departmentId = '" + departmentId + '\'' + 
			",prodIngredients = '" + prodIngredients + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",categoryId = '" + categoryId + '\'' + 
			",specialDesires = '" + specialDesires + '\'' + 
			"}";
		}
}