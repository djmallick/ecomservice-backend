package com.vrs.payload;

import java.util.List;

import com.vrs.dto.OrderDto;

public class OrderPagedResponse {
	boolean isSuccessful;
	String message;
	private List<OrderDto> contents;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private boolean lastPage;
	public OrderPagedResponse(boolean isSuccessful, String message, List<OrderDto> contents, Integer pageNumber,
			Integer pageSize, Long totalElements, Integer totalPages, boolean lastPage) {
		super();
		this.isSuccessful = isSuccessful;
		this.message = message;
		this.contents = contents;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.lastPage = lastPage;
	}
	public boolean isSuccessful() {
		return isSuccessful;
	}
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<OrderDto> getContents() {
		return contents;
	}
	public void setContents(List<OrderDto> contents) {
		this.contents = contents;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	public OrderPagedResponse() {
		super();
	}
	
	

}
