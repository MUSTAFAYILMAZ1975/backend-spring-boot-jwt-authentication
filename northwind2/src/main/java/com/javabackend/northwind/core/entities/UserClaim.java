package com.javabackend.northwind.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users_claims")
@AllArgsConstructor
@NoArgsConstructor
public class UserClaim {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_claim_id")
	private int userClaimId;
	
	@Column(name = "user_id")
	@NotBlank
	@NotNull
	private int userId;
	
	/*
	 * @Column(name = "claim_id")
	 * 
	 * @NotBlank
	 * 
	 * @NotNull private int claimId;
	 */
	
	 @ManyToOne()
     @JoinColumn(name="claim_id")
     private Claim claim;

}
