package pers.dlx.mpstreaking.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import pers.dlx.mpstreaking.mapper.TblTestMapper;
import pers.dlx.mpstreaking.model.Response;
import pers.dlx.mpstreaking.streaking.StreakingMybatisUtilMapper;
import pers.dlx.mpstreaking.template.JtSqlEngine;
import pers.dlx.mpstreaking.utils.FileUtil;

import java.util.Map;

/**
 * @author dinglingxiang  2020/3/13 9:50
 */
@RestController
@Api(tags = "统计分析")
@Slf4j
public class StatController {

    @Autowired
    private StreakingMybatisUtilMapper streakingMybatisUtilMapper;

    @Autowired
    private TblTestMapper tblTestMapper;

    @ApiOperation("执行任意sql接口")
    @RequestMapping(value = "/v1/stat/test1", method = RequestMethod.POST)
    public @ResponseBody
    Response queryStat(@RequestBody Map<String, String> map) throws Exception {
        Response response = new Response();

        String template = FileUtil.readAll(ResourceUtils.getFile("classpath:static/sqltemplate/demo1.js"));
        response.setContent(new JtSqlEngine(streakingMybatisUtilMapper).exec(template, map));

        return response;
    }


    @ApiOperation("执行任意sql接口")
    @RequestMapping(value = "/v1/stat/test2", method = RequestMethod.POST)
    public @ResponseBody
    Response queryStat2(@RequestBody Map<String, String> map) throws Exception {
        Response response = new Response();

        response.setContent(tblTestMapper.selectList(null));

        return response;
    }

}
