package com.jel.tech.sc.ch02.service;

import com.jel.tech.sc.ch02.exception.NoRouteFound;
import com.jel.tech.sc.ch02.model.AbTestingRoute;
import com.jel.tech.sc.ch02.repository.AbTestingRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/6 16:53
 * @Description
 **/
@Service
public class AbTestingRouteService {

    @Autowired
    private AbTestingRouteRepository abTestingRouteRepository;

    public AbTestingRoute getRoute(String serviceName) {

        AbTestingRoute route = abTestingRouteRepository.findByServiceName(serviceName);

        if (route==null){
            throw new NoRouteFound();
        }
        return route;
    }

    public void saveAbTestingRoute(AbTestingRoute route){

        abTestingRouteRepository.save(route);

    }

    public void updateRouteAbTestingRoute(AbTestingRoute route){
        abTestingRouteRepository.save(route);
    }

    public void deleteRoute(AbTestingRoute route){
        abTestingRouteRepository.delete(route);
    }
}
