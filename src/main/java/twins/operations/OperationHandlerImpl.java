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
	private String courseType;
	private String studentType;
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
	
	@Value("${courseType:Course}")
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	
	@Value("${studentType:Student}")
	public void setStudentType(String studentType) {
		this.studentType = studentType;
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
		
		//Extract Student email from Operation attributes
		String studentEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.studentType);
		String studentId = studentEmail + "@@" + this.space;

		//Check Student existence
		Optional<UserEntity> optionalStudent = this.userDao.findById(studentId);
		if(!optionalStudent.isPresent())
			throw new BadRequestException("Student " + studentEmail + " doesn't exist");
		
		//Check if the student already registered 
		Optional<ItemEntity> student = this.itemDao.findByNameAndParents_id(studentEmail, itemId);
		if(student.isPresent() && student.get().isActive())
			throw new BadRequestException("Student " + studentEmail + " already registered");
		if(student.isPresent() && !student.get().isActive()) {
			student.get().setActive(true);
			return;
		}
			
		
		//Grant Student temporary MANAGER role to convert himself to Item
		String role = optionalStudent.get().getRole();
		optionalStudent.get().setRole(UserRole.MANAGER.toString());
		
		//Add Student (as Item) to the Course
		ItemEntity studentAsItem = this.userToItemConverter.UserToItem(optionalStudent.get());
		this.itemDao.save(studentAsItem);
		this.itemService.addChildToItem(optionalItem.get().getId(), this.itemConverter.toBoundary(studentAsItem).getItemId(), studentEmail, this.space);
		optionalStudent.get().setRole(role);
	}

	@Override
	public void resignFromCourse(OperationEntity operation) {
		//check if Course exists and active
		String itemId = /*this.space + "_" + */operation.getItemId();
		Optional<ItemEntity> optionalItem = this.itemDao.findByIdAndActive(itemId, true); 
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");

		//Extract Student email from Operation attributes
		String studentEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.studentType);
		String studentId = studentEmail + "@@" + this.space;

		//Check Student existence
		Optional<UserEntity> optionalStudent = this.userDao.findById(studentId);
		if(!optionalStudent.isPresent())
			throw new BadRequestException("Student " + studentEmail + " doesn't exist");
		
		//Check if the student registered to the course
		Optional<ItemEntity> student = this.itemDao.findByNameAndParents_id(studentEmail, itemId);
		if (!student.isPresent())
			throw new BadRequestException("Student " + studentEmail + " isn't registered to Course " + itemId);
		else if(!student.get().isActive())
				throw new BadRequestException("Student " + studentEmail + " isn't registered to Course " + itemId);
		
		student.get().setActive(false);
	}

	@Override
	public void updateGrade(OperationEntity operation) {
		//check if Course exists and active
		String itemId = /*this.space + "_" + */operation.getItemId();
		Optional<ItemEntity> optionalItem = this.itemDao.findByIdAndActive(itemId, true);
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");
		
		//Extract Student email from Operation attributes
		String studentEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get(this.studentType);
		String studentId = studentEmail + "@@" + this.space;

		// Check Student existence
		Optional<UserEntity> optionalStudent = this.userDao.findById(studentId);
		if (!optionalStudent.isPresent())
			throw new BadRequestException("Student " + studentEmail + " doesn't exist");

		//Check if the student registered to the course
		Optional<ItemEntity> student = this.itemDao.findByNameAndParents_id(studentEmail, itemId);
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

		optionalItem.get().setActive(false);
	}
	
	@Override
	public List<ItemBoundary> getRegisteredCourses(OperationEntity operation) {
		//Extract Student email from Operation attributes
		String studentEmail = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes())
				.get(this.studentType);
		String studentId = studentEmail + "@@" + this.space;

		// Check Student existence
		Optional<UserEntity> optionalStudent = this.userDao.findById(studentId);
		if (!optionalStudent.isPresent())
			throw new BadRequestException("Student " + studentEmail + " doesn't exist");
		
		return this.itemDao.findAllByChildren_nameAndChildren_active(studentEmail, true).stream().map(this.itemConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<ItemBoundary> getAllCourses(OperationEntity operation) {
		return this.itemDao.findAllByTypeAndActive(this.courseType, true, Sort.by("name").ascending())
						.stream().map(this.itemConverter::toBoundary).collect(Collectors.toList());
	}
	
}
