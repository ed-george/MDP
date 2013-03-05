package ed.george.addressbook;

public class Contact {

	private String name;
	private String number;
	private String email;
	
	Contact(String name, String number, String email){
		this.name = name;
		this.number = number;
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public String getEmail() {
		return email;
	}
	
	
	
	
}
