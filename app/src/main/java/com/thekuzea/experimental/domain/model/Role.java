package com.thekuzea.experimental.domain.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Immutable
@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
