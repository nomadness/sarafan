package edu.nomadness.sarafan.service;

import edu.nomadness.sarafan.domain.Comment;
import edu.nomadness.sarafan.domain.User;
import edu.nomadness.sarafan.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
  private final CommentRepository commentRepository;

  @Autowired
  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  public Comment create(Comment comment, User user) {
    comment.setAuthor(user);
    commentRepository.save(comment);
    return comment;
  }
}
