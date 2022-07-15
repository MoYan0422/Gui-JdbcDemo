package tw.Project_1.material;

public class Hospital {

	private String city;
	private String dist;
	private String hospitalName;
	private String addr;
	private String telephoneNumber;

	public Hospital() {

	}

	public Hospital(String city, String dist, String hospitalName, String addr, String telephoneNumber) {
		super();
		this.city = city;
		this.dist = dist;
		this.hospitalName = hospitalName;
		this.addr = addr;
		this.telephoneNumber = telephoneNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String toString = "   醫院名稱: "+ hospitalName + ", 地址: "+addr + ", 電話: "+telephoneNumber+"   ";
		return toString;
	}
	
}
