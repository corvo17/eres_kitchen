package kitchen.eres.com.kitchen.net.Pojos.ordersPojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ResponseOrder  implements Parcelable {


	private String publicatedTime;

	private List<OrderItem> orderItems;

	private String acceptedTime;

	private String completedTime;


	private int stateId;

	private String descriptor;

	private int id;

	private int orderId;

	private int departmentId;

	private int tableName;
	private String waiterName;
	private boolean isSelected = false;
	public boolean getIsSelected(){
		return isSelected;
	}

	public  void setIsSelected(boolean isSelected){
		this.isSelected = isSelected;
	}

	public int getTableName() {
		return tableName;
	}

	public void setTableName(int tableName) {
		this.tableName = tableName;
	}

	public ResponseOrder(){

}
	protected ResponseOrder(Parcel in) {
		publicatedTime = in.readString();
		acceptedTime = in.readString();
		completedTime = in.readString();
		stateId = in.readInt();
		descriptor = in.readString();
		id = in.readInt();
		orderId = in.readInt();
		departmentId = in.readInt();
	}

	public static final Creator<ResponseOrder> CREATOR = new Creator<ResponseOrder>() {
		@Override
		public ResponseOrder createFromParcel(Parcel in) {
			return new ResponseOrder(in);
		}

		@Override
		public ResponseOrder[] newArray(int size) {
			return new ResponseOrder[size];
		}
	};

	public String getPublicatedTime() {
		return publicatedTime;
	}

	public String getWaiterName() {
		return waiterName;
	}

	public void setWaiterName(String waiterName) {
		this.waiterName = waiterName;
	}

	public void setPublicatedTime(String publicatedTime) {
		this.publicatedTime = publicatedTime;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getAcceptedTime() {
		return acceptedTime;
	}

	public void setAcceptedTime(String acceptedTime) {
		this.acceptedTime = acceptedTime;
	}

	public String getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(String completedTime) {
		this.completedTime = completedTime;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public String toString() {
		return "ResponseOrder{" +
				"publicatedTime='" + publicatedTime + '\'' +
				", orderItems=" + orderItems +
				", acceptedTime='" + acceptedTime + '\'' +
				", completedTime='" + completedTime + '\'' +
				", stateId=" + stateId +
				", descriptor='" + descriptor + '\'' +
				", id=" + id +
				", orderId=" + orderId +
				", departmentId=" + departmentId +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(publicatedTime);
		parcel.writeTypedList(orderItems);
		parcel.writeString(acceptedTime);
		parcel.writeString(completedTime);
		parcel.writeInt(stateId);
		parcel.writeString(descriptor);
		parcel.writeInt(id);
		parcel.writeInt(orderId);
		parcel.writeInt(departmentId);
	}
}