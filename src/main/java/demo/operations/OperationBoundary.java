package demo.operations;
/*
 * OperationBoundary sample JSON:
	{
		"operationId":{
			"space":"2021b.twins",
			"id":"451"
		},
		"type":"operationType",
		"item":{
			"itemId":{
			"space":"2021b.twins",
			"id":"99"
			}
		},
		"createdTimestamp":"2021-03-07T09:57:13.109+0000",
		"invokedBy":{
			"userId":{
			"space":"2021b.twins",
			"email":"user3@@demo.com"
			}
		},
		"operationAttributes":{
			"key1":"can be set to any value you wish",
			"key2":{
				"key2Subkey1":"can be nested json"
			}
		}
	}
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OperationBoundary {
	private OperationId operationId;
	private String type;
	private ItemWrapper item;
	private Date timestamp;
	private UserIdWrapper invokedBy;
	private Map<String, Object> operationsAttributes;
	
	public OperationBoundary() {
		this.operationId = new OperationId();
		this.type = "random_number";
		this.item = new ItemWrapper();
		this.timestamp = new Date();
		this.invokedBy = new UserIdWrapper();
		this.operationsAttributes = new HashMap<>();
	}

	public OperationId getOperationId() {
		return operationId;
	}

	public void setOperationId(OperationId operationId) {
		this.operationId = operationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ItemWrapper getItem() {
		return item;
	}

	public void setItem(ItemWrapper item) {
		this.item = item;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public UserIdWrapper getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(UserIdWrapper invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getOperationsAttributes() {
		return operationsAttributes;
	}

	public void setOperationsAttributes(Map<String, Object> operationsAttributes) {
		this.operationsAttributes = operationsAttributes;
	}
	
	public void addOperationAttribute(String key, Object value) {
		this.operationsAttributes.put(key, value);
	}
}
