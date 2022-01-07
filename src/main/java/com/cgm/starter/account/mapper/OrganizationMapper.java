package com.cgm.starter.account.mapper;

import com.cgm.starter.account.dto.OrganizationParamDTO;
import com.cgm.starter.account.entity.Organization;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author cgm
 */
public interface OrganizationMapper extends Mapper<Organization> {
    /**
     * 查询组织列表
     *
     * @param paramDTO 查询参数
     * @return 组织列表
     */
    List<Organization> listOrganization(@Param("paramDTO") OrganizationParamDTO paramDTO);
}
