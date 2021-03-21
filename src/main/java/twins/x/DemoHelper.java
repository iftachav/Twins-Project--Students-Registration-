package twins.x;

import org.springframework.stereotype.Component;

//@Component
public class DemoHelper {
	public DemoHelper() {
		System.err.println("helper instance was generated");
	}

	// NOTE: this constructor requires defining a Spring Bean of type: String
//	public DemoHelper(String init) {
//		System.err.println("helper instance was generated - using init parameter: " + init);
//	}
	
}
