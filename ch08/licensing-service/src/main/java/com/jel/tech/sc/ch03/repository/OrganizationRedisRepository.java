package com.jel.tech.sc.ch03.repository;

import com.jel.tech.sc.ch03.model.Organization;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/9 17:36
 * @Description redis操作对象crud
 **/
public interface OrganizationRedisRepository {

    /**
     * 保存
     * @param org
     */
    void saveOrganization(Organization org);

    /**
     * 更新
     * @param org
     */
    void updateOrganization(Organization org);

    /**
     * 删除
     * @param organizationId
     */
    void deleteOrganization(String organizationId);

    /**
     * 查询
     * @param organizationId
     * @return
     */
    Organization findOrganization(String organizationId);
}
