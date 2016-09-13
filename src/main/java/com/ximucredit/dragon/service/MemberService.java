package com.ximucredit.dragon.service;

import java.util.List;

import com.ximucredit.dragon.DO.MemberDO;
import com.ximucredit.teambition.Member;

public interface MemberService {
//	public MemberDO findByMemberId(String memberId);
	public MemberDO findByUserAndProjectId(String userId,String projectId);
	public List<MemberDO> findByUserId(String userId);
	public List<MemberDO> findByProjectId(String projectId);
	public List<MemberDO> updateMember(String projectId,List<Member> members);
	public void updateMember(MemberDO member);
	public void deleteMember(String memberId);
	public void deleteMembers(String projectId);
}
