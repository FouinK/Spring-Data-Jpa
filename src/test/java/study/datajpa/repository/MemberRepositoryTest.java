package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import study.datajpa.entity.Member;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void MemberRepositoryTest() {

        // given
        Member member = new Member("memberA");
        memberRepository.save(member);

        // when
        Member member1 = memberRepository.find(member.getId());

        // then
        assertEquals(member.getId(), member1.getId());
        assertEquals(member.getUsername(), member1.getUsername());
    }
}