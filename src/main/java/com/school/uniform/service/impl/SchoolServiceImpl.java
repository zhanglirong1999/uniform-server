package com.school.uniform.service.impl;

import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.entity.Product;
import com.school.uniform.model.dao.entity.Purchase;
import com.school.uniform.model.dao.entity.School;
import com.school.uniform.model.dao.entity.Tag;
import com.school.uniform.model.dao.mapper.SchoolMapper;
import com.school.uniform.model.dao.mapper.TagMapper;
import com.school.uniform.model.dto.post.SchoolAdd;
import com.school.uniform.model.dto.post.TagAdd;
import com.school.uniform.service.SchoolService;
import com.school.uniform.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

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


}
