package com.calpullix.service.users.model;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String name;
	
	@Basic
	@Column(name = "position")
	private Integer positionvalue;
	
	@Transient
	private EmployeePosition position;
	
	@OneToOne(mappedBy = "idemployee", fetch = FetchType.LAZY)
	private Users branch;

	private String sex;
	
	private Integer age;
	
	private String address;
	
	private BigDecimal monthlysalary;
	
	private BigDecimal isr;
		
	@PostLoad
    void fillTransient() {
        if (positionvalue > 0) {
            this.position = EmployeePosition.of(positionvalue);
        }
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(position == null)) {
            this.positionvalue = position.getIdPosition();
        }
    }
    
}
