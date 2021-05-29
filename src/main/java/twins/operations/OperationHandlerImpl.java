package twins.operations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import twins.dal.ItemDao;
import twins.dal.UserDao;
import twins.data.ItemEntity;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.data.UserRole;
import twins.errors.BadRequestException;
import twins.errors.NotFoundException;
import twins.item.ItemBoundary;
import twins.item.ItemTypes;
import twins.logic.ItemConverter;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationHandler;
import twins.logic.UpdatedItemService;
import twins.logic.UserItemConverter;

@Component
public class OperationHandlerImpl implements OperationHandler{

	private UserDao userDao;
	private ItemDao itemDao;
	private ItemConverter itemConverter;
	private UserItemConverter userToItemConverter;
	private OperationEntityConverter operationEntityConverter;
	private UpdatedItemService itemService;
	private String type;
	private String gradeType;
	private String space;

	public OperationHandlerImpl() { }

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	
	@Autowired
	public void setItemService(UpdatedItemService itemService) {
		this.itemService = itemService;
	}

	@Autowired
	public void setItemConverter(ItemConverter itemConverter) {
		this.itemConverter = itemConverter;
	}

	@Autowired
	public void setOperationEntityConverter(OperationEntityConverter operationEntityConverter) {
		this.operationEntityConverter = operationEntityConverter;
	}

	@Autowired
	public void setUserToItemConverter(UserItemConverter userToItemConverter) {
		this.userToItemConverter = userToItemConverter;
	}
	
	@Value("${type:Type}")
	public void setCourseType(String type) {
		this.type = type;
	}
	
