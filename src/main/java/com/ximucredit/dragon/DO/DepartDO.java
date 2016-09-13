/**
 * 
 */
package com.ximucredit.dragon.DO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dux.fangl
 *
 */
public class DepartDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8315069087499794360L;

	private String departId;
	private String departName;
	private String parentId;
	private boolean delFlag;
	private Date gmtCreate;
	private Date gmtModify;
	
	public boolean isDelFlag() {
		return delFlag;
	}
	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModify() {
		return gmtModify;
	}
	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}
}
