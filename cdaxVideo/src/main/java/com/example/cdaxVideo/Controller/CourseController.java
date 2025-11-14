package com.example.cdaxVideo.Controller;

import com.example.cdaxVideo.Entity.*;
import com.example.cdaxVideo.Entity.Module;
import com.example.cdaxVideo.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // ---------------------- COURSE APIs ----------------------
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course saved = courseService.saveCourse(course);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> getCourses() {
        List<Course> list = courseService.getAllCoursesWithModulesAndVideos();
        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> getCourse(@PathVariable Long id) {
        return courseService.getCourseByIdWithModulesAndVideos(id)
                .map(course -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("data", course);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------- MODULE APIs ----------------------
    @PostMapping("/modules")
    public ResponseEntity<?> addModule(@RequestParam("courseId") Long courseId, @RequestBody Module module) {
        try {
            Module saved = courseService.saveModule(courseId, module);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/modules/course/{courseId}")
    public ResponseEntity<List<Module>> getModulesByCourse(@PathVariable Long courseId) {
        List<Module> list = courseService.getModulesByCourseId(courseId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/modules/{id}")
    public ResponseEntity<?> getModule(@PathVariable Long id) {
        return courseService.getModuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------- VIDEO APIs ----------------------
    @PostMapping("/videos")
    public ResponseEntity<?> addVideo(@RequestParam("moduleId") Long moduleId, @RequestBody Video video) {
        try {
            Video saved = courseService.saveVideo(moduleId, video);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/modules/{moduleId}/videos")
    public ResponseEntity<List<Video>> getVideosByModule(@PathVariable Long moduleId) {
        List<Video> list = courseService.getVideosByModuleId(moduleId);
        return ResponseEntity.ok(list);
    }

    // ---------------------- ASSESSMENT APIs ----------------------
    @PostMapping("/assessments")
    public ResponseEntity<?> addAssessment(@RequestParam("moduleId") Long moduleId, @RequestBody Assessment assessment) {
        try {
            Assessment saved = courseService.saveAssessment(moduleId, assessment);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/modules/{moduleId}/assessments")
    public ResponseEntity<List<Assessment>> getAssessmentsByModule(@PathVariable Long moduleId) {
        List<Assessment> list = courseService.getAssessmentsByModuleId(moduleId);
        return ResponseEntity.ok(list);
    }


    // ---------------------- QUESTION APIs ----------------------
    @PostMapping("/questions")
    public ResponseEntity<?> addQuestion(@RequestParam("assessmentId") Long assessmentId, @RequestBody Question question) {
        try {
            Question saved = courseService.saveQuestion(assessmentId, question);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



//    @GetMapping("/assessments/{assessmentId}/questions")
//    public ResponseEntity<List<Question>> getQuestionsByAssessment(@PathVariable Long assessmentId) {
//        List<Question> list = courseService.getQuestionsByAssessmentId(assessmentId);
//        return ResponseEntity.ok(list);
//    }

    @GetMapping("/assessments/{assessmentId}/questions")
    public ResponseEntity<Map<String, Object>> getQuestionsByAssessment(@PathVariable Long assessmentId) {
        List<Question> list = courseService.getQuestionsByAssessmentId(assessmentId);

        Map<String, Object> response = new HashMap<>();
        response.put("assessmentId", assessmentId);
        response.put("questions", list);

        return ResponseEntity.ok(response);
    }

}
