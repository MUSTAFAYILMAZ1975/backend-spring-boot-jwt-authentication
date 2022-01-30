package com.javabackend.northwind.core.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "claims")
@AllArgsConstructor
@NoArgsConstructor
public class Claim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claim_id")
	private int claimId;
	
	@Column(name = "claim_name")
	@NotBlank
	@NotNull
	private String claimName;
	
	@OneToMany(mappedBy = "claim")
	private List<UserClaim> userClaims;
}
