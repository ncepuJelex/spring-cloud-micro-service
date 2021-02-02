package com.jel.tech.sc.ch03;

import com.jel.tech.sc.ch03.model.Organization;
import com.jel.tech.sc.ch03.repository.OrganizationRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
class Ch03ApplicationTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private OrganizationRedisRepository organizationRedisRepository;

	@Test
	void contextLoads() {

        ValueOperations ops = redisTemplate.opsForValue();

        Object o = ops.get("name");

        System.out.println(o);
    }

    @Test
    public void testRedisGet() {

        Organization organization = organizationRedisRepository.findOrganization("jel");

        System.out.println(organization);
    }

    @Test
    public void testRedisUpdate() {

        Organization org = new Organization();
        org.setId("jel");
        org.setContactPhone("111");
        org.setContactEmail("email163");
        org.setName("zs");
        org.setContactName("goodZ");

        organizationRedisRepository.updateOrganization(org);
    }

    @Test
    public void testRedisDelete() {

	    organizationRedisRepository.deleteOrganization("jel");
    }
}
