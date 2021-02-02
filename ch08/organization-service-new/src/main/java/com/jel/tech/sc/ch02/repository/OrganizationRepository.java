package com.jel.tech.sc.ch02.repository;

import com.jel.tech.sc.ch02.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 11:51
 * @Description
 **/
@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String> {

    /**
     * 主键查询
     * @param organizationId
     * @return
     */
    Organization findByOrganizationId(String organizationId);
}
