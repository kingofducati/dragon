package com.ximucredit.dragon.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ximucredit.dragon.DO.MemberDO;

public interface MemberMapper {
	public MemberDO findByMemberId(String memberId);
	public MemberDO findByUserAndProjectId(@Param("userId")String userId,@Param("projectId")String projectId);
	public List<MemberDO> findByUserId(String userId);
	public List<MemberDO> findByProjectId(String projectId);
	public void insert(MemberDO memberDO);
	public void deleteMember(@Param("userId")String userId,@Param("projectId")String projectId);
	public void deleteMemberById(String memberId);
	public void deleteMemberByProjectId(String projectId);
}
