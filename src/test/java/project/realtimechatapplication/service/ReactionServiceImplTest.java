package project.realtimechatapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.realtimechatapplication.dto.request.reaction.AddReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.EditReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.ReactionDto;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.ReactionEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.MessageNotExistException;
import project.realtimechatapplication.exception.impl.ReactionNotFoundException;
import project.realtimechatapplication.exception.impl.UnauthorizedReactionEditException;
import project.realtimechatapplication.exception.impl.UnauthorizedReactionRemoveException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.model.type.Reaction;
import project.realtimechatapplication.repository.jpa.MessageRepository;
import project.realtimechatapplication.repository.jpa.ReactionRepository;
import project.realtimechatapplication.repository.jpa.UserRepository;
import project.realtimechatapplication.service.impl.ReactionServiceImpl;

public class ReactionServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ReactionRepository reactionRepository;

  @InjectMocks
  private ReactionServiceImpl reactionService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testAddReaction_Success() {
    AddReactionRequestDto requestDto = new AddReactionRequestDto();
    requestDto.setMessageId(1L);
    requestDto.setReaction(Reaction.HEART);

    UserEntity user = new UserEntity();
    user.setUsername("testUser");

    MessageEntity message = new MessageEntity();
    message.setId(1L);

    ReactionEntity reaction = new ReactionEntity();
    reaction.setId(1L);
    reaction.setUser(user);
    reaction.setMessage(message);
    reaction.setReaction(Reaction.HEART);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(messageRepository.findById(anyLong())).thenReturn(Optional.of(message));
    when(reactionRepository.save(any(ReactionEntity.class))).thenReturn(reaction);

    ReactionDto response = reactionService.addReaction(requestDto, "testUser");

    verify(userRepository).findByUsername("testUser");
    verify(messageRepository).findById(1L);
    verify(reactionRepository).save(any(ReactionEntity.class));

    assertEquals(Reaction.HEART, response.getReaction());
  }

  @Test
  public void testAddReaction_UserNotFound() {
    AddReactionRequestDto requestDto = new AddReactionRequestDto();
    requestDto.setMessageId(1L);
    requestDto.setReaction(Reaction.HEART);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> reactionService.addReaction(requestDto, "testUser"));
  }

  @Test
  public void testAddReaction_MessageNotFound() {
    AddReactionRequestDto requestDto = new AddReactionRequestDto();
    requestDto.setMessageId(1L);
    requestDto.setReaction(Reaction.HEART);

    UserEntity user = new UserEntity();
    user.setUsername("testUser");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(messageRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(MessageNotExistException.class,
        () -> reactionService.addReaction(requestDto, "testUser"));
  }

  @Test
  public void testEditReaction_Success() {
    EditReactionRequestDto requestDto = new EditReactionRequestDto();
    requestDto.setReaction(Reaction.ANGRY);

    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    MessageEntity message = new MessageEntity();
    message.setId(1L);

    ReactionEntity reaction = new ReactionEntity();
    reaction.setId(1L);
    reaction.setUser(user);
    reaction.setReaction(Reaction.HEART);
    reaction.setMessage(message);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(reactionRepository.findById(anyLong())).thenReturn(Optional.of(reaction));

    ReactionDto response = reactionService.editReaction(requestDto, "testUser", 1L);

    verify(userRepository).findByUsername("testUser");
    verify(reactionRepository).findById(1L);
    verify(reactionRepository).save(any(ReactionEntity.class));

    assertEquals(Reaction.ANGRY, response.getReaction());
  }

  @Test
  public void testEditReaction_ReactionNotFound() {
    EditReactionRequestDto requestDto = new EditReactionRequestDto();
    requestDto.setReaction(Reaction.HEART);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));
    when(reactionRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(MessageNotExistException.class,
        () -> reactionService.editReaction(requestDto, "testUser", 1L));
  }

  @Test
  public void testEditReaction_UnauthorizedEdit() {
    EditReactionRequestDto requestDto = new EditReactionRequestDto();
    requestDto.setReaction(Reaction.HEART);

    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    UserEntity differentUser = new UserEntity();
    differentUser.setId(2L);

    ReactionEntity reaction = new ReactionEntity();
    reaction.setId(1L);
    reaction.setUser(differentUser);
    reaction.setReaction(Reaction.HAPPY);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(reactionRepository.findById(anyLong())).thenReturn(Optional.of(reaction));

    assertThrows(UnauthorizedReactionEditException.class,
        () -> reactionService.editReaction(requestDto, "testUser", 1L));
  }

  @Test
  public void testRemoveReaction_Success() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    ReactionEntity reaction = new ReactionEntity();
    reaction.setId(1L);
    reaction.setUser(user);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(reactionRepository.findById(anyLong())).thenReturn(Optional.of(reaction));

    reactionService.removeReaction(1L, "testUser");

    verify(userRepository).findByUsername("testUser");
    verify(reactionRepository).findById(1L);
    verify(reactionRepository).delete(reaction);
  }

  @Test
  public void testRemoveReaction_ReactionNotFound() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));
    when(reactionRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(ReactionNotFoundException.class,
        () -> reactionService.removeReaction(1L, "testUser"));
  }

  @Test
  public void testRemoveReaction_UnauthorizedRemove() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    UserEntity differentUser = new UserEntity();
    differentUser.setId(2L);

    ReactionEntity reaction = new ReactionEntity();
    reaction.setId(1L);
    reaction.setUser(differentUser);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(reactionRepository.findById(anyLong())).thenReturn(Optional.of(reaction));

    assertThrows(UnauthorizedReactionRemoveException.class,
        () -> reactionService.removeReaction(1L, "testUser"));
  }

  @Test
  public void testGetReactionsByMessageId_Success() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    MessageEntity message = new MessageEntity();
    message.setId(1L);

    ReactionEntity reaction = new ReactionEntity();
    reaction.setId(1L);
    reaction.setReaction(Reaction.HEART);
    reaction.setMessage(message);
    reaction.setUser(user);

    when(messageRepository.findById(anyLong())).thenReturn(Optional.of(message));
    when(reactionRepository.findByMessage(any(MessageEntity.class))).thenReturn(
        Collections.singletonList(reaction));

    List<ReactionDto> reactions = reactionService.getReactionsByMessageId(1L);

    verify(messageRepository).findById(1L);
    verify(reactionRepository).findByMessage(message);

    assertEquals(1, reactions.size());
    assertEquals(Reaction.HEART, reactions.get(0).getReaction());
  }

  @Test
  public void testGetReactionsByMessageId_MessageNotFound() {
    when(messageRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(MessageNotExistException.class, () -> reactionService.getReactionsByMessageId(1L));
  }
}
