package com.fundingflex.funding.service;

import com.fundingflex.funding.domain.dto.FundingRequestDTO;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FundingUpdateQueue {

    private static final BlockingQueue<FundingRequestDTO> queue = new LinkedBlockingQueue<>();


    // 큐에 넣기
    public static void addFundingUpdate(FundingRequestDTO requestDtos) {
        try {
            queue.put(requestDtos);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 큐에 빼기
    public static FundingRequestDTO takeFundingUpdate() {
        try {
            return queue.take();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
