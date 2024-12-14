package com.pras.account;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * User account model with unique user name and account activation flag
 * 
 * @author Prasanta
 *
 */
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String userName;
	private Boolean active;
	private Date updateTime;
	private Date creationTime;
	
	@PrePersist
	public void onCreate() {
		Date now = new Date();
		if(this.getCreationTime() == null)
			setCreationTime(now);
		if(this.getUpdateTime() == null)
			setUpdateTime(now);
	}
	
	@PreUpdate
	public void onUpdate() {
		setUpdateTime(new Date());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	@Override
	public String toString() {
		return "Users [id=" + id + ", userName=" + userName + ", active=" + active + "]";
	}
}
