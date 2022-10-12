package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void MemberRepositoryTest() {
        // given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);

    }

    @Test
    public void MemberJpaRepositoryTest() {
        // given
        Member member = new Member("member1");
        Member member1 = new Member("member2");

        // when
        memberRepository.save(member);
        memberRepository.save(member1);


        Member findMember = memberRepository.findById(member.getId()).get();
        Member findMember1 = memberRepository.findById(member1.getId()).get();

        // then
        assertEquals(member, findMember);
        assertEquals(member1, findMember1);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(), 2);

        //카운트 조회
        long count = memberRepository.count();
        assertEquals(count, 2);

        //삭제 검증
        memberRepository.delete(member);
        memberRepository.delete(member1);

        long count1 = memberRepository.count();
        assertEquals(count1, 0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThenTest() {

        // given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);
        // when

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        // then
        assertEquals("AAA", result.get(0).getUsername());
        assertEquals(20, result.get(0).getAge());
        assertEquals(1, result.size());
    }

    @Test
    public void findUserTest() {
        // given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);
        // when

        List<Member> result = memberRepository.findUser("AAA", 10);

        // then
        assertEquals(1, result.size());
    }

    @Test
    public void findUsernameListTest() {
        // given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("BBB", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        // when
        List<String> usernameList = memberRepository.findUsernameList();

        // then
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDtoTest() {
        // given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("AAA", 10);
        member.setTeam(team);
        memberRepository.save(member);

        // when
        List<MemberDto> memberDto = memberRepository.findMemberDto();


        // then
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findNamesTest() {
        // given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("BBB", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        // when
        List<Member> findNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        // then
        for (Member findName : findNames) {
            System.out.println("findName = " + findName);
        }
    }

    @Test
    public void returnTypeTest() {
        // given
        Member member = new Member("AAA", 10);
        Member member1 = new Member("BBB", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<Member> result = memberRepository.findListByUsername("asdsad");        //컬렉션은 값이 없어도 빈 컬렉션을 반환해줌, 단건은 null로 나옴
        System.out.println("result = " + result.size());                            //0

    }


    @Test
    public void pageableTest() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> result = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = result.getContent();
        long totalElements = result.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println(totalElements);

        assertEquals(content.size(), 3);        //현재 페이지 엘리먼트 개수
        assertEquals(totalElements, 5);         //전체 페이지의 엘리먼트 총 개수
        assertEquals(result.getNumber(), 0);    //page 넘버
        assertEquals(result.getTotalPages(), 2);
        assertEquals(result.isFirst(), true);
        assertEquals(result.hasNext(), true);

    }



}