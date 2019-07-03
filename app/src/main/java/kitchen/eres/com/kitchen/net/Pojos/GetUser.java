package kitchen.eres.com.kitchen.net.Pojos;

public class GetUser{
	private String password;
	private String posswordSlate;
	private String name;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setPosswordSlate(String posswordSlate){
		this.posswordSlate = posswordSlate;
	}

	public String getPosswordSlate(){
		return posswordSlate;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"GetUser{" + 
			"password = '" + password + '\'' + 
			",posswordSlate = '" + posswordSlate + '\'' + 
			",name = '" + name + '\'' + 
			"}";
		}
}
