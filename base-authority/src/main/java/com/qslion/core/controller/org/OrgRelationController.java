/**
 *
 */
package com.qslion.core.controller.org;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.qslion.core.entity.*;
import com.qslion.core.enums.AuPartyRelationType;
import com.qslion.core.enums.AuPartyType;
import com.qslion.core.service.AuRoleService;
import com.qslion.core.service.ConnectionRuleService;
import com.qslion.core.service.PartyRelationService;
import com.qslion.core.vo.DetailVO;
import com.qslion.custom.service.AuCompanyService;
import com.qslion.custom.service.AuDepartmentService;
import com.qslion.custom.service.AuEmployeeService;
import com.qslion.custom.service.AuPositionService;
import com.qslion.framework.bean.*;
import com.qslion.framework.bean.QueryFilter.Operator;
import com.qslion.framework.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 团体关系控制类
 *
 * @author Gray.Z
 * @date 2018/4/21 13:43.
 */
@Api(value = "团体关系Controller", description = "团体关系Controller", tags = {"团体关系控制器"})
@ResponseResult
@RestController
@RequestMapping(value = "/org/relation")
public class OrgRelationController extends BaseController<AuPartyRelation> {
    @Autowired
    private PartyRelationService partyRelationService;
    @Autowired
    private ConnectionRuleService connectionRuleService;


    @Autowired
    private AuCompanyService auCompanyService;
    @Autowired
    private AuDepartmentService auDepartmentService;
    @Autowired
    private AuPositionService auPositionService;
    @Autowired
    private AuEmployeeService auEmployeeService;
    @Autowired
    private AuRoleService auRoleService;


    @PostMapping(value = "/list/{relationTypeId}")
    public Pager<AuPartyRelation> list(@PathVariable int relationTypeId, @RequestParam Pageable pageable) {
        QueryFilter queryFilter = new QueryFilter("auPartyRelationType", Operator.equal, relationTypeId);
        if (pageable.getQueryFilters().isEmpty()) {
            pageable.setQueryFilters(Lists.newArrayList(queryFilter));
        } else {
            pageable.getQueryFilters().add(queryFilter);
        }
        return partyRelationService.findPage(pageable);
    }

    @GetMapping(value = "/detail/{id}")
    public DetailVO<PartyEntity> detail(@PathVariable Long id) {
        AuPartyRelation partyRelation = partyRelationService.findById(id);
        AuPartyType orgType = partyRelation.getPartyType();
        PartyEntity content = null;
        switch (orgType) {
            case COMPANY:
                content = auCompanyService.findById(partyRelation.getPartyId());
                break;
            case DEPARTMENT:
                content = auDepartmentService.findById(partyRelation.getPartyId());
                break;
            case POSITION:
                content = auPositionService.findById(partyRelation.getPartyId());
                break;
            case EMPLOYEE:
                content = auEmployeeService.findById(partyRelation.getPartyId());
                break;
            case ROLE:
                content = auRoleService.findById(partyRelation.getPartyId());
                break;
            case PROXY:
                break;
            default:
                break;
        }
        DetailVO<PartyEntity> detailVO = new DetailVO<>(content);

        List<AuPartyType> subOrgTypes = connectionRuleService.findAll().stream()
                .filter(auConnectionRule -> auConnectionRule.getCurPartyType() == orgType)
                .map(AuConnectionRule::getSubPartyType).collect(Collectors.toList());

        detailVO.addExtras("curOrgType", orgType.getId());
        detailVO.addExtras("subOrgTypes", subOrgTypes.stream().map(pType -> ImmutableMap.of("name", pType.getName(), "value", pType.getId()))
                .collect(Collectors.toList()));
        if (content != null) {
            detailVO.addExtras("displayColumns", DisplayColumn.getDisplayColumns(content.getClass()));
        }

        return detailVO;
    }

    @DeleteMapping
    public boolean deletes(Long[] ids) {
        try {
            List<AuPartyRelation> list = partyRelationService.findList(ids);
            partyRelationService.delete(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @PutMapping
    public Boolean update(@ModelAttribute("entity") AuPartyRelation entity) {
        return partyRelationService.update(entity) == null;
    }

    @GetMapping(value = "/type")
    public List<AuPartyRelationType> getPartyRelationTypes() {
        return Lists.newArrayList(AuPartyRelationType.values());
    }

    @GetMapping(value = {"/tree/{orgType}", "/tree"})
    public List<TreeNode> getPartyRelationTree(@PathVariable(required = false) AuPartyType orgType, @ApiIgnore @AuthenticationPrincipal AuUser user) {
        List<TreeNode> resultList;
        if (orgType == null) {
            resultList = this.partyRelationService.getGlobalRelationTree(user.getRoles());
        } else {
            resultList = this.partyRelationService.getPartyRelationTree(orgType);
        }
        return resultList;
    }
}
