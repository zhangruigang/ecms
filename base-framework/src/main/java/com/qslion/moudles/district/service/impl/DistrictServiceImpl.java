package com.qslion.moudles.district.service.impl;

import com.qslion.framework.bean.TreeNode;
import com.qslion.framework.service.impl.GenericServiceImpl;
import com.qslion.moudles.district.dao.DistrictRepository;
import com.qslion.moudles.district.entity.CommonDistrict;
import com.qslion.moudles.district.service.DistrictService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ecms
 *
 * @author Gray.Z
 * @date 2018/10/30 22:46.
 */
@Service
@Transactional(value = "transactionManager", rollbackFor = Exception.class)
public class DistrictServiceImpl extends GenericServiceImpl<CommonDistrict, Long> implements
    DistrictService {

    @Autowired
    DistrictRepository districtRepository;

    @Override
    public List<TreeNode> getDistrictTreeByUpid(String upid) {
        List<TreeNode> resultList = new ArrayList<>();
        List<CommonDistrict> list = districtRepository.findByUpid(Long.valueOf(upid));
        for (CommonDistrict district : list) {
            TreeNode treeNode = new TreeNode(district.getId(), district.getName());
           // treeNode.setUrl("admin/district/index.jspx?id=" + district.getId());
            //treeNode.setTarget("rightFrame");
            //treeNode.setPid(district.getUpid());
            boolean hasChildren = false;//districtDao.hasChildren(district.getId());
            if (hasChildren) {
                //treeNode.setIsParent(true);
            } else {
               // treeNode.setIsParent(false);
            }
            resultList.add(treeNode);
        }
        return resultList;
    }

    public List<TreeNode> getChildTreeNode(Long upid, List<CommonDistrict> list) {
        List<TreeNode> resultList = new ArrayList<TreeNode>();
        for (CommonDistrict district : list) {
            if (district.getUpid().toString().equals(upid)) {
                TreeNode leafNode = null;//new TreeNode(district.getId(),district.getName());
                //leafNode.setUrl("admin/district/index.jspx?id=" + district.getId());
                //leafNode.setTarget("rightFrame");
                List<TreeNode> childList = this.getChildTreeNode(district.getId(), list);
                if (childList.size() > 0) {
                    leafNode.setChildren(childList);
                }
                resultList.add(leafNode);
            }
        }
        return resultList;
    }
}
