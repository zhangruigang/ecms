package com.qslion.framework.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页
 *
 * @author Gray.Z
 * @date 2018/4/3 20:25.
 */
public class Pager<T> implements Serializable {

    private static final long serialVersionUID = -2053800594583879853L;

    /**
     * 内容
     */
    private final List<T> content = new ArrayList<>();

    /**
     * 总记录数
     */
    private final long totalCount;

    /**
     * 分页信息
     */
    @JsonIgnore
    private final Pageable pageable;


    /**
     * 自定义数据
     */
    private Map<String, Object> extras;

    /**
     * 构造方法
     */
    public Pager() {
        this.totalCount = 0L;
        this.pageable = new Pageable();
    }

    /**
     * 构造方法
     *
     * @param content    内容
     * @param totalCount 总记录数
     * @param pageable   分页信息
     */
    public Pager(List<T> content, long totalCount, Pageable pageable) {
        this.content.addAll(content);
        this.totalCount = totalCount;
        this.pageable = pageable;
    }

    public Pager(List<T> content, long totalCount, Pageable pageable, Map<String, Object> extras) {
        this.content.addAll(content);
        this.totalCount = totalCount;
        this.pageable = pageable;
        this.extras = extras;
    }

    public Pager<EntityVo> wrap(Function<? super T, EntityVo> mapper) {
        List<EntityVo> content = getContent().stream().map(mapper).collect(Collectors.toList());
        return new Pager<>(content, getTotalCount(), getPageable(), getExtras());
    }

    /**
     * 获取页码
     *
     * @return 页码
     */
    public int getPageNo() {
        return pageable.getPageNo();
    }

    /**
     * 获取每页记录数
     *
     * @return 每页记录数
     */
    public int getPageSize() {
        return pageable.getPageSize();
    }

    /**
     * 获取搜索属性
     *
     * @return 搜索属性
     */
    public String getSearchProperty() {
        return pageable.getSearchProperty();
    }

    /**
     * 获取搜索值
     *
     * @return 搜索值
     */
    public String getSearchValue() {
        return pageable.getSearchValue();
    }

    /**
     * 获取排序属性
     *
     * @return 排序属性
     */
    public String getOrderProperty() {
        return pageable.getOrderProperty();
    }

    /**
     * 获取排序方向
     *
     * @return 排序方向
     */
    public Order.Direction getOrderDirection() {
        return pageable.getOrderDirection();
    }

    /**
     * 获取排序
     *
     * @return 排序
     */
    @JsonIgnore
    public List<Order> getOrders() {
        return pageable.getOrders();
    }

    /**
     * 获取筛选
     *
     * @return 筛选
     */
    @JsonIgnore
    public List<QueryFilter> getFilters() {
        return pageable.getQueryFilters();
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getTotalPages() {
        return (int) Math.ceil((double) getTotalCount() / (double) getPageSize());
    }

    /**
     * 获取内容
     *
     * @return 内容
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 获取分页信息
     *
     * @return 分页信息
     */
    public Pageable getPageable() {
        return pageable;
    }

    /**
     * 获取空分页
     *
     * @param pageable 分页信息
     * @return 空分页
     */
    public static final <T> Pager<T> emptyPage(Pageable pageable) {
        return new Pager<>(Collections.<T>emptyList(), 0L, pageable);
    }

    /**
     * 重写equals方法
     *
     * @param obj 对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * 重写hashCode方法
     *
     * @return HashCode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public void addExtras(String key, Object value) {
        if (extras == null) {
            extras = Maps.newHashMap();
        }
        extras.put(key, value);
    }
}