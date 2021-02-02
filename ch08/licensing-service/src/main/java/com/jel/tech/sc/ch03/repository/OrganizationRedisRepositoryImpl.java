package com.jel.tech.sc.ch03.repository;

import com.jel.tech.sc.ch03.model.Organization;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/9 17:38
 * @Description
 **/
@Repository
public class OrganizationRedisRepositoryImpl implements OrganizationRedisRepository{

    private static final String HASH_NAME = "organization";

    @Resource
    private RedisTemplate<String, Organization> redisTemplate;

    private HashOperations ops;

    @PostConstruct
    private void init() {
        ops = redisTemplate.opsForHash();
    }

    @Override
    public void saveOrganization(Organization org) {
        ops.put(HASH_NAME, org.getId(), org);
    }

    @Override
    public void updateOrganization(Organization org) {
        ops.put(HASH_NAME, org.getId(), org);
    }

    @Override
    public void deleteOrganization(String organizationId) {
        ops.delete(HASH_NAME, organizationId);
    }

    @Override
    public Organization findOrganization(String organizationId) {
        return (Organization) ops.get(HASH_NAME, organizationId);
    }
}
