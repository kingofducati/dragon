/**
 * 
 */
package com.ximucredit.dragon.dao;

import java.util.List;

import com.ximucredit.dragon.DO.DepartDO;

/**
 * @author dux.fangl
 *
 */
public interface DepartMapper {
	public List<DepartDO> listAll();
	public DepartDO findById(String departId);
	public List<DepartDO> findByParent(String parentId);
	public void insert(DepartDO departDO);
	public void updateByPrimaryKey(DepartDO departDO);
}
