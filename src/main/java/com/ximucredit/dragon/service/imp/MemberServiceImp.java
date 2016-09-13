package com.ximucredit.dragon.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximucredit.dragon.DO.MemberDO;
import com.ximucredit.dragon.DO.UserDO;
import com.ximucredit.dragon.dao.MemberMapper;
import com.ximucredit.dragon.dao.UserMapper;
import com.ximucredit.dragon.service.MemberService;
import com.ximucredit.teambition.Member;

@Service("memberService")
public class MemberServiceImp implements MemberService {
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private UserMapper userMapper;

	public MemberDO findByUserAndProjectId(String userId, String projectId) {
		return memberMapper.findByUserAndProjectId(userId, projectId);
	}

	public List<MemberDO> findByUserId(String userId) {
		return memberMapper.findByUserId(userId);
	}

	public List<MemberDO> findByProjectId(String projectId) {
		List<MemberDO> mems=memberMapper.findByProjectId(projectId);
		for(MemberDO mem:mems){
			UserDO user=userMapper.findById(mem.getUserId());
			mem.setUser(user);
		}
		return mems;
	}

	public List<MemberDO> updateMember(String projectId,List<Member> members) {
		if(members!=null&&members.size()>0){
			memberMapper.deleteMemberByProjectId(projectId);
			
			List<MemberDO> list=new ArrayList<MemberDO>();
			for(Member member:members){
				MemberDO mem=new MemberDO();
				mem.setMemberId(member.getId());
				mem.setProjectId(projectId);
				mem.setUserId(member.getUserId());
				
				memberMapper.insert(mem);
				
				UserDO user=userMapper.findById(member.getUserId());
				if(user==null){
					user=new UserDO();
					user.setUserId(member.getUserId());
					user.setName(member.getName());
					user.setEmail(member.getEmail());
					
					userMapper.insert(user);
				}
				mem.setUser(user);
				
				list.add(mem);
			}
			
			return list;
		}
		return null;
	}
	
	public void updateMember(MemberDO member) {
		memberMapper.deleteMember(member.getUserId(), member.getProjectId());
		
		memberMapper.insert(member);
	}
	
	public void deleteMember(String memberId) {
		memberMapper.deleteMemberById(memberId);
	}
	
	public void deleteMembers(String projectId) {
		memberMapper.deleteMemberByProjectId(projectId);
	}

}
