package com.school.uniform.service.impl;

import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.entity.Location;
import com.school.uniform.model.dao.mapper.LocationMapper;
import com.school.uniform.model.dto.post.LocationChange;
import com.school.uniform.model.dto.post.LocationPost;
import com.school.uniform.service.LocationService;
import com.school.uniform.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationMapper locationMapper;

    @Override
    public void addPosition(LocationPost position, String accountId) {
        Location location = new Location();
        location.setAccountId(accountId);
        location.setPosition(position.getPosition());
        location.setLocationId(ConstantUtil.generateId());
        location.setName(position.getName());
        location.setPhone(position.getPhone());
        if(position.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_POSITION);
        }
        if(position.getName().equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_NAME);
        }
        if(position.getPhone().equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_PHONE);
        }
        locationMapper.insert(location);
    }

    @Override
    public void postPosition(LocationChange positionChange, String accountId) {
        Long positionId = positionChange.getPositionId();
        String position = positionChange.getPosition();
        String name = positionChange.getName();
        String phone = positionChange.getPhone();
        Location location = new Location();
        location.setLocationId(positionId);
        if(!position.equals("")){
            location.setPosition(position);
        }
        if(!name.equals("")){
            location.setName(name);
        }
        if(!phone.equals("")){
            location.setPhone(phone);
        }

        String id = locationMapper.selectOneByExample(
                Example.builder(Location.class).where(Sqls.custom().
                        andEqualTo("locationId",positionId))
                        .build()
        ).getAccountId();
        if(!id.equals(accountId)){
            throw new BizException(ConstantUtil.BizExceptionCause.LOW_AUTHORITY);
        }
        locationMapper.updateByExampleSelective(
                location,Example.builder(Location.class).where(Sqls.custom().
                        andEqualTo("locationId",positionId))
                        .build()
        );
    }

    @Override
    public void deletePosition(Long positionId, String accountId) {
        int count = locationMapper.getLocationCount(positionId);
        if(count>0){
            throw new BizException(ConstantUtil.BizExceptionCause.CANT_DELETE_LOCATION);
        }
        Location location = locationMapper.selectOneByExample(
                Example.builder(Location.class).where(Sqls.custom().
                        andEqualTo("locationId",positionId))
                        .build()
        );
        if(!accountId.equals(location.getAccountId())){
            throw new BizException(ConstantUtil.BizExceptionCause.LOW_AUTHORITY);
        }
        locationMapper.deleteByExample(
                Example.builder(Location.class).where(Sqls.custom().
                        andEqualTo("locationId",positionId))
                        .build()
        );
    }

    @Override
    public Object getPositionList(String accountId) {
        Iterator<Location> iterator = locationMapper.selectByExample(
                Example.builder(Location.class).where(Sqls.custom()
                .andEqualTo("accountId",accountId)).build()
        ).iterator();

        LinkedList list = new LinkedList();
        while (iterator.hasNext()){
            Location location = iterator.next();
            Map<String,Object> map = new HashMap<>();
            map.put("positionId",location.getLocationId());
            map.put("position",location.getPosition());
            map.put("name",location.getName());
            map.put("phone",location.getPhone());
            list.add(map);
        }
        return list;

    }
}
