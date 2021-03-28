package twins.data;

import java.util.Date;
import java.util.Map;

import twins.item.ItemIdBoundary;
import twins.operations.ItemWrapper;
import twins.operations.OperationId;
import twins.operations.UserIdWrapper;
import twins.user.UserId;

public class OperationEntity {
	private String operationSpace;
	private String operationId;
	private String type;
	private String itemSpace;
	private Long itemId;
	private Date createdTimestamp;
	private String userSpace;
	private String userEmail;
	private Map<String, Object> operationAttributes;

	public OperationEntity() {}

	
	
	public OperationEntity(String operationSpace, String operationId, String type, String itemSpace, Long itemId,
			Date createdTimestamp, String userSpace, String userEmail, Map<String, Object> operationAttributes) {
		super();
		this.operationSpace = operationSpace;
		this.operationId = operationId;
		this.type = type;
		this.itemSpace = itemSpace;
		this.itemId = itemId;
		this.createdTimestamp = createdTimestamp;
		this.userSpace = userSpace;
		this.userEmail = userEmail;
		this.operationAttributes = operationAttributes;
	}



	public String getOperationSpace() {
		return operationSpace;
	}

	public void setOperationSpace(String operationSpace) {
		this.operationSpace = operationSpace;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItemSpace() {
		return itemSpace;
	}

	public void setItemSpace(String itemSpace) {
		this.itemSpace = itemSpace;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getUserSpace() {
		return userSpace;
	}

	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Map<String, Object> getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(Map<String, Object> operationAttributes) {
		this.operationAttributes = operationAttributes;
	}
	
	
}
