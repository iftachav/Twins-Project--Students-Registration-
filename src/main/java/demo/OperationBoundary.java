package demo;
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

public class OperationBoundary {
	private OperationId id;
	private String type;
	private Item item;
	private Date timestamp;
	private UserId invokedBy;
	private HashMap<String, Object> operationsAttributes;
	
	public OperationBoundary() {
		this.id = new OperationId("2021b.twins");
		this.type = "random_number";
		this.item = new Item();
		this.timestamp = new Date();
		this.invokedBy = new UserId();
		this.operationsAttributes = new HashMap<>();
	}

	public OperationId getId() {
		return id;
	}

	public void setId(OperationId id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public UserId getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(UserId invokedBy) {
		this.invokedBy = invokedBy;
	}

	public HashMap<String, Object> getOperationsAttributes() {
		return operationsAttributes;
	}

	public void setOperationsAttributes(HashMap<String, Object> operationsAttributes) {
		this.operationsAttributes = operationsAttributes;
	}
	
	public void addOperationAttribute(String key, Object value) {
		this.operationsAttributes.put(key, value);
	}
}
