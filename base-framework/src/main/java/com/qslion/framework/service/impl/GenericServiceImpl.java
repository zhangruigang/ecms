
package com.qslion.framework.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qslion.framework.bean.*;
import com.qslion.framework.dao.IGenericRepository;
import com.qslion.framework.entity.BaseEntity;
import com.qslion.framework.service.IGenericService;
import com.qslion.framework.util.DateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * server
 *
 * @author Gray.Z
 * @date 2018/4/13 10:56.
 */
public class GenericServiceImpl<T extends BaseEntity<ID>, ID extends Serializable> implements IGenericService<T, ID> {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    private IGenericRepository<T, ID> genericRepository;

    protected Class<T> entityClazz;

    @SuppressWarnings("unchecked")
    public GenericServiceImpl() {
        this.entityClazz = null;
        Class c = getClass();
        Type type = c.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClazz = (Class<T>) parameterizedType[0];
        }
    }

    @Autowired
    protected void setGenericRepository(IGenericRepository<T, ID> genericRepository) {
        this.genericRepository = genericRepository;
    }

    private PageRequest getPageRequest(Pageable pageable) {
        Sort sort = Sort.by(new Sort.Order[0]);
        if (CollectionUtils.isNotEmpty(pageable.getOrders())) {
            List<Sort.Order> orders = pageable.getOrders().stream().map(order ->
                    new Sort.Order(Sort.Direction.fromString(order.getDirection().name()), order.getProperty()))
                    .collect(Collectors.toList());
            if (StringUtils.isNotEmpty(pageable.getOrderProperty())) {
                orders.add(new Sort.Order(Sort.Direction.valueOf(pageable.getOrderDirection().name().toUpperCase()),
                        pageable.getOrderProperty()));
            }
            sort = Sort.by(orders);
        } else {
            if (StringUtils.isNotEmpty(pageable.getOrderProperty())) {
                sort = new Sort(Direction.valueOf(pageable.getOrderDirection().name().toUpperCase()),
                        pageable.getOrderProperty());
            }
        }
        return PageRequest.of(pageable.getPageNo() - 1, pageable.getPageSize(), sort);
    }

    private Specification<T> getSpecification(List<QueryFilter> queryFilters) {
        return (Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            queryFilters.forEach(filter -> {
                Path path;
                if (filter.getProperty().contains("->")) {
                    String[] joinProperties = filter.getProperty().split("->");
                    path = root.join(joinProperties[0], JoinType.INNER).get(joinProperties[1]);
                } else {
                    path = root.get(filter.getProperty());
                }

                Object valueObj = filter.getValue();
                if (valueObj instanceof String) {
                    Date dateValue = DateConverter.getDate(valueObj.toString());
                    if (dateValue != null) {
                        valueObj = dateValue;
                    }
                }
                switch (filter.getOperator()) {
                    case EQUAL:
                        predicates.add(criteriaBuilder.equal(path, valueObj));
                        break;
                    case NOT_EQUAL:
                        predicates.add(criteriaBuilder.notEqual(path, valueObj));
                        break;
                    case GT:
                        predicates.add(criteriaBuilder.gt(path, (Number) valueObj));
                        break;
                    case LT:
                        predicates.add(criteriaBuilder.lt(path, (Number) valueObj));
                        break;
                    case GE:
                        predicates.add(criteriaBuilder.le(path, (Number) valueObj));
                        break;
                    case LE:
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path, (Comparable) valueObj));
                        break;
                    case LIKE:
                        predicates.add(criteriaBuilder.like(path, "%" + valueObj + "%"));
                        break;
                    case NOT_LIKE:
                        predicates.add(criteriaBuilder.notLike(path, "%" + valueObj + "%"));
                        break;
                    case IN:
                        predicates.add(path.in(valueObj));
                        break;
                    case NOT_IN:
                        predicates.add(criteriaBuilder.not(path.in(valueObj)));
                        break;
                    case IS_NULL:
                        predicates.add(criteriaBuilder.isNull(path));
                        break;
                    case IS_NOT_NULL:
                        predicates.add(criteriaBuilder.isNotNull(path));
                        break;
                    case BETWEEN:
                        criteriaBuilder.between(path, (Comparable) valueObj, (Comparable) valueObj);
                        break;
                    case GREATER_THAN:
                        predicates.add(criteriaBuilder.greaterThan(path, (Comparable) valueObj));
                        break;
                    case GREATER_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) valueObj));
                        break;
                    case LESS_THAN:
                        predicates.add(criteriaBuilder.lessThan(path, (Comparable) valueObj));
                        break;
                    case LESS_THAN_OR_EQUAL_TO:
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(path, (Comparable) valueObj));
                        break;
                    default:
                        break;
                }
            });
            return criteriaQuery.where(predicates.toArray(new Predicate[0])) .getRestriction();
        };
    }


    @Override
    public T findById(ID id) {
        return genericRepository.findById(id).orElseGet(() -> null);
    }

    @Override
    public List<T> findAll() {
        return genericRepository.findAll();
    }

    @Override
    public List<T> findList(ID... ids) {
        return genericRepository.findAllById(Lists.newArrayList(ids));
    }

    @Override
    public List<T> findList(List<QueryFilter> queryFilters, List<Order> orders) {
        Pageable pageable = new Pageable(1, 10000);
        pageable.setQueryFilters(queryFilters);
        pageable.setOrders(orders);
        return findPage(pageable).getContent();
    }


    @Override
    public Pager<T> findPage(Pageable pageable) {
        PageRequest pageRequest = getPageRequest(pageable);
        List<QueryFilter> queryFilters = pageable.getQueryFilters();
        Page<T> page = genericRepository.findAll(getSpecification(queryFilters), pageRequest);

        List<T> result = Lists.newArrayList();
        result.addAll(page.getContent());

        Map<String, Object> extras = Maps.newHashMap();
        extras.put("displayColumns", DisplayColumn.getDisplayColumns(entityClazz));
        return new Pager<>(result, page.getTotalElements(), pageable, extras);
    }

    @Override
    public long count() {
        return genericRepository.count();
    }

    @Override
    public long count(QueryFilter... queryFilters) {
        return genericRepository.count(getSpecification(Lists.newArrayList(queryFilters)));
    }

    @Override
    public boolean exists(ID id) {
        return genericRepository.existsById(id);
    }

    @Override
    public boolean exists(QueryFilter... queryFilters) {
        return findList(Lists.newArrayList(queryFilters), null).size() > 0;
    }

    @Override
    public T save(T entity) {
        return genericRepository.save(entity);
    }

    @Override
    public T insert(T entity) {
        return genericRepository.save(entity);
    }

    @Override
    public T update(T entity) {
        return genericRepository.saveAndFlush(entity);
    }

    @Override
    public T saveOrUpdate(T entity) {
        return genericRepository.saveAndFlush(entity);
    }

    @Override
    public void delete(ID id) {
        genericRepository.deleteById(id);
    }

    @Override
    public void delete(List<T> entities) {
        genericRepository.deleteAll(entities);
    }

    @Override
    public void delete(T entity) {
        genericRepository.delete(entity);
    }

    @Override
    public void deleteByIds(List<ID> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(id -> genericRepository.deleteById(id));
        }
    }
}
