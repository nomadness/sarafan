package edu.nomadness.sarafan.dto;

import com.fasterxml.jackson.annotation.JsonView;
import edu.nomadness.sarafan.domain.Message;
import edu.nomadness.sarafan.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDto {
  private List<Message> messages;
  private int currentPage;
  private int totalPages;
}