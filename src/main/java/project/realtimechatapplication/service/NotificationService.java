package project.realtimechatapplication.service;

import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.model.type.Reaction;

public interface NotificationService {

  void createAndSendNotifications(ChatDto chatDto, MessageSendResponseDto responseDto);

  void createAndSendReactionNotification(Long messageId, String reactorUsername, Reaction reaction);

  void createAndSendAddMemberNotification(String chatRoomName, Long addedUser,
      String addedByUsername);
}
