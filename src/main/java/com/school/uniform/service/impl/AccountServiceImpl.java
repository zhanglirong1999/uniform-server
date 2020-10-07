package com.school.uniform.service.impl;

import com.school.uniform.common.CONST;
import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.entity.Account;
import com.school.uniform.model.dao.entity.Product;
import com.school.uniform.model.dao.entity.Student;
import com.school.uniform.model.dao.mapper.AccountMapper;
import com.school.uniform.model.dao.mapper.SchoolMapper;
import com.school.uniform.model.dao.mapper.StudentMapper;
import com.school.uniform.model.dto.post.Register;
import com.school.uniform.service.AccountService;
import com.school.uniform.util.ConstantUtil;
import com.school.uniform.util.RedisUtil;
import org.bouncycastle.asn1.cms.PasswordRecipientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public void registerUser(Register register,String accountId) {
        Account account = new Account();
        account.setAccountId(accountId);
        if(accountMapper.selectByPrimaryKey(account)==null){
            throw new BizException(ConstantUtil.BizExceptionCause.NOT_USER);
        }
        account.setAvatar(register.getAvatar());
        account.setName(register.getName());
        account.setLastTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public Object getUserInfo(String accountId) {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        if(account==null)
        {
            throw new BizException(ConstantUtil.BizExceptionCause.NOT_USER);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("name",account.getName());
        map.put("avatar",account.getAvatar());
        return map;
    }

    @Override
    public Object getStudentList(String accountId) {
        Long schoolId = redisUtil.getSchoolId(accountId);
        Iterator<Student> iterator =  studentMapper.selectByExample(
                Example.builder(Student.class).where(Sqls.custom().andEqualTo("accountId",accountId).
                        andEqualTo("schoolId",schoolId))
                        .build()
        ).iterator();
        LinkedList list = new LinkedList();
        while (iterator.hasNext()){
            Student student = iterator.next();
            System.out.println(student);
            Map<String,Object> map = new HashMap<>();

            map.put("name",student.getName());
            map.put("gender",student.getGender());
            map.put("class1",student.getClass1());
            if(student.getGender()==0){
                map.put("avatar", CONST.ManAvatar);
            }else if (student.getGender()==1){
                map.put("avatar",CONST.WomanAvatar);
            }else {
                throw new BizException(ConstantUtil.BizExceptionCause.ERROR_GENDER);
            }
            list.add(map);
        }
        return list;
    }


}
