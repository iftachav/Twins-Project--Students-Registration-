package twins.operations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twins.dal.ItemDao;
import twins.dal.OperationDao;
import twins.dal.UserDao;
import twins.data.ItemEntity;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.data.UserRole;
import twins.errors.BadRequestException;
import twins.errors.ForbiddenRequestException;
import twins.errors.NotFoundException;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationService;


@Service
public class OperationServiceJpa implements OperationService{
	private UserDao userDao;
	private ItemDao itemDao;
	private OperationDao operationDao;
	private OperationEntityConverter operationEntityConverter;
	private String springApplicationName;
	
	public OperationServiceJpa() { }
	
	@Autowired
	public void setOperationDao(OperationDao operationDao) {
		this.operationDao = operationDao;
	}
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	
	@Autowired
	public void setEntityConverter(OperationEntityConverter entityConverter) {
		this.operationEntityConverter = entityConverter;
	}

	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
	}
	
	@Override
	@Transactional
	public Object invokeOperation(OperationBoundary operation) {
		if(operation == null)
			throw new BadRequestException("Null Operation Received.");
		if(operation.getType() == null)
			throw new BadRequestException("Null operation type passed");
		if(operation.getType().equals(""))
			throw new BadRequestException("No operation type passed");
		if(operation.getInvokedBy() == null || operation.getInvokedBy().getUserId() == null)
			throw new BadRequestException("Null Invoked By Element Received.");
		if(!checkEmail(operation.getInvokedBy().getUserId().getEmail()))
			throw new BadRequestException("Email Is Not Valid.");
		if(operation.getItem() == null || operation.getItem().getItemId() == null|| operation.getItem().getItemId().getId()== null || operation.getItem().getItemId().getId().equals("") || operation.getItem().getItemId().getId() == null)
			throw new BadRequestException("Null Item Element Received.");
		
		//Check User existence
		Optional<UserEntity> optionalUser = this.userDao.findById(operation.getInvokedBy().getUserId().getEmail() + 
				"@@" + operation.getInvokedBy().getUserId().getSpace());	//search user by: UserEmail@@Space
		if(!optionalUser.isPresent())
			throw new NotFoundException("User " + operation.getInvokedBy().getUserId().getEmail() + " Doesn't exist");
		
		//Check user Role
		UserEntity user = optionalUser.get();
		if(user.getRole() != UserRole.PLAYER.toString())
			throw new ForbiddenRequestException("Operation " + operation.getType() + " not authorized for role " + user.getRole());
		
		//Check Item existence
		Optional<ItemEntity> optionalItem = this.itemDao.findById(operation.getItem().getItemId().getSpace() + "_" + 
				operation.getItem().getItemId().getId());	//search item by: Space_ItemId
		if(!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItem().getItemId().getId() + " doesn't exist");
		
		String newId= UUID.randomUUID().toString()+"_"+this.springApplicationName;
		
		OperationEntity entity = operationEntityConverter.fromBoundary(operation);
		entity.setCreatedTimestamp(new Date());
		entity.setOperationSpace(springApplicationName);
//		entity.setItemSpace(springApplicationName);		//?
//		entity.setUserSpace(springApplicationName);		//?
		entity.setOperationId(newId);
		entity.setOperationSpace(springApplicationName);
		
		operationDao.save(entity);
		return operationEntityConverter.toBoundary(entity);
	}

	@Override
	@Transactional
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		if(operation == null)
			throw new BadRequestException("Null Operation Received.");
		if(operation.getType() == null)
			throw new BadRequestException("Null operation type passed");
		if(operation.getType().equals(""))
			throw new BadRequestException("No operation type passed");
		if(operation.getInvokedBy() == null || operation.getInvokedBy().getUserId() == null)
			throw new BadRequestException("Null Invoked By Element Received.");
		if(!checkEmail(operation.getInvokedBy().getUserId().getEmail()))
			throw new BadRequestException("Email Is Not Valid.");
		if(operation.getItem() == null || operation.getItem().getItemId() == null|| operation.getItem().getItemId().getId()== null || operation.getItem().getItemId().getId().equals("") || operation.getItem().getItemId().getId() == null)
			throw new BadRequestException("Null Item Element Received.");
		
		//Check User existence
		Optional<UserEntity> optionalUser = this.userDao.findById(operation.getInvokedBy().getUserId().getEmail() + 
				"@@" + operation.getInvokedBy().getUserId().getSpace());	//search user by: UserEmail@@Space
		if(!optionalUser.isPresent())
			throw new NotFoundException("User " + operation.getInvokedBy().getUserId().getEmail() + " Doesn't exist");
		
		//Check user Role
		UserEntity user = optionalUser.get();
		if(user.getRole() != UserRole.PLAYER.toString())
			throw new ForbiddenRequestException("Operation " + operation.getType() + " does't authorized for role " + user.getRole());
		
		//Check Item existence
		Optional<ItemEntity> optionalItem = this.itemDao.findById(operation.getItem().getItemId().getSpace() + "_" + 
				operation.getItem().getItemId().getId());	//search item by: Space_ItemId
		if(!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItem().getItemId().getId() + " doesn't exist");
		
		String newId= UUID.randomUUID().toString()+"_"+this.springApplicationName;
		
		OperationEntity entity = operationEntityConverter.fromBoundary(operation);
		entity.setCreatedTimestamp(new Date());
		entity.setOperationSpace(springApplicationName);
