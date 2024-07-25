package com.github.nhatoriginal.spring.doc.example;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateTaskDTO {
  private String title;
  private String description;
  private boolean completed;
}
