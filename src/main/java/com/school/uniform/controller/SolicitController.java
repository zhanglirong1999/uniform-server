package com.school.uniform.controller;

import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dto.post.AddSolicite;
import com.school.uniform.model.dto.post.PostSolicit;
import com.school.uniform.service.SolicitaionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/solicit")
@WebResponse
public class SolicitController {
    @Autowired
    private SolicitaionService solicitaionService;

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
     * 显示征订的商品列表
     * @return
     */
    @TokenRequired
    @GetMapping("/list")
    public Object getList(){
        return solicitaionService.getList();
    }
}
