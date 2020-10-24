package com.school.uniform.controller;

import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dto.post.AddSolicite;
import com.school.uniform.model.dto.post.PostSolicit;
import com.school.uniform.service.SolicitaionService;
import com.school.uniform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/solicit")
@WebResponse
public class SolicitController {
    @Autowired
    private SolicitaionService solicitaionService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 新增征订订单
     * @param addSolicite
     * @return
     */
    @TokenRequired
    @PostMapping("/add")
    public Object addProduct(
            @RequestBody AddSolicite addSolicite
    ){
        solicitaionService.addSolicit(addSolicite);
        return "新增征订订单成功";
    }

    /**
     * 删除征订
     * @param sid
     * @return
     */
    @TokenRequired
    @DeleteMapping("delete")
    public Object deleteProduct(
            @RequestParam Long sid
    ){
        solicitaionService.deleteSolicit(sid);
        return "删除成功";
    }

    /**
     * 修改征订信息
     * @param postSolicit
     * @return
     */
    @TokenRequired
    @PostMapping("/put")
    public Object putProduct(
            @RequestBody PostSolicit postSolicit
            ){
        solicitaionService.postSolicit(postSolicit);
        return "修改成功";
    }

    /**
     * 显示征订的商品列表(管理员
     * @return
     */
    @TokenRequired
    @GetMapping("/list")
    public Object getList(){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        Long sid = redisUtil.getSolicitId(accountId);
        return solicitaionService.getList();
    }

    @TokenRequired
    @GetMapping("/user")
    public Object getUserSolicit(){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        Long sid = redisUtil.getSolicitId(accountId);
        return solicitaionService.getUserSolicit(sid);
    }


    @TokenRequired
    @GetMapping("/filrate")
    public Object getSolicitFilrate(
            @RequestParam Long sid
    ){
        return solicitaionService.getSolicitProduct(sid);
    }
}
