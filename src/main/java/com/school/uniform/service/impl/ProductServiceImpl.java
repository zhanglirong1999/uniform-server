package com.school.uniform.service.impl;



import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.school.uniform.model.dao.entity.*;
import com.school.uniform.model.dao.mapper.*;
import com.school.uniform.model.dto.post.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.school.uniform.common.IdGenerator;
import com.school.uniform.exception.BizException;
import com.school.uniform.service.ProductService;
import com.school.uniform.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.*;

@Service

public class ProductServiceImpl implements ProductService {
    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private ShoppingMapper shoppingMapper;

    @Autowired
    private PurchaseMapMapper purchaseMapMapper;

    @Override
    public Object getProductList() {
        Map<String,Object> map = new HashMap<>();
        Iterator<Purchase> iterator = purchaseMapper.selectByExample(
                Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("state",1).
                        orEqualTo("state","2"))
                        .build()
        ).iterator();
        Double moneySum=0.0;
        Integer orderSum = 0;
        Integer unSendSum=0;
        while (iterator.hasNext()){
            Purchase purchase = iterator.next();
            moneySum +=Double.valueOf( purchase.getTotal());
            orderSum++;
            if(purchase.getState().equals("1")){
                unSendSum++;
            }
        }
        Integer school = schoolMapper.selectCountByExample(
                Example.builder(School.class).where(Sqls.custom().andEqualTo("deleted",0)
        ));
        map.put("money",moneySum);
        map.put("order",orderSum);
        map.put("unshipped",unSendSum);
        map.put("school",school);
        return map;

    }

    @Override
    public void addTag(TagAdd tagAdd) {
        Long tagId = ConstantUtil.generateId();
        String name = tagAdd.getTag();
        if(name.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.NOT_TAG);
        }
        String des = tagAdd.getDescription();
        Tag tag = new Tag();
        tag.setTagId(tagId);
        tag.setName(name);
        tag.setDescription(des);
        tagMapper.insert(tag);
    }

    @Override
    public void deletePro(Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        productMapper.delete(product);
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = new Tag();
        tag.setTagId(tagId);
        tagMapper.delete(tag);
    }

    @Override
    public Object getTag() {
        return tagMapper.selectByExample(
                Example.builder(Tag.class).where(Sqls.custom().andEqualTo("deleted",0))
                        .build()
        );
    }

    @Override
    public Object getSchoolList(Long schoolId) {
        return productMapper.selectByExample(
                Example.builder(Product.class).where(Sqls.custom().andEqualTo("schoolId",schoolId))
                .build()
        );
    }

    @Override
    public void productAdd(ProductAdd productAdd) {
        System.out.println(productAdd);
        Long productId = ConstantUtil.generateId();
        String proName = productAdd.getProductName();
        String description = productAdd.getDescription();
        String freight = productAdd.getFreight();
        String img = "url";
        String tag = productAdd.getTag();
//        List<Detail> elements = productAdd.getDetail();
//        Arrays.stream(elements).forEach(e -> {
//            System.out.println(e.getPrice());
//        });
//        System.out.println(elements);
        JsonArray detail = null;
        if(proName.equals("")||img.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_DETAIL);
        }
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(proName);
        product.setImg(img);
        product.setDescription(description);
        product.setFreight(freight);
        product.setType(tag);
        JSONArray json = JSONArray.fromObject(detail);
        System.out.println(json);
        String price="10000.0";

//        if(json.size()>0){
//            for(int i=0;i<json.size();i++){
//                JSONObject element = json.getJSONObject(i);   // 遍历 jsonarray 数组，把每一个对象转成 json 对象
//                System.out.println(element) ;   // 得到 每个对象中的属性值
//                String price1 = element.getString("price");
//                String size = element.getString("size");
//                Integer sex = (Integer) element.get("sex");
//                if(Double.valueOf(price)>Double.valueOf(price1)){
//                    price = price1;
//                }  //将最低价格作为商品价格保存
//                Long id = ConstantUtil.generateId();
//                if(price.equals("")||size.equals("")||sex==null){
//                    throw new BizException(ConstantUtil.BizExceptionCause.LOSS_DETAIL);
//                }
//                Price productSon = new Price();
//                productSon.setId(id);
//                productSon.setPrice(price);
//                productSon.setSex(sex);
//                priceMapper.insert(productSon);
//            }
//        }else {
//            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_DETAIL);
//        }

        productMapper.insert(product);



    }

    @Override
    public void addShopping(ShoppingPost shopping, String accountId) {
        Long productId = shopping.getProductId();
        Integer count = shopping.getCount();
        String size = shopping.getSize();
        Integer sex = shopping.getSex();
        if(count==null){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_COUNT);
        }
        if(size.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_SIZE);
        }
        if(sex==null){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_SEX);
        }
        Long orderId = ConstantUtil.generateId();
