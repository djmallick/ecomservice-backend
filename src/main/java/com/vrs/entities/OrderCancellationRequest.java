package com.vrs.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ordercancelrequest")
public class OrderCancellationRequest {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "request_id")
	private int cancellationRequestId;
	private Date dateOfRequest;
	private String reasonOfCancellation;
	private Date dateOfReview;
	private boolean isActive;
	private boolean isCancelled;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
	private Order order;
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public int getCancellationRequestId() {
		return cancellationRequestId;
	}
	public void setCancellationRequestId(int cancellationRequestId) {
		this.cancellationRequestId = cancellationRequestId;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}
	public void setDateOfRequest(Date dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isCancelled() {
		return isCancelled;
	}
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	public Date getDateOfReview() {
		return dateOfReview;
	}
	public void setDateOfReview(Date dateOfReview) {
		this.dateOfReview = dateOfReview;
	}
	public String getReasonOfCancellation() {
		return reasonOfCancellation;
	}
	public void setReasonOfCancellation(String reasonOfCancellation) {
		this.reasonOfCancellation = reasonOfCancellation;
	}
	
	
}
