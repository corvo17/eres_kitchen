package kitchen.eres.com.kitchen.net.Pojos.ordersPojo;

import android.os.Parcel;
import android.os.Parcelable;

class MyOrderItem implements Parcelable {
    private int state = 0;

    protected MyOrderItem(Parcel in) {
        state = in.readInt();
    }

    public static final Creator<MyOrderItem> CREATOR = new Creator<MyOrderItem>() {
        @Override
        public MyOrderItem createFromParcel(Parcel in) {
            return new MyOrderItem(in);
        }

        @Override
        public MyOrderItem[] newArray(int size) {
            return new MyOrderItem[size];
        }
    };

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(state);
    }
}