//        Long product_id = productMapper.getPriceId(productId,size,sex);

        if(productId==null){
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_INFOSHOP);
        }

        Shopping shopping1 = new Shopping();
        shopping1.setShopId(orderId);
        shopping1.setProductId(productId);
        shopping1.setSex(sex);
        shopping1.setCount(count);
        shopping1.setSize(size);

        shoppingMapper.insert(shopping1);
    }

    @Override
    public Object productDetail(Long productId) {
        Product product = productMapper.selectOneByExample(
                Example.builder(Product.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        );
        Map<String,Object> map = new HashMap<>();
        map.put("productName",product.getProductName());
        map.put("description",product.getDescription());
        map.put("freight",product.getFreight());
        map.put("img",product.getImg());
        map.put("tag",product.getType());
        Iterator<Price> iterator = priceMapper.selectByExample(
                Example.builder(Price.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        ).iterator();
        LinkedList<Detail> linkedList = new LinkedList<>();
        while (iterator.hasNext()){
            Price price = iterator.next();
            Detail detail = new Detail();
            detail.setPrice(price.getPrice());
            detail.setSex(price.getSex());
            detail.setSize(price.getSize());
            linkedList.add(detail);
        }
        map.put("detail",linkedList);
        return map;
    }

    @Override
    public void purchase(Purchase1 purchaseShop, String accountId) {
        String type = purchaseShop.getType();   //线上线下
        Long positionId = purchaseShop.getPositionId();
        PurchaseShop[] purchaseShops = purchaseShop.getPurchaseShops();
        Long orderId = ConstantUtil.generateId();
        String statePur = "0";   //未支付状态
        String form = purchaseShop.getForm();   //征订/补丁
        if(positionId==null){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_POSITION);
        }
        Double freight=0.0;
        Double totalPrice=0.0;
        for(int i=0;i<purchaseShops.length;i++) {
            Long productId = purchaseShops[i].getProductId();
            Integer count = purchaseShops[i].getCount();
            String size = purchaseShops[i].getSize();
            Integer sex = purchaseShops[i].getSex();
            if (count == null) {
                throw new BizException(ConstantUtil.BizExceptionCause.LOSS_COUNT);
            }
            if (size.equals("")) {
                throw new BizException(ConstantUtil.BizExceptionCause.LOSS_SIZE);
            }
            if (sex == null) {
                throw new BizException(ConstantUtil.BizExceptionCause.LOSS_SEX);
            }

            PurchaseMap purchaseMap = new PurchaseMap();
            purchaseMap.setPurId(ConstantUtil.generateId());
            purchaseMap.setCount(count);
            purchaseMap.setProductId(productId);
            purchaseMap.setSize(size);
            Long priceId = productMapper.getPriceId(productId,size,sex);
            String price = priceMapper.selectOneByExample(
                    Example.builder(Price.class).where(Sqls.custom().andEqualTo("priceId",priceId))
                            .build()
            ).getPrice();
            purchaseMap.setPrice(price);
            if(Double.valueOf(productMapper.getFreight(productId))>freight) {    //运费
                freight = Double.valueOf(productMapper.getFreight(productId));
            }
            totalPrice += Double.valueOf(price);
            purchaseMapMapper.insert(purchaseMap);

            if(purchaseShop.getState().equals("1")) {
                //表示从购物车购买,删除购物车这条数据信息
                shoppingMapper.deleteByExample(
                        Example.builder(Shopping.class).where(Sqls.custom().andEqualTo("productId",productId).
                                andEqualTo("size",size).andEqualTo("accountId",accountId)
                        )
                                .build()
                );

            }

        }
        Purchase purchase = new Purchase();
        purchase.setOrderId(orderId);
        purchase.setState(statePur);
        purchase.setType(type);
        purchase.setAccountId(accountId);
        purchase.setPositionId(positionId);
        purchase.setForm(form);
        purchase.setTotal(String.valueOf(totalPrice+freight));
        purchaseMapper.insert(purchase);

    }

    @Override
    public Object getCartList(String accountId) {
        Iterator<Shopping> iterator = shoppingMapper.selectByExample(
                Example.builder(Shopping.class).where(Sqls.custom().andEqualTo("accountId",accountId)
                        ).orderByDesc("addTime")
                        .build()
        ).iterator();
        LinkedList list = new LinkedList();

        while (iterator.hasNext()){
            Shopping shopping = iterator.next();
            Map<String,Object> map = new HashMap<>();
            Long productId = shopping.getProductId();
            Integer count =shopping.getCount();
            String size = shopping.getSize();
            Integer sex = shopping.getSex();
            Price price = priceMapper.selectOneByExample(
                    Example.builder(Price.class).where(Sqls.custom().andEqualTo("productId",productId)
                    .andEqualTo("size",size).andEqualTo("sex",sex))
                            .build()
            );
            String pricing = price.getPrice();
            map.put("productId",productId);
            map.put("price",pricing);
            map.put("size",size);
            map.put("sex",sex);
            map.put("count",count);
            Product product = productMapper.selectOneByExample(
                    Example.builder(Product.class).where(Sqls.custom().andEqualTo("productId",productId))
                            .build()
            );
            String img = product.getImg();
            String productName = product.getProductName();
            map.put("img",img);
            map.put("productName",productName);
            list.add(map);
        }
        return list;
    }

    @Override
    public Object getPurchaseList(String accountId) {
        Iterator<Purchase> iterator = purchaseMapper.selectByExample(
                Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("accountId",accountId)
                        .andEqualTo("state","1").orEqualTo("state","2"))
                        .build()
        ).iterator();
        LinkedList list = new LinkedList();
        while (iterator.hasNext()){
            Purchase purchase = iterator.next();
            System.out.println(purchase);
            Map<String,Object> map = new HashMap<>();
            Long orderId = purchase.getOrderId();

            Iterator<PurchaseMap> iteratorMap = purchaseMapMapper.selectByExample(
                    Example.builder(PurchaseMap.class).where(Sqls.custom().andEqualTo("purId",orderId))
                            .build()
            ).iterator();
            while (iteratorMap.hasNext()){
                PurchaseMap purchaseMap = iteratorMap.next();
                Long productId = purchaseMap.getProductId();
                String pricing = purchaseMap.getPrice();
                String size = purchaseMap.getSize();
                Product product = productMapper.selectOneByExample(
                        Example.builder(Product.class).where(Sqls.custom().andEqualTo("productId",productId))
                                .build()
                );
                String img = product.getImg();
                String productName = product.getProductName();
                String number=purchase.getNumber();
                Integer sex = purchaseMap.getSex();
                Integer count = purchaseMap.getCount();
                map.put("productId",productId);
                map.put("price",pricing);
                map.put("size",size);
                map.put("sex",sex);
                map.put("count",count);
                map.put("img",img);
                map.put("productName",productName);
                map.put("number",number);
                list.add(map);
            }

        }
        return list;

    }

    @Override
    public Object searchProduct(String index) {
        return productMapper.search(index);
    }

    @Override
    public Object searchByTagAndSchool(String[] school, String[] tag) {
//        for (int i=0;i<school.length;i++){
//
//        }
        return 1;
    }

    @Override
    public void sendProduct(Send send) {
        Long orderId = send.getOrderId();
        String number = send.getNumber();
        Purchase purchase = purchaseMapper.selectOneByExample(
                Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("orderId",orderId))
                        .build());
        if(purchase.getState().equals("2")){
            throw new BizException(ConstantUtil.BizExceptionCause.HAVE_SEND);
        }
        if(purchase.getState().equals("0")){
            throw new BizException(ConstantUtil.BizExceptionCause.IS_SHOPPING);
        }
        purchase.setState("2");
        purchase.setNumber(number) ;
        purchaseMapper.updateByExampleSelective(
                purchase,Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("orderId",orderId))
                        .build()
        );
    }

    @Override
    public void addSolicit(AddSolicite addSolicite) {
        Long schoolId = addSolicite.getSchoolId();
        String description = addSolicite.getDescription();
        Long[] productIds = addSolicite.getProductId();

    }
}
