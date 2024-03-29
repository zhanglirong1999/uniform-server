package com.school.uniform.service.impl;



import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonArray;
import com.school.uniform.model.dao.entity.*;
import com.school.uniform.model.dao.mapper.*;
import com.school.uniform.model.dto.PageResult;
import com.school.uniform.model.dto.post.*;
import com.school.uniform.service.QCloudFileManager;
import com.school.uniform.util.RedisUtil;
import com.squareup.okhttp.internal.Internal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.school.uniform.common.IdGenerator;
import com.school.uniform.exception.BizException;
import com.school.uniform.service.ProductService;
import com.school.uniform.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.xml.crypto.Data;
import java.io.IOException;
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

    @Autowired
    private QCloudFileManager qCloudFileManager;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Object getProductList() {
        Map<String,Object> map = new HashMap<>();
        Iterator<Purchase> iterator = purchaseMapper.selectByExample(
                Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("state","1").
                        orEqualTo("state","2"))
                        .build()
        ).iterator();
        Double moneySum=0.0;
        Integer orderSum = 0;
        Integer unSendSum=0;
        while (iterator.hasNext()){
            Purchase purchase = iterator.next();
            System.out.println( purchase);
            moneySum +=Double.valueOf( purchase.getTotal());
            orderSum++;
            if(purchase.getState().equals("1")){
                unSendSum++;
            }
        }

        Integer school = schoolMapper.getSchoolSum();
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
        purchaseMapMapper.deleteByExample(
                Example.builder(PurchaseMap.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        );
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
        if(schoolId==-1){
            return productMapper.selectByExample(
                    Example.builder(Product.class).where(Sqls.custom().andEqualTo("deleted", 0))
                            .build()
            );
        }else {
            return productMapper.selectByExample(
                    Example.builder(Product.class).where(Sqls.custom().andEqualTo("schoolId", schoolId))
                            .build()
            );
        }
    }

    @Override
    public void productAdd(ProductAdd productAdd) {
        System.out.println(productAdd);
        Long productId = ConstantUtil.generateId();
        String proName = productAdd.getProductName();
        String description = productAdd.getDescription();
        String freight = productAdd.getFreight();
        Integer sex = productAdd.getSex();
        Long schoolId = productAdd.getSchoolId();
        String img=null;
        String img2=null,img3=null,img4=null;
        if(productAdd.getImg()!=null) {
             img = uploadFile(productAdd.getImg());  //上传图片并且生成url
        }
        if(productAdd.getImg2()!=null){
            img2 = uploadFile(productAdd.getImg2());  //上传图片并且生成url
        }
        if(productAdd.getImg3()!=null){
            img3 = uploadFile(productAdd.getImg3());  //上传图片并且生成url
        }
        if(productAdd.getImg4()!=null){
            img4 = uploadFile(productAdd.getImg4());  //上传图片并且生成url
        }
        String tag = productAdd.getTag();
        String file=null;
        if(productAdd.getFile()!=null) {
            file = uploadFile(productAdd.getFile());
            File file1 = new File();
            file1.setProductId(productId);
            file1.setUrl(file);
            fileMapper.insert(file1);
        }
        String[] sizes = productAdd.getSize();
        String[] prices = productAdd.getPrice();
        Integer[] counts = productAdd.getCount();

        if(proName.equals("")||img.equals("")){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_DETAIL);
        }
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(proName);
        product.setImg(img);
        product.setImg2(img2);
        product.setImg3(img3);
        product.setImg4(img4);
        product.setDescription(description);
        product.setFreight(freight);
        product.setType(tag);
        product.setSex(sex);
        product.setSchoolId(schoolId);
        String price="10000.0";
        if(sizes.length!=prices.length||sizes.length!=counts.length){
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_SIZEANDPRICE);
        }

        if(sizes.length!=0){
            for(int i=0;i<sizes.length;i++){
                String price1 = prices[i];
                String size = sizes[i];
                Integer count = counts[i];
                if(Double.valueOf(price)>Double.valueOf(price1)){
                    price = price1;
                }  //将最低价格作为商品价格保存
                Long id = ConstantUtil.generateId();
                if(price1.equals("")||size.equals("")){
                    throw new BizException(ConstantUtil.BizExceptionCause.LOSS_DETAIL);
                }
                Price productSon = new Price();
                productSon.setId(id);
                productSon.setPrice(price1);
                productSon.setSize(size);
                productSon.setCount(count);
                productSon.setProductId(productId);
                priceMapper.insert(productSon);
            }
        }else {
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_DETAIL);
        }
        product.setPrice(price);

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
        shopping1.setAccountId(accountId);

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
        map.put("img2",product.getImg2());
        map.put("img3",product.getImg3());
        map.put("img4",product.getImg4());

        map.put("tag",product.getType());
        map.put("sex",product.getSex());
        map.put("schoolId",product.getSchoolId());
        map.put("schoolName",schoolMapper.getSchoolName(product.getSchoolId()));
        Iterator<Price> iterator = priceMapper.selectByExample(
                Example.builder(Price.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        ).iterator();
        LinkedList<Detail> linkedList = new LinkedList<>();
        while (iterator.hasNext()){
            Price price = iterator.next();
            Detail detail = new Detail();
            detail.setPrice(price.getPrice());
            detail.setSize(price.getSize());
            detail.setCount(price.getCount());
            linkedList.add(detail);
        }
        map.put("detail",linkedList);
        return map;
    }

    @Override
    public Object purchase(Purchase1 purchaseShop, String accountId) {
        Long studentId = redisUtil.getStudentId(accountId);
        System.out.println(purchaseShop);
        String type = purchaseShop.getType();   //线上线下
        if(type==null){
            type="0";
        }
        Long positionId = purchaseShop.getPositionId();
        PurchaseShop[] purchaseShops = purchaseShop.getPurchaseShops();
        Long orderId = ConstantUtil.generateId();
        String statePur = "0";   //未支付状态
        String form = purchaseShop.getForm();   //征订/补丁
        Long schoolId = redisUtil.getSchoolId(accountId);
        if(positionId==null){
            throw new BizException(ConstantUtil.BizExceptionCause.LOSS_POSITION);
        }
        Double freight=0.0;
        Double totalPrice=0.0;
        List<Long> priceIds = new LinkedList<>();
        List<Integer> nums = new LinkedList<>();
        for(int i=0;i<purchaseShops.length;i++) {
            Long productId = purchaseShops[i].getProductId();
            Integer count = purchaseShops[i].getCount();
            String size = purchaseShops[i].getSize();
            Integer sex = purchaseShops[i].getSex();
            Price product = productMapper.getProductByIdAndSize(productId,size);
            Integer nowCount = product.getCount();
            if(nowCount<count){
                throw new BizException(ConstantUtil.BizExceptionCause.ERROR_COUNT);
            }
            if (count == null) {
                throw new BizException(ConstantUtil.BizExceptionCause.LOSS_COUNT);
            }
            if (size.equals("")) {
                throw new BizException(ConstantUtil.BizExceptionCause.LOSS_SIZE);
            }
            if (sex == null) {
                throw new BizException(ConstantUtil.BizExceptionCause.LOSS_SEX);
            }
            if(form.equals("0")){
                Integer buyCount = productMapper.ifHasPurchase(accountId,productId);
                if(buyCount>0){
                    throw new BizException(ConstantUtil.BizExceptionCause.HAS_BUY);
                }
            }

            PurchaseMap purchaseMap = new PurchaseMap();
            purchaseMap.setPurId(orderId);
            purchaseMap.setCount(count);
            purchaseMap.setProductId(productId);
            purchaseMap.setSize(size);
            purchaseMap.setSex(sex);
            Long priceId = productMapper.getPriceId(productId,size);
//            priceMapper.updateCount(priceId,count);
            String price = priceMapper.selectOneByExample(
                    Example.builder(Price.class).where(Sqls.custom().andEqualTo("id",priceId))
                            .build()
            ).getPrice();
            purchaseMap.setPrice(price);
            //if(Double.valueOf(productMapper.getFreight(productId))>freight) {    //运费
                freight += Double.valueOf(productMapper.getFreight(productId));
           // }
            totalPrice += Double.valueOf(price)*count;
            purchaseMapMapper.insert(purchaseMap);
            priceIds.add(priceId);   //加入priceId
            nums.add(count);  //加入count
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
        redisUtil.setPriceIds(orderId,priceIds,nums);   //priceIds放入redis

        Purchase purchase = new Purchase();
        purchase.setOrderId(orderId);
        purchase.setState(statePur);
        purchase.setType(type);
        purchase.setAccountId(accountId);
        purchase.setPositionId(positionId);
        purchase.setForm(form);
        Map<String,Object> map= new HashMap<>();
        map.put("orderId",orderId);
        if(type.equals("0")) {
            purchase.setTotal(String.valueOf(totalPrice + freight));
            map.put("price",String.valueOf(totalPrice+freight));
        }else {
            purchase.setTotal(String.valueOf(totalPrice));
            map.put("price",String.valueOf(totalPrice));
        }
        purchase.setSchoolId(schoolId);
        purchase.setStudentId(studentId);
        purchaseMapper.insert(purchase);

        return map;


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
                    .andEqualTo("size",size))
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
    public Object getPurchaseList(String accountId,String type) {
        Iterator<Purchase> iterator = purchaseMapper.selectByExample(
                Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("accountId",accountId)
                        .andEqualTo("state",type))
                        .build()
        ).iterator();
        LinkedList list = new LinkedList();
        while (iterator.hasNext()){
            Purchase purchase = iterator.next();
            System.out.println(purchase);
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
                Map<String,Object> map = new HashMap<>();
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

    @Override
    public String uploadFile(MultipartFile file) {
        // 首先获取 newName
        String newNameWithoutType = String.valueOf(ConstantUtil.generateId());
        String newNameWithType = this.qCloudFileManager.buildNewFileNameWithType(
                file, newNameWithoutType
        );
        String ansUrl = null;
        try {
            ansUrl = qCloudFileManager.uploadOneFile(
                    file,
                    newNameWithoutType
            );
        } catch (IOException e) {
            return "上传文件失败";
        }
        // 要删除文件
        ConstantUtil.deleteFileUnderProjectDir(newNameWithType);
        // 返回最终结果
        return ansUrl;
    }

    @Override
    public String getFileUrl(Long productId) {
        if(fileMapper.selectCountByExample(
                Example.builder(File.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        )==0){
            return "暂无质检文件";
        }
        String fileUrl =fileMapper.selectOneByExample(
                Example.builder(File.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        ).getUrl();
        return fileUrl;

    }

    @Override
    public Object getSearchBy(Long schoolId, String state, String type
            ,Integer pageSize,Integer pageIndex) {
        Example example = new Example(Purchase.class);
        if(state.equals("0")){
            if (schoolId != 0){
                System.out.println("1");
                example.createCriteria().andEqualTo("schoolId", schoolId).
                        andEqualTo("form", type).andNotEqualTo("state","0");
            }else {
                System.out.println("?");
                example.createCriteria().
                        andEqualTo("form", type)
                        .andNotEqualTo("state","0");
            }
        }else {
            if (schoolId != 0) {
                System.out.println("2");
                example.createCriteria().andEqualTo("schoolId", schoolId).
                        andEqualTo("form", type)
                        .andEqualTo("state", state);
            }  else {
                System.out.println("3");
                example.createCriteria().andEqualTo("state", state)
                        .andEqualTo("form", type);
            }
        }
        List list = new LinkedList();
        PageHelper.startPage(pageIndex, pageSize);   //分页
        Iterator<Purchase> iterator = purchaseMapper.selectByExample(example).iterator();
        while (iterator.hasNext()){
            Map<String,Object> map= new HashMap<>();
            Purchase purchase = iterator.next();
                    Long orderId =purchase.getOrderId();
            String stating = purchase.getState();
            String number =purchase.getNumber();
            String accountId = purchase.getAccountId();
            String typing = purchase.getType();
            Date buyTime = purchase.getBuyTime();
            String form = purchase.getForm();
            Long positionId = purchase.getPositionId();
            String total = purchase.getTotal();
            Long schoolID = purchase.getSchoolId();
            Long studentId = purchase.getStudentId();
            String class1 = studentMapper.getStudentClass(studentId);
            String name = studentMapper.getStudentName(studentId);
            Student student = studentMapper.getStudent(studentId);
            String height = student.getHeight();
            String weight = student.getWeight();
            String phone = student.getPhone();
            Integer gender = student.getGender();
            map.put("orderId",orderId);
            map.put("state",stating);
            map.put("number",number);
            map.put("type",typing);
            map.put("buyTime",buyTime);
            map.put("form",form);
            map.put("price",total);
            map.put("school",schoolMapper.getSchoolName(schoolID));
            System.out.println(purchase);
            Location location = locationMapper.selectByPrimaryKey(positionId);
            map.put("position",location.getPosition());
            map.put("phone",location.getPhone());
            map.put("name",location.getName());
            map.put("class1",class1);
            map.put("studentName",name);
            map.put("height",height);
            map.put("weight",weight);
            map.put("studentPhone",phone);
            map.put("gender",gender);
            Iterator<PurchaseMap> iteratorMap = purchaseMapMapper.selectByExample(
                    Example.builder(PurchaseMap.class).where(Sqls.custom().andEqualTo("purId",orderId))
                            .build()
            ).iterator();
            List listMap = new LinkedList();
            while (iteratorMap.hasNext()){
                PurchaseMap purchaseMap = iteratorMap.next();
                Map<String,Object> map1 = new HashMap<>();
                map1.put("productId",purchaseMap.getProductId());
                map1.put("productName",productMapper.getProductName(purchaseMap.getProductId()));
                map1.put("sex",purchaseMap.getSex());
                map1.put("count",purchaseMap.getCount());
                map1.put("size",purchaseMap.getSize());
                map1.put("price",purchaseMap.getPrice());
                listMap.add(map1);
            }
            map.put("detail",listMap);
            list.add(map);

        }
//        return new PageResult<>(((Page) list).getTotal(), list);
          return list;
    }

    @Override
    public Object hasBuy(Long orderId) {
        Purchase purchase = purchaseMapper.selectOneByExample(
                Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("orderId",orderId))
                        .build()
        );
        if(!purchase.getState().equals("0")){
            throw new BizException(ConstantUtil.BizExceptionCause.NO_BUYTWO);
        }
        purchase.setState("1");
        purchaseMapper.updateByExampleSelective(
                purchase,Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("orderId",orderId))
                        .build()
        );
        return "下单成功";
    }

    @Override
    public void postProduct(ProductPost productPost) {
        System.out.println(productPost);
        Long productId = productPost.getProductId();
        Product product = productMapper.selectOneByExample(
                Example.builder(Product.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        );
        String proName = productPost.getProductName();
        String description = productPost.getDescription();
        String freight = productPost.getFreight();
        Integer sex = productPost.getSex();
        Long schoolId = productPost.getSchoolId();
        String img=null,img2=null,img3=null,img4=null;
        if(productPost.getImg()!=null) {
            img = uploadFile(productPost.getImg());  //上传图片并且生成url
            product.setImg(img);
        }
        if(productPost.getImg2()!=null) {
            img2 = uploadFile(productPost.getImg2());  //上传图片并且生成url
            product.setImg2(img2);
        }
        if(productPost.getImg3()!=null) {
            img3 = uploadFile(productPost.getImg3());  //上传图片并且生成url
            product.setImg3(img3);
        }
        if(productPost.getImg4()!=null) {
            img4 = uploadFile(productPost.getImg4());  //上传图片并且生成url
            product.setImg4(img4);
        }

        String file=null;
        if(productPost.getFile()!=null) {
            file = uploadFile(productPost.getFile());
            File file1 = new File();
            file1.setProductId(productId);
            file1.setUrl(file);
            fileMapper.updateByExample(file1,
                    Example.builder(File.class).where(Sqls.custom().andEqualTo("productId",productId))
                            .build()
                    );
        }
        String[] sizes = productPost.getSize();
        String[] prices = productPost.getPrice();
        Integer[] counts = productPost.getCount();
        if (!proName.equals("")) {
            product.setProductName(proName);
        }
        if (!description.equals("")) {
            product.setDescription(description);
        }
        if (!freight.equals("")) {
            product.setFreight(freight);
        }
        if (sex!=null) {
            product.setSex(sex);
        }
        if(schoolId!=null){
            product.setSchoolId(schoolId);
        }
        String price="10000.0";
        if(sizes.length!=prices.length){
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_SIZEANDPRICE);
        }
        if(sizes.length!=0){
            //先清空
            priceMapper.deleteByExample(
                    Example.builder(Price.class).where(Sqls.custom().andEqualTo("productId",productId))
                            .build()
            );
            for(int i=0;i<sizes.length;i++){
                String price1 = prices[i];
                String size = sizes[i];
                Integer count = counts[i];
                if(Double.valueOf(price)>Double.valueOf(price1)){
                    price = price1;
                }  //将最低价格作为商品价格保存
                Long id = ConstantUtil.generateId();
                if(price1.equals("")||size.equals("")){
                    throw new BizException(ConstantUtil.BizExceptionCause.LOSS_DETAIL);
                }
                Price productSon = new Price();
                productSon.setId(id);
                productSon.setPrice(price1);
                productSon.setSize(size);
                productSon.setCount(count);
                productSon.setProductId(productId);
                priceMapper.insert(productSon);
            }
            product.setPrice(price);
        }


//        productMapper.insert(product);
        productMapper.updateByExample(
                product,Example.builder(Product.class).where(Sqls.custom().andEqualTo("productId",productId))
                        .build()
        );
    }

    @Override
    public void postSendProduct(Send send) {
        Long orderId = send.getOrderId();
        String number = send.getNumber();
        Purchase purchase = purchaseMapper.selectOneByExample(
                Example.builder(Purchase.class).where(Sqls.custom().andEqualTo("orderId",orderId))
                        .build());
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
    public void deletePurchase(Long orderId) {
        purchaseMapper.deletePurchase(orderId);
        purchaseMapper.deleteMap(orderId);
    }
}
