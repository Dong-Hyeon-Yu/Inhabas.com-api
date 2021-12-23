package com.inhabas.api.repository.member;

import com.inhabas.api.domain.member.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaMemberRepository implements MemberRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Member findById(Integer id) {
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Member update(Member member) {
        return em.merge(member);
    }

    // 테스트용
    public void detach(Member member) {
        em.detach(member);
    }
}