package com.school.uniform.controller;

import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dto.post.*;
import com.school.uniform.service.ProductService;
import com.school.uniform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/product")
@WebResponse
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private HttpServletRequest request;
    /**
     * 管理员查看订单总量等信息
     * @return
     */
    @TokenRequired
    @GetMapping("/sum")
    public Object getProductList(){
        return productService.getProductList();
    }

    /**
     * 新增标签
     * @param tagAdd
     * @return
     */
    @TokenRequired
    @PostMapping("/tag")
    public Object addTag(
            @RequestBody TagAdd tagAdd
            ){
        productService.addTag(tagAdd);
        return "新增成功";
    }

    /**
     * 删除标签
     * @param tagId
     * @return
     */
    @TokenRequired
    @DeleteMapping("/tag")
    public Object deleteTag(
            @RequestParam Long tagId
    ){
        productService.deleteTag(tagId);
        return "删除成功";
    }

    @TokenRequired
    @GetMapping("/tag")
    public Object getTag(
    ){
        return productService.getTag();
    }

    /**
     * 删除商品
     * @param productId
     * @return
     */
    @TokenRequired
    @DeleteMapping("/delete")
    public Object deletePro(
            @RequestParam Long productId
    ){
        productService.deletePro(productId);
        return "删除成功";
    }

    /**
     * 学校下对应的产品
     * @return
     */
    @TokenRequired
    @GetMapping("/list")
    public Object getSchoolList(
    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);

        Long schoolId = redisUtil.getSchoolId(accountId);
        return productService.getSchoolList(schoolId);
    }

    /**
     * 新增商品
     * @param productAdd
     * @return
     */
    @TokenRequired
    @PostMapping("/add")
    public Object addProduct(
            @ModelAttribute ProductAdd productAdd
            ){
        productService.productAdd(productAdd);
        return "新增成功";
    }

    @TokenRequired
    @PostMapping("/change")
    public Object postProduct(
            @ModelAttribute ProductPost productPost
    ){
        productService.postProduct(productPost);
        return "修改成功";
    }



    /**
     * 加入购物车
     * @param shopping
     * @return
     */
    @TokenRequired
    @PostMapping("/shopping")
    public Object addShopping(
            @RequestBody ShoppingPost shopping
            ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        productService.addShopping(shopping,accountId);
        return "加入购物车";
    }

    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    @TokenRequired
    @GetMapping("/detail")
    public Object productDetail(
            @RequestParam Long productId
    ){
        return productService.productDetail(productId);
    }

    /**
     * 购买商品
     * @param purchaseShop
     * @return
     */
    @TokenRequired
    @PostMapping("/purchase")
    public Object purchase(
            @RequestBody Purchase1 purchaseShop

    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        return  productService.purchase(purchaseShop,accountId);
    }

    @TokenRequired
    @PostMapping("/purchase2")
    public Object purchase2(
            @RequestParam String isBuy,
            @RequestParam Long orderId
    ){
        if ((isBuy.equals("1"))){
            return productService.hasBuy(orderId);
        }
        else {
            return "支付失败";
        }
    }


    /**
     * 查看购物车
     * @return
     */
    @TokenRequired
    @GetMapping("/cartlist")
    public Object getCartList(){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        return productService.getCartList(accountId);
    }

    /**
     * 查看购买列表
     * @return
     */
    @TokenRequired
    @GetMapping("/purchase")
    public Object getPurchaseList(){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        return productService.getPurchaseList(accountId);
    }

    /**
     * 根据名字模糊搜索商品
     * @param index
     * @return
     */
    @TokenRequired
    @GetMapping("/search")
    public Object searchByName(
            @RequestParam String index
    ){
        return productService.searchProduct(index);
    }

    /**
     * 通过学校/标签筛选
     * @param schoolId
     * @return
     */
    @TokenRequired
    @GetMapping("/filrate")
    public Object filrateProduct(
            @RequestParam Long schoolId
    ){
        return productService.getSchoolList(schoolId);
    }

    /**
     * 发货
     * @param send
     * @return
     */
    @TokenRequired
    @PostMapping("/send")
    public Object sendProduct(
            @RequestBody Send send
            ){
        productService.sendProduct(send);
        return "单号填写成功";
    }

    /**
     * 获取质检文件的url
     * @param productId
     * @return
     */
    @TokenRequired
    @GetMapping("/file")
    public Object getFileUrl(
            @RequestParam Long productId
    ){
        return productService.getFileUrl(productId);
    }

    @TokenRequired
    @GetMapping("/order/search")
    public Object getSearch(
            @RequestParam Long schoolId,
            @RequestParam String state,
            @RequestParam String type
    ){
        return productService.getSearchBy(schoolId, state, type);
    }

















}
