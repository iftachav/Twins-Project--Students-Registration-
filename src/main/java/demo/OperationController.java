package demo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xmlunit.builder.Input;

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
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperations(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
		
	}
	
	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail){
		OperationBoundary[] tmp = Stream.of(new OperationBoundary(), new OperationBoundary(), new OperationBoundary())
				.map(input->{ return input; }).collect(Collectors.toList()).toArray(new OperationBoundary[0]);
		return tmp;
	}
}
