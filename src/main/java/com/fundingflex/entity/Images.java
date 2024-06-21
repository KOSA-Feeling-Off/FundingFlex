package com.fundingflex.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = "fundings")
@Entity
@Table(name = "IMAGES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Images {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imgId;

	@Column(length = 500)
	private String imageUrl;

	private int seq;

	@ManyToOne
	@JoinColumn(name = "fundings_id")
	@JsonBackReference
	private Fundings fundings;

	@CreatedDate
	private LocalDateTime createdAt;

	private char isDeleted;
}