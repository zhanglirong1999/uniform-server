package com.school.uniform.controller;

import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.Log;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dto.post.LocationChange;
import com.school.uniform.model.dto.post.LocationPost;
import com.school.uniform.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@Log
@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/position")
@WebResponse
public class LocationController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LocationService locationService;

    /**
     * 新增地址
     * @param position
     * @return
     */
    @TokenRequired
    @PostMapping("/add")
    public Object addPosition(
            @RequestBody LocationPost position
    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        locationService.addPosition(position,accountId);
        return "添加地址成功";
    }

    /**
     * 修改收获地址
     * @param positon
     * @return
     */
    @TokenRequired
    @PostMapping("/post")
    public Object postPosition(
            @RequestBody LocationChange positon
            ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        locationService.postPosition(positon,accountId);
        return "修改地址成功";
    }

    /**
     * 删除地址
     * @param positionId
     * @return
     */
    @TokenRequired
    @PostMapping()
    public Object deletePosition(
            @RequestParam Long positionId
    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        locationService.deletePosition(positionId,accountId);
        return "删除地址成功";
    }

    @TokenRequired
    @GetMapping("/list")
    public Object getPositionList(){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        return locationService.getPositionList(accountId);
    }

}
