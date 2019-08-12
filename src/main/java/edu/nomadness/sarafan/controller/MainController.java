package edu.nomadness.sarafan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.nomadness.sarafan.domain.User;
import edu.nomadness.sarafan.domain.Views;
import edu.nomadness.sarafan.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
  private final MessageRepository messageRepository;
  private final ObjectWriter writer;

  @Autowired
  public MainController(MessageRepository messageRepository, ObjectMapper mapper) {
    this.messageRepository = messageRepository;
    this.writer = mapper
            .setConfig(mapper.getSerializationConfig())
            .writerWithView(Views.FullMessage.class);
  }

  @Value("${spring.profiles.active}")
  private String profile;

  @GetMapping
  public String main(
          Model model,
          @AuthenticationPrincipal User user) throws JsonProcessingException {
    HashMap<Object, Object> data = new HashMap<>();

    if(user != null) {
      data.put("profile", user);

      String messages = writer.writeValueAsString(messageRepository.findAll());
      model.addAttribute("messages", messages);
    }

    model.addAttribute("frontendData", data);
    model.addAttribute("isDevMode", "dev".equals(profile));

    return "index";
  }
}
