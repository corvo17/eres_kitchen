package kitchen.eres.com.kitchen.net.Pojos.get_categories_of_products;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import kitchen.eres.com.kitchen.net.Pojos.roam.ProdIngredient;
import kitchen.eres.com.kitchen.net.Pojos.roam.SpecialDesire;

public class GetAllProducts implements Parcelable{

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

	public GetAllProducts(int categoryId, int isDish, String description, int price, String imageUrl, int id, int departmentId, int code, String name, int excluded, ArrayList<SpecialDesire> specialDesires, ArrayList<ProdIngredient> prodIngredients) {
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

	protected GetAllProducts(Parcel in) {
		categoryId = in.readInt();
		isDish = in.readInt();
		description = in.readString();
		price = in.readInt();
		imageUrl = in.readString();
		id = in.readInt();
		departmentId = in.readInt();
		code = in.readInt();
		name = in.readString();
		excluded = in.readInt();
	}

	public static final Creator<GetAllProducts> CREATOR = new Creator<GetAllProducts>() {
		@Override
		public GetAllProducts createFromParcel(Parcel in) {
			return new GetAllProducts(in);
		}

		@Override
		public GetAllProducts[] newArray(int size) {
			return new GetAllProducts[size];
		}
	};

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
		this.specialDesires = specialDesires;
	}

	public ArrayList<ProdIngredient> getProdIngredients() {
		return prodIngredients;
	}

	public void setProdIngredients(ArrayList<ProdIngredient> prodIngredients) {
		this.prodIngredients = prodIngredients;
	}

	@Override
	public String toString() {
		return "GetAllProducts{" +
				"categoryId=" + categoryId +
				", isDish=" + isDish +
				", description='" + description + '\'' +
				", price=" + price +
				", imageUrl='" + imageUrl + '\'' +
				", id=" + id +
				", departmentId=" + departmentId +
				", code=" + code +
				", name='" + name + '\'' +
				", excluded=" + excluded +
				", specialDesires=" + specialDesires +
				", ProdIngredients=" + prodIngredients +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(categoryId);
		parcel.writeInt(isDish);
		parcel.writeString(description);
		parcel.writeInt(price);
		parcel.writeString(imageUrl);
		parcel.writeInt(id);
		parcel.writeInt(departmentId);
		parcel.writeInt(code);
		parcel.writeString(name);
		parcel.writeInt(excluded);
	}
}