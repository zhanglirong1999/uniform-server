package com.school.uniform.service.impl;

import com.school.uniform.common.CONST;
import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.entity.Account;
import com.school.uniform.model.dao.entity.School;
import com.school.uniform.model.dao.entity.Student;
import com.school.uniform.model.dao.mapper.SchoolMapper;
import com.school.uniform.model.dao.mapper.StudentMapper;
import com.school.uniform.model.dto.post.PutStudent;
import com.school.uniform.model.dto.post.StudentInfo;
import com.school.uniform.service.StudentService;
import com.school.uniform.util.ConstantUtil;
import com.school.uniform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addStudent(StudentInfo studentInfo,String accountId) {
        Student student = new Student();
        Long studentId = ConstantUtil.generateId();
        student.setAccountId(accountId);
        student.setStudentId(studentId);
        String class1 = studentInfo.getClass1();
        if(studentInfo.getChest()!=null){
        student.setChest(studentInfo.getChest());}

        student.setGender(studentInfo.getGender());
        student.setName(studentInfo.getName());

        if(studentInfo.getHeight()!=null){
        student.setHeight(studentInfo.getHeight());}
        if(studentInfo.getHipline()!=null){
        student.setHipline(studentInfo.getHipline());}
        if(studentInfo.getSchoolNum()!=null){
        student.setSchoolNum(studentInfo.getSchoolNum());}
        if(studentInfo.getWeight()!=null){
        student.setWeight(studentInfo.getWeight());}
        if(studentInfo.getWaistline()!=null){
        student.setWaistline(studentInfo.getWaistline());}
        if(class1!=null){
            student.setClass1(class1);
        }

        Long schoolId = redisUtil.getSchoolId(accountId);  //schoolId
        student.setSchoolId(schoolId);
        studentMapper.insertSelective(student);
    }

    @Override
    public Object getStudent(Long studentId) {
        Student student = studentMapper.selectByPrimaryKey(studentId);
        String name=student.getName();
        Integer gender=student.getGender();
        String schoolnum=student.getSchoolNum();
        Long schoolId=student.getSchoolId();
        System.out.println(schoolId);
        School schoolClass =  schoolMapper.selectOneByExample(
                Example.builder(School.class).where(Sqls.custom().andEqualTo("schoolId",schoolId))
                        .build()
        );
        System.out.println(schoolClass);
        String school = schoolClass.getName();  //schoolId
        String height=student.getHeight();
        String weight=student.getWeight();
        String chest=student.getChest();
        String waistline=student.getWaistline();
        String hipline=student.getHipline();
        Map<String,Object> map = new HashMap<>();
        map.put("studentId",studentId);
        map.put("name",name);
        map.put("gender",gender);
        map.put("schoolNum",schoolnum);
        map.put("schoolId",schoolId);
        map.put("schoolName",school);
        map.put("height",height);
        map.put("chest",chest);
        map.put("weight",weight);
        map.put("waistline",waistline);
        map.put("hipline",hipline);
        map.put("class1",student.getClass1());
        if(student.getGender()==0){
            map.put("avatar", CONST.ManAvatar);
        }else if (student.getGender()==1){
            map.put("avatar",CONST.WomanAvatar);
        }else {
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_GENDER);
        }
        return map;
    }

    @Override
    public void putStudent(PutStudent putStudent, String accountId) {
        Long studentId = putStudent.getStudentId();
        System.out.println(studentId);
        Student student = studentMapper.selectOneByExample(
                Example.builder(Student.class).where(Sqls.custom().andEqualTo("studentId",studentId))
                        .build()
        );
        if(!student.getAccountId().equals(accountId)){
            throw new BizException(ConstantUtil.BizExceptionCause.LOW_AUTHORITY);
        }
        String name=putStudent.getName();
        if(name.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_NAME);
        }
        Integer gender=putStudent.getGender();
        if(gender.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_GENDER);
        }
        String schoolNum=putStudent.getSchoolNum();
        Long schoolId= redisUtil.getSchoolId(accountId);
        String height=putStudent.getHeight();
        String weight=putStudent.getWeight();
        String chest=putStudent.getChest();
        String waistline=putStudent.getWaistline();
        String hipline=putStudent.getHipline();
        String class1=putStudent.getClass1();
        if(name!=null){
            student.setName(name);
        }
        if(gender!=null){
            student.setGender(gender);
        }
        if(schoolNum!=null){
            student.setSchoolNum(schoolNum);
        }
        if(schoolId!=null){
            student.setSchoolId(schoolId);
        }
        if(height!=null){
            student.setHeight(height);
        }
        if(weight!=null){
            student.setWeight(weight);
        }
        if(chest!=null){
            student.setChest(chest);
        }
        if(waistline!=null){
            student.setWaistline(waistline);
        }
        if(hipline!=null){
            student.setHipline(hipline);
        }
        if(class1!=null){
            student.setClass1(class1);
        }
        studentMapper.updateByPrimaryKeySelective(student);
    }

    @Override
    public void deleteStudent(Long studentId,String accountId) {
        Student student = studentMapper.selectOneByExample(
                Example.builder(Student.class).where(Sqls.custom().andEqualTo("studentId",studentId))
                        .build()
        );
        if(!student.getAccountId().equals(accountId)){
            throw new BizException(ConstantUtil.BizExceptionCause.LOW_AUTHORITY);
        }
        studentMapper.delete(student);
    }
}
