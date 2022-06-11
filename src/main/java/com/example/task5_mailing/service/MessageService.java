package com.example.task5_mailing.service;

import com.example.task5_mailing.entity.Message;
import com.example.task5_mailing.entity.User;
import com.example.task5_mailing.payload.UserMessagesDto;
import com.example.task5_mailing.repository.MessageRepository;
import com.example.task5_mailing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    public List<UserMessagesDto> getMessages(String username){
        List<Message> messages = messageRepository.findByReceiver_Username(username);
        List<UserMessagesDto> messageDtoList = new ArrayList<>();

        for (Message message : messages){
            messageDtoList.add(
                    new UserMessagesDto(
                            message.getId(),
                            message.getSender().getUsername(),
                            message.getTitle(),
                            message.getContent(),
                            message.getSentTime()
                    )
            );
        }

        return messageDtoList;
    }

    public void sendMessages(String usernames, String title,String content, String senderName){
        Optional<User> sender = userRepository.findByUsername(senderName);
        if(sender.isEmpty())
            throw new RuntimeException("Current user is null");
        String[] users = usernames.split(", ");
        for (String username: users){
            Optional<User> receiver = userRepository.findByUsername(username);
            if(receiver.isPresent()){
                Message message = new Message(null, sender.get(), receiver.get(), title, content, Time.valueOf(LocalTime.now()));
                messageRepository.save(message);
                send(message);
            }
        }
    }

    private void send(Message message){
        UserMessagesDto messageDto = new UserMessagesDto(
                message.getId(),
                message.getSender().getUsername(),
                message.getTitle(),
                message.getContent(),
                message.getSentTime()
        );
        String receiver = message.getReceiver().getUsername();

        messagingTemplate.convertAndSendToUser(receiver,"/new-message",messageDto);
    }

}
