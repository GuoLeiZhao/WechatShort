package com.sqx.modules.activity.controller;


import com.sqx.common.utils.Result;
import com.sqx.modules.activity.entity.InviteActivity;
import com.sqx.modules.activity.request.InviteActivityQueryReq;
import com.sqx.modules.activity.service.InviteActivityService;
import com.sqx.modules.shop.entity.Item;
import com.sqx.modules.shop.request.ItemQueryReq;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "邀请活动", tags = {"邀请活动"})
@RequestMapping(value = "/activity/invite")
@RequiredArgsConstructor
public class InviteActivityController extends AbstractController {

    private final InviteActivityService inviteActivityService;

    @PostMapping()
    @ApiOperation("添加活动信息")
    public Result insertItem(@RequestBody InviteActivity inviteActivity) {
        return inviteActivityService.insert(inviteActivity);
    }

    @PutMapping()
    @ApiOperation("修改活动信息")
    public Result updateItem(@RequestBody InviteActivity inviteActivity) {
        return Result.success().setData(inviteActivityService.saveOrUpdate(inviteActivity));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("假删除")
    public Result updateDelete(@PathVariable Long id) {
        return Result.success().setData(inviteActivityService.delete(id));
    }

    @GetMapping("/selectPage")
    @ApiOperation("查询活动信息")
    public Result selectItem(InviteActivityQueryReq req) {
        return Result.success().setData(inviteActivityService.page(req));
    }

    @GetMapping("/selectById/{id}")
    @ApiOperation("根据id查询活动详细信息")
    public Result selectItemById(@PathVariable Long id) {
        return Result.success().setData(inviteActivityService.getById(id));
    }



}
