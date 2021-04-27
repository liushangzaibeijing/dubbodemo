package com.xiu.dubbo.common;

import java.io.Serializable;

/**
 * 分页查询参数
 *
 */
public class PageInfo implements Serializable {
	private static final long serialVersionUID = 1206017417120973323L;
	// 页码
	Integer page = 0;
	// 记录条数
	Integer rows = 10;
	// 排序字段
	String sort;
	// 升降顺序 asc desc
	String order = "asc";

	public PageInfo() {
		super();
	}

	public PageInfo(Integer page, Integer rows) {
		super();
		this.page = page;
		this.rows = rows;
	}

	public PageInfo(Integer page, Integer rows, String sort, String order) {
		super();
		this.page = page;
		this.rows = rows;
		this.sort = sort;
		this.order = order;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}


}
