package com.qslion.authority.core.dao;


import com.qslion.authority.core.entity.AuMenu;
import com.qslion.authority.core.entity.AuResource;
import com.qslion.framework.dao.IGenericRepository;
import com.qslion.framework.enums.EnableStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao实现类 - 资源
 *
 * @author Gray.Z
 * @date 2018/4/30 13:56.
 */
@Repository
public interface AuResourceRepository extends IGenericRepository<AuResource, Long> {

    AuResource findByMenu(AuMenu auMenu);

    /**
     * @param enableStatus 状态
     * @return list
     */
    List<AuResource> findByEnableStatus(EnableStatus enableStatus);
}