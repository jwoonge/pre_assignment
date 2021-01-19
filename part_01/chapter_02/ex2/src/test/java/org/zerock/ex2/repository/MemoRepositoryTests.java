package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testInsertDummies()
    {
        IntStream.rangeClosed(1, 100).forEach(i ->
        {
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect()
    {
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("-----");
        if (result.isPresent())
        {
            Memo memo = result.get();
            System.out.println(memo.getMemoText());
        }
    }
    @Transactional
    @Test
    public void testSelect2()
    {
        Long mno = 99L;
        Memo memo = memoRepository.getOne(mno);
        System.out.println("------");
        System.out.println(memo);
    }

    @Test
    public void testUpdate()
    {
        Memo memo = Memo.builder().mno(101L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete()
    {
        Long mno = 100L;
        memoRepository.deleteById(mno);
        memoRepository.deleteById((mno+1));
    }

    @Test
    public void testPageDefault()
    {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("------------");
        System.out.println("Total Pages : "+result.getTotalPages());
        System.out.println("Total Count : "+result.getTotalElements());
        System.out.println("Page Number : "+result.getNumber());
        System.out.println("Page Size : " + result.getSize());
        System.out.println("has Next Page? "+result.hasNext());
        System.out.println("first page? "+result.isFirst());

        System.out.println("-------------");
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethods()
    {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);
        for (Memo memo : list)
            System.out.println(memo);
    }

    @Test
    public void testQueryMethodWithPageable()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods()
    {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testQuery()
    {
        //List<Memo> list = memoRepository.getListDesc();
        //for (Memo m : list)
        //    System.out.println(m);
        int a = memoRepository.updateMemoText(15L, "test15");
    }
}
