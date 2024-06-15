package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysResource;

import java.util.List;


public interface SysResourceMapper {

    /**
     * Query all resources and include their parent menus
     *
     * @return List of resources
     */
    List<SysResource> findAllResources();

    /**
     * Query all menus and include their direct children
     *
     * @return List of menus
     */
    List<SysResource> findWithChildren();

    /**
     * Query all permissions in parent-child relationships
     *
     * @return List of permissions
     */
    List<SysResource> findAllWithAllChildren();

    SysResource findResourceById(Long id);

    int addResource(SysResource sysResource);

    int updateResource(SysResource sysResource);

    int deleteResource(Long id);

}
