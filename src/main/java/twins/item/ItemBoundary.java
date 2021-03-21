package twins.item;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//JSON{
//	"itemId":{
//		"space":string- "2021b.twins",
//		"id":int- "99"
//	},
//	"type":"demoType",
//	"name":"demo item",
//	"active":true,
//	"createdTimestamp":Date & Time,
//	"createdBy":{
//		"userId":{
//			"space":"2021b.twins",
//			"email":"user2@demo.com"
//		}
//	},
//	"location":{
//		"lat":double- 32.115139,
//		"lng":double- 34.817804
//	},
//	"itemAttributes":{
//		"attribute1":string- "can be set to any value you wish",
//		"attribute2":string- "you can also name the attributes any name you like",
//		"attribute3":int- default:1,
//		"attribute4":boolean default:true
//	}
//}

public class ItemBoundary {
	private ItemIdBoundary itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private CreatedByBoundary createdBy;
	private LocationBoundary location;
	private Map<String, Object> itemAttributes;

	public ItemBoundary() {
		itemId = new ItemIdBoundary();
		active = true;
		createdTimestamp = new Date();
		createdBy= new CreatedByBoundary();
		location = new LocationBoundary();
		itemAttributes = new HashMap<>();
	}

	public ItemBoundary(String type, String name, Boolean active) {
		this();
		this.type = type;
		this.name = name;
		this.active = active;
	}

	public ItemBoundary(String type, String name, Boolean active,
			LocationBoundary location) {
		this();
		this.type = type;
		this.name = name;
		this.active = active;
		this.location = location;
	}
	

	public CreatedByBoundary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedByBoundary createdBy) {
		this.createdBy = createdBy;
	}

	public ItemIdBoundary getItemId() {
		return itemId;
	}

	public void setItemId(ItemIdBoundary itemId) {
		this.itemId = itemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public LocationBoundary getLocation() {
		return location;
	}

	public void setLocation(LocationBoundary location) {
		this.location = location;
	}

	public Map<String, Object> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(Map<String, Object> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
}
