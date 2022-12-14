package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);   //컬렉션

    Member findMemberByUsername(String username);   //단건

    Optional<Member> findOptionalByUsername(String username);   //단건 Optional

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * 업데이트 쿼리 필수
     * update 쿼리는 영속성 컨텍스트는 무시하기 때문에 영속성 컨텍스트의 값은 바뀌지 않음
     * (Automatically 옵션을 true로 바꿔주면 적용 em.clear를 컨텍스트를 초기화 해줌).
     * DB값만 바뀜.
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("SELECT m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})                 //원하는 옵션을 넣고 엔티티 그래프로 fetch join할 수 있음
    List<Member> findEntityGraphByUsername(String username);

}
