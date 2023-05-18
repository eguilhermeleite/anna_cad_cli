package com.anna.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@ToString
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	 
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue
	public String cpf;
	public String name;
	public String email;
}
