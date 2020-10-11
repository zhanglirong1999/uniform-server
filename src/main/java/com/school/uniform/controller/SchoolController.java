package com.school.uniform.controller;

import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dao.entity.School;
import com.school.uniform.model.dto.post.SchoolAdd;
import com.school.uniform.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/school")
@WebResponse
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    /**
     * 新增学校
     * @param schoolAdd
     * @return
     */
    @TokenRequired
    @PostMapping("/add")
    public Object addSchool(
            @RequestBody SchoolAdd schoolAdd
            ){
        schoolService.addSchool(schoolAdd);
        return "添加成功";
    }

    /**
     * 修改学校
     * @param school
     * @return
     */
    @TokenRequired
    @PostMapping("/change")
    public Object putSchool(
            @RequestBody School school
            ){
        schoolService.putSchool(school);
        return "修改成功";
    }

    /**
     * 删除学校（软删除）
     * @param schoolId
     * @return
     */
    @TokenRequired
    @DeleteMapping("/delete")
    public Object deleteSchool(
            @RequestParam Long schoolId
    ){
        schoolService.deleteSchool(schoolId);
        return "删除成功";
    }

    /**
     * 查看学校列表
     * @return
     */
    @TokenRequired
    @GetMapping("/list")
    public Object getSchoolList(){
        return schoolService.getSchoolList();
    }




}
