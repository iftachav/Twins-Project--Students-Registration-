package twins.item;

//{
//	"space":"2021b.twins",
//	"id":"99"
//}

public class ItemIdBoundary {
	private String space;
	private Long id;

	public ItemIdBoundary() {
		this.space="iftachTeam";
		this.id = 1L;
	}

	public ItemIdBoundary(Long id) {
		this();
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
