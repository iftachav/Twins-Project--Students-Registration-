package twins.item;


//{
//	"key1":"can be set to any value you wish",
//	"key2":"you can also name the attributes any name you like",
//	"key3":58,
//	"key4":false
//}

public class ItemAttributesBoundary {
	private String attribute1;
	private String attribute2;
	private int attribute3;
	private Boolean attribute4;

	public ItemAttributesBoundary() {
	}

	public ItemAttributesBoundary(String attribute1, String attribute2, int attribute3, Boolean attribute4) {
		this();
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		this.attribute3 = attribute3;
		this.attribute4 = attribute4;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public int getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(int attribute3) {
		this.attribute3 = attribute3;
	}

	public Boolean getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(Boolean attribute4) {
		this.attribute4 = attribute4;
	}
}