	@Value("${gradeType:Grade}")
	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}
	
	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpace(String space) {
		this.space = space;
	}
	
	@Override
	public void registerToCourse(OperationEntity operation) {
		//check if Course exists and active
		String itemId = /*this.space + "_" + */operation.getItemId();
		Optional<ItemEntity> optionalItem = this.itemDao.findByIdAndActive(itemId, true); 
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");
		
		//Extract user type from operation attributes
		String type = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.type);
		if(type.equals(ItemTypes.Staff.toString()))
			throw new  BadRequestException("Invalid operation for type" + type);
		
		//Extract user email from Operation attributes
		String userEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get("Id");
		String userId = userEmail + "@@" + this.space;

		//Check user existence
		Optional<UserEntity> optionalUser = this.userDao.findById(userId);
		if(!optionalUser.isPresent())
			throw new BadRequestException("User " + userEmail + " doesn't exist");
		
		//Check if user is Lecturer
		if(type.equals(ItemTypes.Lecturer.toString())) {
			//Check if there's a lecturer registered to the course
			Optional<ItemEntity> lecturer = this.itemDao.findLecturerByTypeAndActiveAndParents_id(type, true, itemId);
			if(lecturer.isPresent())
				throw new BadRequestException("Only one " + type + " allowed per Course");
		}
		
		//Check if the user already registered 
		Optional<ItemEntity> registered = this.itemDao.findStudentByNameAndParents_id(userEmail, itemId);
		if(registered.isPresent() && registered.get().isActive())
			throw new BadRequestException("Student " + userEmail + " already registered");
		if(registered.isPresent() && !registered.get().isActive()) {
			registered.get().setActive(true);
			return;
		}
			
		
		//Grant the User temporary MANAGER role to convert himself to Item
		String role = optionalUser.get().getRole();
		optionalUser.get().setRole(UserRole.MANAGER.toString());
		
		//Add Student (as Item) to the Course
		ItemEntity userAsItem = this.userToItemConverter.UserToItem(optionalUser.get());
		this.itemDao.save(userAsItem);
		this.itemService.addChildToItem(optionalItem.get().getId(), this.itemConverter.toBoundary(userAsItem).getItemId(), userEmail, this.space);
		optionalUser.get().setRole(role);
	}

	@Override
	public void resignFromCourse(OperationEntity operation) {
		//check if Course exists and active
		String itemId = /*this.space + "_" + */operation.getItemId();
		Optional<ItemEntity> optionalItem = this.itemDao.findByIdAndActive(itemId, true); 
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");

		//Extract user type from operation attributes
		String type = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get(this.type);
		if (type.equals(ItemTypes.Staff.toString()))
			throw new BadRequestException("Invalid operation for type" + type);

		// Extract user email from Operation attributes
		String userEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get("Id");
		String userId = userEmail + "@@" + this.space;

		// Check user existence
		Optional<UserEntity> optionalUser = this.userDao.findById(userId);
		if (!optionalUser.isPresent())
			throw new BadRequestException("User " + userEmail + " doesn't exist");
		
		//Check if the student registered to the course
		Optional<ItemEntity> user = this.itemDao.findStudentByNameAndParents_id(userEmail, itemId);
		if (!user.isPresent())
			throw new BadRequestException("Student " + userEmail + " isn't registered to Course " + itemId);
		else if(!user.get().isActive())
				throw new BadRequestException("Student " + userEmail + " isn't registered to Course " + itemId);
		
		user.get().setActive(false);
	}

	@Override
	public void updateGrade(OperationEntity operation) {
		//check if Course exists and active
		String itemId = /*this.space + "_" + */operation.getItemId();
		Optional<ItemEntity> optionalItem = this.itemDao.findByIdAndActive(itemId, true);
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");
		
		//Extract user type from operation attributes
		String type = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get(this.type);
		if (type.equals(ItemTypes.Staff.toString()))
			throw new BadRequestException("Invalid operation for type" + type);
		if(type.equals(ItemTypes.Student.toString()))
			throw new BadRequestException("Invalid operation for type" + type);

		// Extract user email from Operation attributes
		String studentEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get("Id");
		String studentId = studentEmail + "@@" + this.space;

		// Check user existence
		Optional<UserEntity> optionalUser = this.userDao.findById(studentId);
		if (!optionalUser.isPresent())
			throw new BadRequestException("User " + studentEmail + " doesn't exist");

		//Check if the student registered to the course
		Optional<ItemEntity> student = this.itemDao.findStudentByNameAndParents_id(studentEmail, itemId);
		if(!student.isPresent())
			throw new BadRequestException("Student " + studentEmail + " isn't registered to Course " + itemId);
		else if(!student.get().isActive())
			throw new BadRequestException("Student " + studentEmail + " isn't registered to Course " + itemId);
		
		//update grade
		//expect only one grade to be passed
		String grade = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.gradeType);		
		Map<String, Object> attr = (Map<String, Object>) this.itemConverter.fromJsonToMap(student.get().getItemAttributes());
		attr.put(this.gradeType, grade);
		student.get().setItemAttributes(this.itemConverter.fromMapToJson(attr));
	}

	@Override
	public void removeCourse(OperationEntity operation) {
		//check if Course exists and active
		String itemId = /*this.space + "_" + */operation.getItemId();
		Optional<ItemEntity> optionalItem = this.itemDao.findByIdAndActive(itemId, true);
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");
		
		//Extract user type from operation attributes
		String type = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get(this.type);
		if (!type.equals(ItemTypes.Staff.toString()))
			throw new BadRequestException("Invalid operation for type" + type);
		
		//Check user existence
		String userEmail = operation.getUserEmail();
		String userId = userEmail + "@@" + this.space;
		Optional<UserEntity> optionalUser = this.userDao.findById(userId);
		if (!optionalUser.isPresent())
			throw new BadRequestException("User " + userEmail + " doesn't exist");
		
		optionalItem.get().setActive(false);
	}
	
	@Override
	public List<ItemBoundary> getRegisteredCourses(OperationEntity operation) {
		//Extract Student email from Operation attributes
		String studentEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get("Id");
		String studentId = studentEmail + "@@" + this.space;

		// Check Student existence
		Optional<UserEntity> optionalStudent = this.userDao.findById(studentId);
		if (!optionalStudent.isPresent())
			throw new BadRequestException("Student " + studentEmail + " doesn't exist");
		
		return this.itemDao.findAllStudentsByChildren_nameAndChildren_active(studentEmail, true).stream().map(this.itemConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<ItemBoundary> getAllCourses(OperationEntity operation) {
		return this.itemDao.findAllByTypeAndActive(ItemTypes.Course.toString(), true, Sort.by("name").ascending())
						.stream().map(this.itemConverter::toBoundary).collect(Collectors.toList());
	}
	
}
