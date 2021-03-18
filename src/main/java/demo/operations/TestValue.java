package demo.operations;

import java.util.HashMap;
import java.util.Map;

public class TestValue {
	private Map<String, Object> map;
	
	public TestValue() {
		this.map = new HashMap<>();
		addValues("key2Subkey1", "can be nested json");
	}
	
	public TestValue(String key, Object value) {
		addValues(key, value);
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	public void addValues(String key, Object value) {
		map.put(key, value);
	}
}
