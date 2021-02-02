package com.jel.tech.sc.ch03.repository;

import com.jel.tech.sc.ch03.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/4 11:01
 * @Description
 **/
@Repository
public interface LicenseRepository extends CrudRepository<License, String> {

    /**
     * 根据组织id查询许可证
     * @param organizationId
     * @return
     */
    List<License> findByOrganizationId(String organizationId);

    /**
     * 查找组织的特定许可证 信息
     * @param organizationId
     * @param licenseId
     * @return
     */
    License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
