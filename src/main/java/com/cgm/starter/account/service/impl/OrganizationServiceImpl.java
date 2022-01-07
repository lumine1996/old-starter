package com.cgm.starter.account.service.impl;

import com.cgm.starter.account.dto.OrganizationParamDTO;
import com.cgm.starter.account.entity.Organization;
import com.cgm.starter.account.mapper.OrganizationMapper;
import com.cgm.starter.account.service.IOrganizationService;
import com.cgm.starter.base.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author cgm
 */
@Service
public class OrganizationServiceImpl implements IOrganizationService {
    @Resource
    private OrganizationMapper organizationMapper;

    @Override
    public List<Organization> listOrganization(OrganizationParamDTO paramDTO) {
        return organizationMapper.listOrganization(paramDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrganization(Organization organization) {
        organizationMapper.insertSelective(organization);
        Assert.notNull(organization.getId(), ErrorCode.SYS_ORG_ADD_FAILED);
    }

    @Override
    public void updateOrganization(Organization organization) {
        organization.setUpdateTime(new Date());
        organizationMapper.updateByPrimaryKeySelective(organization);
    }
}
