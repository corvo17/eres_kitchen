package kitchen.eres.com.kitchen.net.Pojos;

import java.util.ArrayList;

public class GetMe {
    private String status;
    private String msg;
    private MyData data;
    private ArrayList<GetUser> users;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MyData getData() {
        return data;
    }

    public void setData(MyData data) {
        this.data = data;
    }

    public ArrayList<GetUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<GetUser> users) {
        this.users = users;
    }
}
