package kitchen.eres.com.kitchen.net.Pojos.ordersPojo;

import android.os.Parcel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderItem extends MyOrderItem{

	private int factCount;

	private List<Object> orderItemrefinements;

	private int stateId;

	private String descriptor;

	private String descriptor2;

	private int productId;

	private int id;

	private int count;

	private int departmentId;

	private int orderId;
	private int orderPartId;

	protected OrderItem(Parcel in) {
		super(in);
	}

	public String getDescriptor2() {
		return descriptor2;
	}

	public void setDescriptor2(String descriptor2) {
		this.descriptor2 = descriptor2;
	}

	public int getOrderPartId() {
		return orderPartId;
	}

	public void setOrderPartId(int orderPartId) {
		this.orderPartId = orderPartId;
	}

	public void setFactCount(int factCount){
		this.factCount = factCount;
	}

	public int getFactCount(){
		return factCount;
	}

	public void setOrderItemrefinements(List<Object> orderItemrefinements){
		this.orderItemrefinements = orderItemrefinements;
	}

	public List<Object> getOrderItemrefinements(){
		return orderItemrefinements;
	}

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setDescriptor(String descriptor){
		this.descriptor = descriptor;
	}

	public String getDescriptor(){
		return descriptor;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setDepartmentId(int departmentId){
		this.departmentId = departmentId;
	}

	public int getDepartmentId(){
		return departmentId;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	@Override
 	public String toString(){
		return 
			"OrderItem{" +
			"factCount = '" + factCount + '\'' + 
			",orderItemrefinements = '" + orderItemrefinements + '\'' + 
			",stateId = '" + stateId + '\'' + 
			",descriptor = '" + descriptor + '\'' + 
			",productId = '" + productId + '\'' + 
			",id = '" + id + '\'' + 
			",count = '" + count + '\'' + 
			",departmentId = '" + departmentId + '\'' + 
			",orderId = '" + orderId + '\'' + 
			"}";
		}
}