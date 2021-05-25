package twins.operations;

import java.util.ArrayList;
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
import twins.errors.BadRequestException;
import twins.errors.NotFoundException;
import twins.item.ItemBoundary;
import twins.logic.ItemConverter;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationHandler;
import twins.logic.UserItemConverter;

@Component
public class OperationHandlerImpl implements OperationHandler{

	private UserDao userDao;
	private ItemDao itemDao;
	private ItemConverter itemConverter;
	private UserItemConverter userToItemConverter;
	private OperationEntityConverter operationEntityConverter;
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
	public void registerToCourse(OperationEntity operation/*, ItemEntity item*/) {
		//convert user to item
		//add the Student item to the Course item
		//Check Item existence
		Optional<ItemEntity> optionalItem = this.itemDao.findById(this.space + "_" + operation.getItemId()); 
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");

		// Check if item is active
		ItemEntity item = optionalItem.get();
		if (!item.isActive())
			throw new BadRequestException(
					"Can't invoke " + operation.getType() + " on item " + operation.getItemId());
		
		String studentId = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.studentType);
		String id = studentId + "@@" + this.space;
		System.err.println("studentId: " + studentId + "\tId: " + id + "\tSpace: " + space);
		Optional<UserEntity> optionalStudent = this.userDao.findById(id);
		if(!optionalStudent.isPresent())
			throw new BadRequestException("Student " + id + " doesn't exist");
		
		ItemEntity studentAsItem = this.userToItemConverter.UserToItem(optionalStudent.get());
		item.addChild(studentAsItem);
	}

	@Override
	public void resignFromCourse(OperationEntity operation/*, ItemEntity item*/) {
		Optional<ItemEntity> optionalItem = this.itemDao.findById(operation.getItemId()); 
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");

		// Check if item is active
		ItemEntity item = optionalItem.get();
		if (!item.isActive())
			throw new BadRequestException(
					"Can't invoke " + operation.getType() + " on item " + operation.getItemId());
		//search parent item (Course) for students
		//remove Student item from parent (set active to false)
		String studentId = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.studentType);
		String id = studentId + "@@" + this.space;
		System.err.println("studentId: " + studentId + "\tId: " + id + "\tSpace: " + space);
		Optional<UserEntity> optionalStudent = this.userDao.findById(id);
		if(!optionalStudent.isPresent())
			throw new BadRequestException("Student " + id + " doesn't exist");
		
		List<ItemEntity> courses = this.itemDao.findAllByNameAndTypeAndActive(item.getName(), this.courseType, true, Sort.by("name").ascending());
		courses.stream().forEach(course-> {
			course.getChildren()
					.stream()
					.forEach(student-> {
						if(this.itemConverter.fromJsonToMap(student.getItemAttributes()).get(this.studentType).equals(studentId) && student.isActive()) {
							student.setActive(false);
						}
					});});
	}

	@Override
	public void updateGrade(OperationEntity operation/*, ItemEntity item*/) {
		Optional<ItemEntity> optionalItem = this.itemDao.findById(operation.getItemId()); 
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");

		// Check if item is active
		ItemEntity item = optionalItem.get();
		if (!item.isActive())
			throw new BadRequestException(
					"Can't invoke " + operation.getType() + " on item " + operation.getItemId());
		//search for item Course
		//search for child item (Student)
		//update grade
		//expect only one grade to be passed
		String studentId = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.studentType);
		Optional<UserEntity> optionalStudent = this.userDao.findFirstByEmailSpace(studentId);
		if(!optionalStudent.isPresent())
			throw new BadRequestException("Student " + studentId + " doesn't exist");
		
		String grade = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.gradeType);
		List<ItemEntity> courses = this.itemDao.findAllByNameAndTypeAndActive(item.getName(), this.courseType, true, Sort.by("name").ascending());
		courses.stream().forEach(course-> {
			course.getChildren()
					.stream()
					.forEach(student-> {
						if(this.itemConverter.fromJsonToMap(student.getItemAttributes()).get(this.studentType).equals(studentId) && student.isActive()) {
							this.itemConverter.fromMapToJson((Map<String, Object>) this.itemConverter.fromJsonToMap(student.getItemAttributes()).put(this.gradeType, grade));
						}
					});});
	}

	@Override
	public void removeCourse(OperationEntity operation/*, ItemEntity item*/) {
		Optional<ItemEntity> optionalItem = this.itemDao.findById(operation.getItemId()); 
		if (!optionalItem.isPresent())
			throw new NotFoundException("Item " + operation.getItemId() + " doesn't exist");

		// Check if item is active
		ItemEntity item = optionalItem.get();
		if (!item.isActive())
			throw new BadRequestException(
					"Can't invoke " + operation.getType() + " on item " + operation.getItemId());
		//search for item
		//remove item
		List<ItemEntity> courses = this.itemDao.findAllByNameAndTypeAndActive(item.getName(), this.courseType, true, Sort.by("name").ascending());
		courses.stream().forEach(e-> e.setActive(false));
	}
	
	//TODO: Expensive! maybe there's another way?
	@Override
	public List<ItemBoundary> getRegisteredCourses(OperationEntity operation) {
		String studentId = (String) this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes()).get(this.studentType);
		Optional<UserEntity> student = this.userDao.findFirstByEmailSpace(studentId);
		if(!student.isPresent())
			throw new BadRequestException("Student " + studentId + " doesn't exist");
		
		List<ItemEntity> courses = this.itemDao.findAllByTypeAndActive(this.courseType, true, Sort.by("name").ascending());
		List<ItemEntity> studentRegisteredCourses = new ArrayList<>();
		for(ItemEntity course : courses)
		{
			for(ItemEntity studentItem: course.getChildren()) {
				if(this.itemConverter.fromJsonToMap(studentItem.getItemAttributes()).get(this.studentType).equals(studentId) && studentItem.isActive()) {
					studentRegisteredCourses.add(course);
				}
			}
		}
		return studentRegisteredCourses.stream().map(this.itemConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public List<ItemBoundary> getAllCourses(OperationEntity operation) {
		return this.itemDao.findAllByTypeAndActive(this.courseType, true, Sort.by("name").ascending())
						.stream().map(this.itemConverter::toBoundary).collect(Collectors.toList());
	}
	
}
