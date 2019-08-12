package edu.nomadness.sarafan.service;

import edu.nomadness.sarafan.domain.Comment;
import edu.nomadness.sarafan.domain.Message;
import edu.nomadness.sarafan.domain.User;
import edu.nomadness.sarafan.domain.Views;
import edu.nomadness.sarafan.dto.EventType;
import edu.nomadness.sarafan.dto.ObjectType;
import edu.nomadness.sarafan.repository.CommentRepository;
import edu.nomadness.sarafan.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final BiConsumer<EventType, Comment> wsSender;

  @Autowired
  public CommentService(CommentRepository commentRepository, WsSender wsSender) {
    this.commentRepository = commentRepository;
    this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
  }

  public Comment create(Comment comment, User user) {
    comment.setAuthor(user);
    Comment commentFromDb = commentRepository.save(comment);

    wsSender.accept(EventType.CREATE, commentFromDb);

    return commentFromDb;
  }
}
