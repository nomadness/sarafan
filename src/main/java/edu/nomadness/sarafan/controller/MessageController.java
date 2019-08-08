package edu.nomadness.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.nomadness.sarafan.domain.Message;
import edu.nomadness.sarafan.domain.Views;
import edu.nomadness.sarafan.dto.EventType;
import edu.nomadness.sarafan.dto.ObjectType;
import edu.nomadness.sarafan.repository.MessageRepository;
import edu.nomadness.sarafan.util.WsSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("message")
public class MessageController {
  private final MessageRepository messageRepository;
  private final BiConsumer<EventType, Message> wsSender;

  @Autowired
  public MessageController(MessageRepository messageRepository, WsSender wsSender) {
    this.messageRepository = messageRepository;
    this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
  }

  @GetMapping
  @JsonView(Views.IdName.class)
  public List<Message> list() {
    return messageRepository.findAll();
  }

  @GetMapping("{id}")
  @JsonView(Views.FullMessage.class)
  public Message getOne(@PathVariable("id") Message message) {
    return message;
  }

  @PostMapping
  public Message create(@RequestBody Message message) {
    message.setCreationDate(LocalDateTime.now());
    Message updatedMessage = messageRepository.save(message);

    wsSender.accept(EventType.CREATE, updatedMessage);

    return updatedMessage;
  }

  @PutMapping("{id}")
  public Message update(
          @PathVariable("id") Message messageFromDb,
          @RequestBody Message message
  ) {
    BeanUtils.copyProperties(message, messageFromDb, "id");

    Message updatedMessage = messageRepository.save(messageFromDb);

    wsSender.accept(EventType.UPDATE, updatedMessage);

    return updatedMessage;
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable("id") Message message) {
    messageRepository.delete(message);
    wsSender.accept(EventType.REMOVE, message);
  }
}
