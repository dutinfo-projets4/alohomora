package alohomora.model;

public class Config {
	private int id;
	private String name;
	private String value;

	public Config(int id, String name, String value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
