package com.github.nhatoriginal.spring.doc.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "title", unique = true, nullable = false)
  private String title;

  @Column(name = "description", unique = true, nullable = false)
  private String description;

  @Column(name = "completed", unique = true, nullable = false)
  private boolean completed;
}
