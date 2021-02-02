package com.jel.tech.sc.ch02.repository;

import com.jel.tech.sc.ch02.model.AbTestingRoute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/6 16:55
 * @Description
 **/
@Repository
public interface AbTestingRouteRepository extends CrudRepository<AbTestingRoute, String> {

    AbTestingRoute findByServiceName(String serviceName);
}
