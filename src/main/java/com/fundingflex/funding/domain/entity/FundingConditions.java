package com.fundingflex.funding.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundingConditions {
    private Long fcId;
    private Long fundingsId;
    private int collectedAmount;
    private int percent;
    
    private int goalAmount;
    
    public static FundingConditions of(Long fundingsId, int collectedAmount, int percent, int goalAmount) {
        return FundingConditions.builder()
                .fundingsId(fundingsId)
                .collectedAmount(collectedAmount)
                .percent(percent)
                .goalAmount(goalAmount)
                .build();
    }

}


/*
package com.fundingflex.funding.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funding_conditions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundingConditions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fcId;						// 펀딩 상태 아이디

	@Column(name = "fundings_id")
	private Long fundingsId;				// 펀딩 아이디

	@Column(name = "collected_amount")
	private int collectedAmount;			// 누적 금액 총액

	@Column(name = "percent")
	private int percent;					// 누적 금액 백분율

	@ManyToOne
	@JoinColumn(name = "fundings_id", insertable = false, updatable = false)
	private Fundings fundings;				
}
*/