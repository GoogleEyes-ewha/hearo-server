package com.gdsc.hearo.domain.member.repository;

import com.gdsc.hearo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByLoginId(String loginId);
}
