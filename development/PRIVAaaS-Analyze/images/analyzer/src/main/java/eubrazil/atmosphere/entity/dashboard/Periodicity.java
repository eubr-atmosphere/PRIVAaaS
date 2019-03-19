package eubrazil.atmosphere.entity.dashboard;

public class Periodicity {

	private String name;
	private float data;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getData() {
		return data;
	}
	public void setData(float data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Periodicity [name=" + name + ", data=" + data + "]";
	}
	
}
