package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void MemberRepositoryTest() {

        // given
        Member member = new Member("memberA");
        memberJpaRepository.save(member);

        // when
        Member findMember = memberJpaRepository.find(member.getId());

        // then
        assertEquals(member.getId(), findMember.getId());
        assertEquals(member.getUsername(), findMember.getUsername());
        assertEquals(member, findMember);
    }

    @Test
    public void MemberJpaRepositoryTest() {
        // given
        Member member = new Member("member1");
        Member member1 = new Member("member2");

        // when
        memberJpaRepository.save(member);
        memberJpaRepository.save(member1);


        Member findMember = memberJpaRepository.findById(member.getId()).get();
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();

        // then
        assertEquals(member, findMember);
        assertEquals(member1, findMember1);

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertEquals(all.size(), 2);

        //카운트 조회
        long count = memberJpaRepository.count();
        assertEquals(count, 2);

        //삭제 검증
        memberJpaRepository.delete(member);
        memberJpaRepository.delete(member1);

        long count1 = memberJpaRepository.count();
        assertEquals(count1, 0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThenTest() {

        // given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberJpaRepository.save(member);
        memberJpaRepository.save(member1);
        // when

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 15);

        // then
        assertEquals("AAA", result.get(0).getUsername());
        assertEquals(20, result.get(0).getAge());
        assertEquals(1, result.size());

    }

    @Test
    public void pagingTest() {
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> result = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        // 페이지 계산 공식 적용

        assertEquals(result.size(), 3);
        assertEquals(totalCount, 5);

    }
}