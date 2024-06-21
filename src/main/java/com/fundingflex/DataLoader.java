/*
package com.fundingflex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fundingflex.entity.Fundings;
import com.fundingflex.repository.FundingsRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private FundingsRepository fundingsRepository;

    @Override
    public void run(String... args) throws Exception {
        // 테스트 데이터를 삽입
        Fundings funding = new Fundings();
        funding.setTitle("Test Funding2");
        funding.setContent("This is a test funding project2.");
        funding.setLikeCount(5);
        funding.setGoalAmount(1000000);
        funding.setCreatedBy("admin");
        funding.setUpdatedBy("admin");
        funding.setIsDeleted('N');

        fundingsRepository.save(funding);

        System.out.println("Test data has been inserted.");
    }
}
*/