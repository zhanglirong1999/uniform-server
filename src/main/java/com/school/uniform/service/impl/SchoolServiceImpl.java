package com.school.uniform.service.impl;

import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.entity.*;
import com.school.uniform.model.dao.mapper.SchoolMapper;
import com.school.uniform.model.dao.mapper.SclassMapper;
import com.school.uniform.model.dao.mapper.StudentMapper;
import com.school.uniform.model.dao.mapper.TagMapper;
import com.school.uniform.model.dto.post.AddClass;
import com.school.uniform.model.dto.post.SchoolAdd;
import com.school.uniform.model.dto.post.TagAdd;
import com.school.uniform.service.SchoolService;
import com.school.uniform.util.ConstantUtil;
import com.school.uniform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.*;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SclassMapper sclassMapper;

    @Override
    public void addSchool(SchoolAdd schoolAdd) {
        Long schoolId = ConstantUtil.generateId();
        String name = schoolAdd.getName();
        String des = schoolAdd.getDescription();
        if(name.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_SCHOOLNAME);
        }
        School school = new School();
        school.setName(name);
        school.setDescription(des);
        school.setSchoolId(schoolId);
        schoolMapper.insert(school);
    }

    @Override
    public void putSchool(School school) {
        School school1=new School();
        Long schoolId = school.getSchoolId();
        school1.setSchoolId(schoolId);
        String name = school.getName();
        String des = school.getDescription();
        if(name!=null){
            school1.setName(name);
        }
        if(des!=null){
            school1.setDescription(des);
        }
        Example example = new Example(School.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("schoolId", schoolId);
        schoolMapper.updateByExampleSelective(school1,example);
    }

    @Override
    public void deleteSchool(Long schoolId) {
        School school = new School();
        school.setSchoolId(schoolId);
        schoolMapper.delete(school);
    }

    @Override
    public Object getSchoolList() {
        return schoolMapper.selectByExample(
                Example.builder(School.class).where(Sqls.custom().andEqualTo("deleted",0))
                        .build()
        );
    }

    @Override
    public Object getUserStudent(Long schoolId, String accountId) {
        Map<String,Object> map = new HashMap<>();
        map.put("schoolId",schoolId);
        School school = schoolMapper.selectOneByExample(
                Example.builder(School.class).where(Sqls.custom().andEqualTo("schoolId",schoolId))
                        .build()
        );
        String des = school.getDescription();
        String name = school.getName();
        map.put("name",name);
        map.put("description",des);
        Iterator<Student> iterator = studentMapper.selectByExample(
                Example.builder(Student.class).where(Sqls.custom().andEqualTo("schoolId",schoolId)
                .andEqualTo("accountId",accountId))
                        .build()
        ).iterator();
        List<Long> studentIds = new LinkedList<>();
        while (iterator.hasNext()){
            Student student = iterator.next();
            studentIds.add(student.getStudentId());
        }
        map.put("studentId",studentIds);
        return map;
    }

    @Override
    public Object getSchoolClass(String accountId) {
        Long schoolId = redisUtil.getSchoolId(accountId);
        return sclassMapper.getSchoolClass(schoolId);
    }

    @Override
    public void addClass(AddClass addClass) {
        Long schoolId = addClass.getSchoolId();
        String[] grades = addClass.getGrade();
        String[] classes = addClass.getClasses();
        if (grades.length!=classes.length){
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_LENGTH);
        }
        for(int i=0;i<grades.length;i++){
            Sclass sclass = new Sclass();
            sclass.setSchoolId(schoolId);
            sclass.setClasses(classes[i]);
            sclass.setGrade(grades[i]);
            sclassMapper.insert(sclass);
        }
    }

    @Override
    public Object getSchoolClass2(Long schoolId) {
        return schoolMapper.getClass(schoolId);
    }


}
