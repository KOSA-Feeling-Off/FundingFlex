package com.fundingflex.entity;

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
	private Long fcId;

	@Column(name = "fundings_id")
	private Long fundingsId;

	@Column(name = "collected_amount")
	private int collectedAmount;

	@Column(name = "percent")
	private int percent;

	@ManyToOne
	@JoinColumn(name = "fundings_id", insertable = false, updatable = false)
	private Fundings fundings;
}