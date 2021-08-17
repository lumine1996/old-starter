package com.cgm.starter.account.service;

import com.cgm.starter.account.entity.Organization;

/**
 * @author cgm
 */
public interface IOrganizationService {
    /**
     * 添加组织
     *
     * @param organization 组织
     */
    void addOrganization(Organization organization);
}
