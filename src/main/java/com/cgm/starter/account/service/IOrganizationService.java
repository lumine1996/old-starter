package com.cgm.starter.account.service;

import com.cgm.starter.account.dto.OrganizationParamDTO;
import com.cgm.starter.account.entity.Organization;

import java.util.List;

/**
 * @author cgm
 */
public interface IOrganizationService {
    /**
     * 查询组织列表
     *
     * @param paramDTO 查询参数
     * @return 组织列表
     */
    List<Organization> listOrganization(OrganizationParamDTO paramDTO);

    /**
     * 添加组织
     *
     * @param organization 组织
     */
    void addOrganization(Organization organization);

    /**
     * 更新组织
     *
     * @param organization 组织
     */
    void updateOrganization(Organization organization);
}
