package com.example.task5_mailing.controller;

import com.example.task5_mailing.entity.Message;
import com.example.task5_mailing.entity.User;
import com.example.task5_mailing.payload.UserMessagesDto;
import com.example.task5_mailing.repository.MessageRepository;
import com.example.task5_mailing.repository.UserRepository;
import com.example.task5_mailing.service.MessageService;
import com.example.task5_mailing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final MessageRepository messageRepository;

    @GetMapping
    public String mainPage(HttpSession session, Model model){
        if(session.getAttribute("username") == null){
            session.setAttribute("username","Username");
            userService.saveUser("Username");
        }

        String username = (String)session.getAttribute("username");
        List<UserMessagesDto> messages =  messageService.getMessages(username);
        model.addAttribute("messages", messages);
        model.addAttribute("username", username);
        return "main-page";
    }

    @PostMapping("messages/new-message")
    public String newMessage(
        @RequestParam String usernames,
        @RequestParam String title,
        @RequestParam String content,
        HttpSession session
    ){
        String currentUserName = (String)session.getAttribute("username");
        messageService.sendMessages(usernames,title,content,currentUserName);
        return "redirect:/";
    }

    @GetMapping("message/{id}")
    public String getMessage(
            @PathVariable Long id,
            Model model
        ){
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if(optionalMessage.isPresent()){
            Message message = optionalMessage.get();
            model.addAttribute("message",
                    new UserMessagesDto(
                            id,
                            message.getSender().getUsername(),
                            message.getTitle(),
                            message.getContent(),
                            message.getSentTime()
                            ));
        }

        return "message";
    }


}
