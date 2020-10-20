package com.school.uniform.service.impl;

import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.entity.Product;
import com.school.uniform.model.dao.entity.Purchase;
import com.school.uniform.model.dao.entity.Solicitation;
import com.school.uniform.model.dao.entity.SoliciteMap;
import com.school.uniform.model.dao.mapper.ProductMapper;
import com.school.uniform.model.dao.mapper.SchoolMapper;
import com.school.uniform.model.dao.mapper.SolicitationMapper;
import com.school.uniform.model.dao.mapper.SolicitemapMapper;
import com.school.uniform.model.dto.post.AddSolicite;
import com.school.uniform.model.dto.post.PostSolicit;
import com.school.uniform.service.SolicitaionService;
import com.school.uniform.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.*;

@Service
public class SolicitaionServiceImpl implements SolicitaionService {
    @Autowired
    private SolicitationMapper solicitationMapper;

    @Autowired
    private SolicitemapMapper solicitemapMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void addSolicit(AddSolicite solicit) {
        Long schoolId = solicit.getSchoolId();
        String description = solicit.getDescription();
        Long[] productIds = solicit.getProductId();
        Long sid = ConstantUtil.generateId();
        Integer[] count = solicit.getCount();
        String type = solicit.getType();
        if(count==null){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_COUNT);
        }
        Solicitation solicitation = new Solicitation();
        solicitation.setDescription(description);
        solicitation.setSchoolId(schoolId);
        solicitation.setSid(sid);
        solicitation.setType(type);
        solicitationMapper.insert(solicitation);
        if(productIds.length!=count.length){
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_NUM);
        }
        for (int i=0;i<productIds.length;i++){
            Long productId =productIds[i];
            Integer counting =  count[i];
            SoliciteMap soliciteMap = new SoliciteMap();
            soliciteMap.setProductId(productId);
            soliciteMap.setSolicitId(sid);
            soliciteMap.setCount(counting);
            solicitemapMapper.insert(soliciteMap);
        }

    }

    @Override
    public void deleteSolicit(Long sid) {
        System.out.println("hh");
        Solicitation solicitation = solicitationMapper.selectOneByExample(
                Example.builder(Solicitation.class).where(Sqls.custom().andEqualTo("sid",sid))
                        .build()
        );
        if (solicitation==null){
            throw new BizException(ConstantUtil.BizExceptionCause.NO_SOLICIT);
        }

        solicitationMapper.deleteByExample(
                Example.builder(Solicitation.class).where(Sqls.custom().andEqualTo("sid",sid))
                        .build()
        );

        solicitemapMapper.deleteByExample(
                Example.builder(SoliciteMap.class).where(Sqls.custom().andEqualTo("solicitId",sid))
                        .build()
        );
    }

    @Override
    public void postSolicit(PostSolicit solicit) {
        Long sid = solicit.getSid();
        Solicitation solicitation_ = solicitationMapper.selectOneByExample(
                Example.builder(Solicitation.class).where(Sqls.custom().andEqualTo("sid",sid))
                        .build()
        );
        if (solicitation_==null){
            throw new BizException(ConstantUtil.BizExceptionCause.NO_SOLICIT);
        }
        String des = solicit.getDescription();
        Long[] productIds= solicit.getProductId();
        Integer[] count = solicit.getCount();
        Long schoolId = solicit.getSchoolId();
        String type = solicit.getType();
        if (!des.equals("")||schoolId!=null){
            Solicitation solicitation = new Solicitation();
            solicitation.setSid(sid);
            if(!des.equals("")){
                solicitation.setDescription(des);
            }
            if ((schoolId!=null)){
                solicitation.setSchoolId(schoolId);
            }
            if(!type.equals("")){
                solicitation.setType(type);
            }
            solicitationMapper.updateByExampleSelective(
                    solicitation,Example.builder(Solicitation.class).where(Sqls.custom().andEqualTo("sid",sid))
                            .build()
            );

        }
        if(productIds.length!=count.length){
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_NUM);
        }
        if (productIds.length>0){
            solicitemapMapper.deleteByExample(
                    Example.builder(SoliciteMap.class).where(Sqls.custom().andEqualTo("solicitId",sid))
                            .build()
            );
            for (int i=0;i<productIds.length;i++){
                Long productId =productIds[i];
                Integer counting = count[i];
                SoliciteMap soliciteMap = new SoliciteMap();
                soliciteMap.setProductId(productId);
                soliciteMap.setSolicitId(sid);
                soliciteMap.setCount(counting);
                solicitemapMapper.insert(soliciteMap);
            }
        }
    }

    @Override
    public Object getList() {
        Iterator<Solicitation> iterator = solicitationMapper.selectAll().iterator();
        List list = new LinkedList();
        while (iterator.hasNext()){
            Map<String,Object> map = new HashMap<>();
            Solicitation solicitation = iterator.next();
            Long sid = solicitation.getSid();
            String school = schoolMapper.getSchoolName(solicitation.getSchoolId());
            String description = solicitation.getDescription();
            map.put("sid",sid);
            map.put("school",school);
            map.put("description",description);
            map.put("schoolId",solicitation.getSchoolId());
            map.put("type",solicitation.getType());
            map.put("product",getProduct(sid));
            list.add(map);
        }
        return list;
    }

    @Override
    public List getProduct(Long sid) {
        Iterator<SoliciteMap> iterator = solicitemapMapper.selectByExample(
                Example.builder(SoliciteMap.class).where(Sqls.custom().andEqualTo("solicitId",sid))
                        .build()
        ).iterator();
        List list = new LinkedList();
        while (iterator.hasNext()){
            SoliciteMap soliciteMap =iterator.next();
            Long productId = soliciteMap.getProductId();
            Product product = productMapper.selectOneByExample(
                    Example.builder(Product.class).where(Sqls.custom().andEqualTo("productId",productId))
                            .build()
            );
            if(product==null){
                continue;
            }else {
                Map<String, Object> map = new HashMap<>();
                map.put("productId", product.getProductId());
                map.put("productName", product.getProductName());
                map.put("type", product.getType());
                map.put("img", product.getImg());
                map.put("price", product.getPrice());
                map.put("freight", product.getFreight());
                map.put("description", product.getDescription());
                map.put("count", soliciteMap.getCount());
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public Object getUserSolicit(Long sids) {
        Solicitation solicitation = solicitationMapper.selectOneByExample(
                Example.builder(Solicitation.class).where(Sqls.custom().andEqualTo("sid",sids))
                        .build()
        );
        Iterator<SoliciteMap> iterator = solicitemapMapper.selectByExample(
                Example.builder(SoliciteMap.class).where(Sqls.custom().andEqualTo("solicitId",sids))
                        .build()
        ).iterator();
        Map<String,Object> maps = new HashMap<>();
        maps.put("sid",sids);
        maps.put("school",schoolMapper.getSchoolName(solicitation.getSchoolId()));
        maps.put("description",solicitation.getDescription());
        List list = new LinkedList();
        while (iterator.hasNext()){
            Map<String,Object> map = new HashMap<>();
            SoliciteMap soliciteMap = iterator.next();
            Product product = productMapper.selectOneByExample(
                    Example.builder(Product.class).where(Sqls.custom().andEqualTo("productId",soliciteMap.getProductId()))
                            .build()
            );
            Map<String,Object> maping = new HashMap<>();
            maping.put("productId",product.getProductId());
            maping.put("productName",product.getProductName());
            maping.put("type",product.getType());
            maping.put("img",product.getImg());
            maping.put("price",product.getPrice());
            maping.put("freight",product.getFreight());
            maping.put("description",product.getDescription());
            maping.put("count",soliciteMap.getCount());
            maping.put("sex",product.getSex());
            list.add(maping);
        }
        maps.put("product",list);
        return maps;
    }
}
