package com.example.cdaxVideo.Service;

import com.example.cdaxVideo.Entity.*;
import com.example.cdaxVideo.Entity.Module;
import com.example.cdaxVideo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CourseService {

    @Autowired private CourseRepository courseRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private VideoRepository videoRepository;
    @Autowired private AssessmentRepository assessmentRepository;
    @Autowired private QuestionRepository questionRepository;

    // ----- COURSE -----
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCoursesWithModulesAndVideos() {
        List<Course> courses = courseRepository.findAllWithModules();
        for (Course course : courses) {
            for (Module module : course.getModules()) {
                module.setVideos(videoRepository.findByModuleId(module.getId()));
            }
        }
        return courses;
    }

    public Optional<Course> getCourseByIdWithModulesAndVideos(Long id) {
        Optional<Course> optionalCourse = courseRepository.findByIdWithModules(id);
        optionalCourse.ifPresent(course -> {
            for (Module module : course.getModules()) {
                module.setVideos(videoRepository.findByModuleId(module.getId()));
            }
        });
        return optionalCourse;
    }

    // ----- MODULE -----
    public Module saveModule(Long courseId, Module module) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid courseId"));
        module.setCourse(course);
        return moduleRepository.save(module);
    }

    public List<Module> getModulesByCourseId(Long courseId) {
        return moduleRepository.findByCourseId(courseId);
    }

    public Optional<Module> getModuleById(Long id) {
        return moduleRepository.findById(id);
    }

    // ----- VIDEO -----
    public Video saveVideo(Long moduleId, Video video) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid moduleId"));
        video.setModule(module);
        return videoRepository.save(video);
    }

    public List<Video> getVideosByModuleId(Long moduleId) {
        return videoRepository.findByModuleId(moduleId);
    }

    // ----- ASSESSMENT -----
    public Assessment saveAssessment(Long moduleId, Assessment assessment) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid moduleId"));
        assessment.setModule(module);
        return assessmentRepository.save(assessment);
    }

    public List<Assessment> getAssessmentsByModuleId(Long moduleId) {
        return assessmentRepository.findByModuleId(moduleId);
    }

    // ----- QUESTION -----
    public Question saveQuestion(Long assessmentId, Question question) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assessmentId"));
        question.setAssessment(assessment);
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsByAssessmentId(Long assessmentId) {
        return questionRepository.findByAssessmentId(assessmentId);
    }
}