//		entity.setItemSpace(springApplicationName);		//?
//		entity.setUserSpace(springApplicationName);		//?
		entity.setOperationId(newId);
		entity.setOperationSpace(springApplicationName);
		
		//TODO switch case
		handle(entity);
		
		operationDao.save(entity);
		return operationEntityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		if(!checkEmail(adminEmail))
			throw new BadRequestException(adminEmail + " is not a valid Email");
		
		Optional<UserEntity> optionalUser = this.userDao.findById(adminEmail + "@@" + adminSpace);
		if(!optionalUser.isPresent())
			throw new NotFoundException("User " + adminEmail + " doesn't exist");
		
		UserEntity admin = optionalUser.get();
		if(admin.getRole() != UserRole.ADMIN.toString())
			throw new ForbiddenRequestException("Operation doesn't authorized for role " + admin.getRole());
		
		Iterable<OperationEntity> allEntities=this.operationDao.findAll();
		return StreamSupport.stream(allEntities.spliterator(), false).map(this.operationEntityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		if(!checkEmail(adminEmail))
			throw new BadRequestException(adminEmail + " is not a valid Email");
		
		Optional<UserEntity> optionalUser = this.userDao.findById(adminEmail + "@@" + adminSpace);
		if(!optionalUser.isPresent())
			throw new NotFoundException("User " + adminEmail + " doesn't exist");
		
		UserEntity admin = optionalUser.get();
		if(admin.getRole() != UserRole.ADMIN.toString())
			throw new ForbiddenRequestException("Operation doesn't authorized for role " + admin.getRole());
		
		this.operationDao.deleteAll();
	}
	
	public boolean checkEmail(String email) {
		if(email==null)
			return false;
		String[] splitted=email.split("@");
		int size1=splitted.length;
		if(size1>2 || size1<1)
			return false;
		int size2=splitted[1].split("//.").length;
		if(size2>3 || size2<1)
			return false;
		return true;
	}
	
	private void handle(OperationEntity operation) {
		//operationHandler.handle(entity.getType());
		if(operation.getType().equals(OperationTypes.registerToCourse.toString())) {
			//convert user to item?
			//add the Student item to the Course item
		} else if(operation.getType().equals(OperationTypes.resignFromCourse.toString())){
			//convert user to item
			//search parent item (Course) for students
			//remove Student item from parent 
		} else if(operation.getType().equals(OperationTypes.removeCourse.toString())) {
			//search for item
			//remove item
		} else if(operation.getType().equals(OperationTypes.updateGrade.toString())) {
			//search for item Course
			//search for child item (Student)
			//update grade
		}
	}
}
