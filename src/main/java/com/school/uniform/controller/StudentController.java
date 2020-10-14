package com.school.uniform.controller;

import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dto.post.PutStudent;
import com.school.uniform.model.dto.post.StudentInfo;
import com.school.uniform.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/student")
@WebResponse
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 新增学生
     * @param studentInfo
     * @return
     */
    @TokenRequired
    @PostMapping("/info")
    public Object addStudent(
        @RequestBody StudentInfo studentInfo
    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        studentService.addStudent(studentInfo,accountId);
        return "添加成功";
    }

    /**
     * 通过id获取学生信息
     * @param studentId
     * @return
     */
    @TokenRequired
    @GetMapping("/info")
    public Object getStudentInfo(
            @RequestParam Long studentId
    ){
        return studentService.getStudent(studentId);
    }

    /**
     * 修改学生信息
     * @param putStudent
     * @return
     */
    @TokenRequired
    @PostMapping("/modify")
    public Object putStudent(
            @RequestBody PutStudent putStudent
            ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        studentService.putStudent(putStudent,accountId);
        return "修改成功";
    }

    @TokenRequired
    @DeleteMapping("/delete")
    public Object deleteStudent(
            @RequestParam Long studentId
    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        studentService.deleteStudent(studentId,accountId);
        return "删除成功";
    }







}
