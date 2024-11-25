package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() throws Exception {
        //given 이런게 주어졌을때
        Member member = new Member();
        member.setName("kim");

        //when 이렇게 하면
        Long saveId = memberService.join(member);

        //then 이러게 된다.
//        em.flush();
        Assertions.assertEquals(member, memberRepository.findOne(saveId));
     }

     @Test(expected = IllegalStateException.class) //언노테이션 넣으면 코드 깔끔바리임.
     public void 중복_회원_예약() throws Exception {
         //given
         Member member1 = new Member();
         Member member2 = new Member();

         member1.setName("kim");
         System.out.println("kim 저장 두번 되는가??");
         member2.setName("kim");
         //when
         memberService.join(member1);
//         try {
//             memberService.join(member1);
//         } catch (IllegalStateException e) {
//             return;
//         }
         memberService.join(member2);

         //then
         fail("예외가 발생해야 합니다.");
      }


}