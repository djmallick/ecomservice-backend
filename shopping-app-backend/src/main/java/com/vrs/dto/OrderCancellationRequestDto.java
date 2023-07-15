package com.vrs.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderCancellationRequestDto {

	private int cancellationRequestId;
	private Date dateOfRequest;
	
	@NotEmpty(message = "Must not be empty")
	@NotNull
	private String reasonOfCancellation;
	private Date dateOfReview;
	private boolean isActive;
	private boolean isCancelled;

	public String getReasonOfCancellation() {
		return reasonOfCancellation;
	}
	public void setReasonOfCancellation(String reasonOfCancellation) {
		this.reasonOfCancellation = reasonOfCancellation;
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
	public Date getDateOfReview() {
		return dateOfReview;
	}
	public void setDateOfReview(Date dateOfReview) {
		this.dateOfReview = dateOfReview;
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
	
	
		
}
