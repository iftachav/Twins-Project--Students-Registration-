package demo.operations;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.item.ItemIdBoundary;

@RestController
public class OperationController {
	
	@RequestMapping(
			path = "/twins/operations",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary invokeOperationOnItem(@RequestBody OperationBoundary input) {
		System.err.println("(STUB) operation successfully invoked on item");
		return input;
	}
	
	@RequestMapping(
			path = "/twins/operations/async",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary ASynchronousOperation(@RequestBody OperationBoundary input) {
		//input.setId(new OperationId());
		input.addOperationAttribute("key1", "can be set to any value you wish");
		System.err.println("(STUB) A Synchronous operation successfully invoked on item");
		return input;
	}
	
	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperations(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
		System.err.println("(STUB) All operation deleted successfully");
	}
	
	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail){
		OperationBoundary[] tmp = Stream.of(new OperationBoundary(), new OperationBoundary(), new OperationBoundary())
				.map(input->{ return input; }).collect(Collectors.toList()).toArray(new OperationBoundary[0]);
		System.err.println("(STUB) All operation exported successfully");
		return tmp;
	}
}
