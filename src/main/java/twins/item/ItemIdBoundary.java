package twins.item;

//{
//	"space":"2021b.twins",
//	"id":"99"
//}

public class ItemIdBoundary {
	private String space;
	private String id;

	public ItemIdBoundary() {}

	public ItemIdBoundary(String id, String space) {
		this.space = space;
		this.id = id;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
