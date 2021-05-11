package twins.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twins.logic.OperationService;
import twins.logic.UpdatedOperationService;

@RestController
public class OperationController {
	private UpdatedOperationService operationService;

	@Autowired
	public OperationController(UpdatedOperationService operationService) {
		this.operationService = operationService;
	}

	@RequestMapping(path = "/twins/operations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeOperationOnItem(@RequestBody OperationBoundary input) {
		return operationService.invokeOperation(input);
	}

	@RequestMapping(path = "/twins/operations/async", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary ASynchronousOperation(@RequestBody OperationBoundary input) {
		return operationService.invokeAsynchronousOperation(input);
	}

	@RequestMapping(path = "/twins/admin/operations/{userSpace}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteAllOperations(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		operationService.deleteAllOperations(userSpace, userEmail);
	}

	@RequestMapping(path = "/twins/admin/operations/{userSpace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "20") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
//		OperationBoundary[] tmp = Stream.of(new OperationBoundary(), new OperationBoundary(), new OperationBoundary())
//				.map(input->{ return input; }).collect(Collectors.toList()).toArray(new OperationBoundary[0]);
		return operationService.getAllOperations(userSpace, userEmail, size, page).toArray(new OperationBoundary[0]);
	}
}
