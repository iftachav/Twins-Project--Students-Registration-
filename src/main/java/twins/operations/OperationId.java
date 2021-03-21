package twins.operations;

public class OperationId {
	private String space;
	private String id;
	
	public OperationId() {
		this.space = "2021b.twins";
		this.id = "451";
	}

	public OperationId(String space) {
		this.space = space;
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
