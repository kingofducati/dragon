/**
 * 
 */
package com.ximucredit.dragon.service;

import java.util.List;

import com.ximucredit.dragon.DO.DepartDO;
import com.ximucredit.dragon.DO.UserDO;

/**
 * @author dux.fangl
 *
 */
public interface QYManagerService {
	public void updateQYDepart(DepartDO departDO);
	public void createQYDepart(DepartDO departDO);
	public DepartDO queryDepartByID(String departId);
	public List<DepartDO> queryDepartByParent(String parentId);
	public List<DepartDO> queryDepartFromRemote(String parentId);
	public void insertDepart(DepartDO departDO);
	public void deleteQYDepart(String departId);
	public void createQYUser(UserDO user);
	public void updateQYUser(UserDO user);
	public void deleteQYUser(String userId);
	public List<UserDO> queryUserByDepartId(String departId);
	public List<UserDO> queryUserFromRemote(String departId);
	public UserDO findBDUserByEmail(String email);
	public void insertBDUser(UserDO user);
	public void updateBDUser(UserDO user);
}
