package twins.item;

//{
//	"space":"2021b.twins",
//	"id":"99"
//}

public class ItemIdBoundary {
	private String space;
	private Long id;

	public ItemIdBoundary() {}

	public ItemIdBoundary(Long id, String space) {
		this.space = space;
		this.id = id;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
