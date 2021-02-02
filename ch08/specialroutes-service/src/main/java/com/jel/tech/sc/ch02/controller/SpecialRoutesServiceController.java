package com.jel.tech.sc.ch02.controller;

import com.jel.tech.sc.ch02.model.AbTestingRoute;
import com.jel.tech.sc.ch02.service.AbTestingRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:49
 * @Description
 **/
@RestController
@RequestMapping(value="v1/route/")
public class SpecialRoutesServiceController {

    @Autowired
    AbTestingRouteService routeService;

    @GetMapping("abtesting/{serviceName}")
    public AbTestingRoute abstestings(@PathVariable("serviceName") String serviceName) {

        return routeService.getRoute(serviceName);
    }
}
