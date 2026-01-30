package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course-details")
@CrossOrigin(origins = {"http://localhost:5173", "https://*.vercel.app", "https://teachytechie.com", "https://www.teachytechie.com"}, allowCredentials = "true")
public class CourseDetailController {
    
    @Autowired
    private CourseDetailService courseDetailService;
    
    // Get all course details
    @GetMapping("/all")
    public ResponseEntity<List<CourseDetail>> getAllCourseDetails() {
        List<CourseDetail> courseDetails = courseDetailService.getAllCourseDetails();
        return ResponseEntity.ok(courseDetails);
    }
    
    // Get course detail by ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetail> getCourseDetailById(@PathVariable Long id) {
        Optional<CourseDetail> courseDetail = courseDetailService.getCourseDetailById(id);
        return courseDetail.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Get course detail by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseDetail> getCourseDetailByCourseId(@PathVariable Long courseId) {
        Optional<CourseDetail> courseDetail = courseDetailService.getCourseDetailByCourseId(courseId);
        return courseDetail.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Create a new course detail
    @PostMapping("/create")
    public ResponseEntity<CourseDetail> createCourseDetail(@RequestBody CourseDetail courseDetail) {
        if (courseDetailService.existsByCourseId(courseDetail.getCourseId())) {
            return ResponseEntity.badRequest().build();
        }
        CourseDetail newCourseDetail = courseDetailService.createCourseDetail(courseDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourseDetail);
    }
    
    // Update an existing course detail by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CourseDetail> updateCourseDetail(@PathVariable Long id, @RequestBody CourseDetail courseDetail) {
        CourseDetail updatedCourseDetail = courseDetailService.updateCourseDetail(id, courseDetail);
        if (updatedCourseDetail != null) {
            return ResponseEntity.ok(updatedCourseDetail);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Update course detail by course ID
    @PutMapping("/update/course/{courseId}")
    public ResponseEntity<CourseDetail> updateCourseDetailByCourseId(@PathVariable Long courseId, @RequestBody CourseDetail courseDetail) {
        CourseDetail updatedCourseDetail = courseDetailService.updateCourseDetailByCourseId(courseId, courseDetail);
        if (updatedCourseDetail != null) {
            return ResponseEntity.ok(updatedCourseDetail);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete a course detail by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourseDetail(@PathVariable Long id) {
        boolean deleted = courseDetailService.deleteCourseDetail(id);
        if (deleted) {
            return ResponseEntity.ok("Course detail deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete course detail by course ID
    @DeleteMapping("/delete/course/{courseId}")
    public ResponseEntity<String> deleteCourseDetailByCourseId(@PathVariable Long courseId) {
        boolean deleted = courseDetailService.deleteCourseDetailByCourseId(courseId);
        if (deleted) {
            return ResponseEntity.ok("Course detail deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
    
    // Get course detail count
    @GetMapping("/count")
    public ResponseEntity<Long> getCourseDetailCount() {
        long count = courseDetailService.getCourseDetailCount();
        return ResponseEntity.ok(count);
    }
    
    // Initialize database with default course details for first 3 courses
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeCourseDetails() {
        // Check if course details already exist
        if (courseDetailService.getCourseDetailCount() > 0) {
            return ResponseEntity.badRequest().body("Course details already initialized");
        }
        
        // Course 1: C Programming
        String course1Modules = "[{\"id\":1,\"title\":\"Introduction to C\",\"lessons\":10,\"duration\":\"3 hours\",\"topics\":[\"History of C\",\"Setup Dev Environment\",\"First C Program\",\"Compilation Process\"]},{\"id\":2,\"title\":\"Data Types & Variables\",\"lessons\":12,\"duration\":\"4 hours\",\"topics\":[\"Basic Data Types\",\"Variables & Constants\",\"Type Conversion\",\"Storage Classes\"]},{\"id\":3,\"title\":\"Control Structures\",\"lessons\":14,\"duration\":\"5 hours\",\"topics\":[\"If-else Statements\",\"Switch Cases\",\"Loops\",\"Break & Continue\"]},{\"id\":4,\"title\":\"Functions & Pointers\",\"lessons\":16,\"duration\":\"6 hours\",\"topics\":[\"Function Declaration\",\"Pointers Basics\",\"Pointer Arithmetic\",\"Function Pointers\"]}]";
        String course1Reviews = "[{\"id\":1,\"name\":\"Rajesh Kumar\",\"avatar\":\"R\",\"rating\":5,\"time\":\"2 Months\",\"comment\":\"Excellent course for beginners! The pointer concepts were explained brilliantly.\"},{\"id\":2,\"name\":\"Priya Singh\",\"avatar\":\"P\",\"rating\":5,\"time\":\"3 Months\",\"comment\":\"Best C programming course I have taken. Clear explanations with practical examples.\"},{\"id\":3,\"name\":\"Amit Patel\",\"avatar\":\"A\",\"rating\":4,\"time\":\"1 Month\",\"comment\":\"Very good content. Would love more advanced projects.\"}]";
        String course1RatingDist = "[{\"stars\":5,\"count\":180,\"percentage\":72},{\"stars\":4,\"count\":50,\"percentage\":20},{\"stars\":3,\"count\":15,\"percentage\":6},{\"stars\":2,\"count\":3,\"percentage\":1.2},{\"stars\":1,\"count\":2,\"percentage\":0.8}]";
        String course1Overview = "{\"learn\":[\"Understand C syntax and structure\",\"Work with variables, data types, and operators\",\"Master control flow and functions\",\"Use pointers and memory management\",\"Build and debug C programs\",\"Apply C in real-world projects\"],\"prerequisites\":[\"Basic computer usage\",\"No prior programming experience required\",\"Logical thinking and curiosity\"],\"features\":[\"30+ hours of video lectures\",\"Downloadable code samples\",\"Hands-on assignments\",\"Certificate of completion\",\"Community Q&A forum\"]}";
        courseDetailService.createCourseDetail(new CourseDetail(1L, course1Modules, course1Reviews, 4.8, "2.5K", course1RatingDist, course1Overview));
        
        // Course 2: C++ Programming
        String course2Modules = "[{\"id\":1,\"title\":\"C++ Basics\",\"lessons\":11,\"duration\":\"3.5 hours\",\"topics\":[\"C++ Introduction\",\"OOP Concepts\",\"Classes & Objects\",\"Constructors\"]},{\"id\":2,\"title\":\"Advanced OOP\",\"lessons\":13,\"duration\":\"4.5 hours\",\"topics\":[\"Inheritance\",\"Polymorphism\",\"Encapsulation\",\"Abstraction\"]},{\"id\":3,\"title\":\"STL & Templates\",\"lessons\":15,\"duration\":\"5 hours\",\"topics\":[\"Standard Template Library\",\"Vectors\",\"Maps\",\"Templates\"]},{\"id\":4,\"title\":\"Modern C++ Features\",\"lessons\":12,\"duration\":\"4 hours\",\"topics\":[\"Smart Pointers\",\"Lambda Functions\",\"Move Semantics\",\"Threading\"]}]";
        String course2Reviews = "[{\"id\":1,\"name\":\"Vikram Reddy\",\"avatar\":\"V\",\"rating\":5,\"time\":\"4 Months\",\"comment\":\"Outstanding! The STL section was incredibly detailed and practical.\"},{\"id\":2,\"name\":\"Sneha Sharma\",\"avatar\":\"S\",\"rating\":5,\"time\":\"2 Months\",\"comment\":\"Finally understood OOP concepts properly. Highly recommended!\"},{\"id\":3,\"name\":\"Karthik M\",\"avatar\":\"K\",\"rating\":4,\"time\":\"1 Month\",\"comment\":\"Great course structure. Modern C++ features were well explained.\"}]";
        String course2RatingDist = "[{\"stars\":5,\"count\":210,\"percentage\":70},{\"stars\":4,\"count\":60,\"percentage\":20},{\"stars\":3,\"count\":20,\"percentage\":7},{\"stars\":2,\"count\":6,\"percentage\":2},{\"stars\":1,\"count\":4,\"percentage\":1}]";
        String course2Overview = "{\"learn\":[\"Grasp C++ syntax and OOP principles\",\"Implement classes, objects, and inheritance\",\"Utilize STL and templates\",\"Apply modern C++ features\",\"Develop and debug C++ applications\",\"Prepare for coding interviews\"],\"prerequisites\":[\"Basic programming knowledge (any language)\",\"Interest in software development\",\"Willingness to learn OOP concepts\"],\"features\":[\"35+ hours of content\",\"Practical coding exercises\",\"Interview preparation\",\"Lifetime access\",\"Expert instructor support\"]}";
        courseDetailService.createCourseDetail(new CourseDetail(2L, course2Modules, course2Reviews, 4.7, "3.2K", course2RatingDist, course2Overview));
        
        // Course 3: Python Programming
        String course3Modules = "[{\"id\":1,\"title\":\"Python Fundamentals\",\"lessons\":9,\"duration\":\"2.5 hours\",\"topics\":[\"Python Basics\",\"Variables & Types\",\"Operators\",\"Input/Output\"]},{\"id\":2,\"title\":\"Data Structures\",\"lessons\":14,\"duration\":\"5 hours\",\"topics\":[\"Lists & Tuples\",\"Dictionaries\",\"Sets\",\"Comprehensions\"]},{\"id\":3,\"title\":\"File Handling & Modules\",\"lessons\":11,\"duration\":\"4 hours\",\"topics\":[\"File Operations\",\"Exception Handling\",\"Modules\",\"Packages\"]},{\"id\":4,\"title\":\"Advanced Python\",\"lessons\":13,\"duration\":\"5 hours\",\"topics\":[\"Decorators\",\"Generators\",\"Context Managers\",\"Async Programming\"]}]";
        String course3Reviews = "[{\"id\":1,\"name\":\"Ananya Das\",\"avatar\":\"A\",\"rating\":5,\"time\":\"3 Months\",\"comment\":\"Perfect for beginners and intermediate learners. Loved the data science integration!\"},{\"id\":2,\"name\":\"Ravi Verma\",\"avatar\":\"R\",\"rating\":5,\"time\":\"2 Months\",\"comment\":\"Best Python course available. Projects were really engaging.\"},{\"id\":3,\"name\":\"Meera Joshi\",\"avatar\":\"M\",\"rating\":5,\"time\":\"1 Month\",\"comment\":\"Crystal clear explanations. Now I feel confident in Python!\"}]";
        String course3RatingDist = "[{\"stars\":5,\"count\":350,\"percentage\":78},{\"stars\":4,\"count\":80,\"percentage\":18},{\"stars\":3,\"count\":15,\"percentage\":3},{\"stars\":2,\"count\":3,\"percentage\":0.7},{\"stars\":1,\"count\":2,\"percentage\":0.3}]";
        String course3Overview = "{\"learn\":[\"Master Python basics and syntax\",\"Work with data structures and algorithms\",\"Handle files and exceptions\",\"Explore advanced Python topics\",\"Build automation scripts and web apps\",\"Prepare for Python interviews\"],\"prerequisites\":[\"No prior programming required\",\"Basic math skills\",\"Interest in automation and data science\"],\"features\":[\"40+ hours of video\",\"Real-world projects\",\"Downloadable resources\",\"Certificate\",\"Active student community\"]}";
        courseDetailService.createCourseDetail(new CourseDetail(3L, course3Modules, course3Reviews, 4.9, "4.8K", course3RatingDist, course3Overview));
        
        return ResponseEntity.ok("3 course details initialized successfully");
    }
}
