package com.aniket.chatspringboot.controller;

import com.aniket.chatspringboot.enums.MessageType;
import com.aniket.chatspringboot.model.ChatMessage;
import com.aniket.chatspringboot.model.MessageBody;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Array;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    private Map<String, List<String>> rooms = new HashMap<>();
    private List<String> users = new ArrayList<>();

    @MessageMapping("/newUser")
    public void handleNewUser(ChatMessage chatMessage) {
        users.add(chatMessage.getUsername());
        System.out.println(users);

//        messagingTemplate.convertAndSend("/topic/" + chatMessage.getUsername(), "Connected to the server");
        Set<String> listofrooms = rooms.keySet();


          messagingTemplate.convertAndSend("/topic/"+ chatMessage.getUsername() ,listofrooms);
    }

    @MessageMapping("/createRooms")
    public void createRooms(ChatMessage chatMessage) {
        if (chatMessage.getMessageType().equals(MessageType.CREATEROOM)) {
            System.out.println("hello");
            String roomName = chatMessage.getMessageString();

            rooms.put(roomName, new ArrayList<>());
            rooms.get(roomName).add(chatMessage.getUsername());
            System.out.println(rooms);
            chatMessage.setMessageType(MessageType.ACK);
            chatMessage.setUsername(chatMessage.getUsername());
            chatMessage.setMessageString("created and connected to " + roomName);
            messagingTemplate.convertAndSend("/topic/" + chatMessage.getUsername(),"created and connected to " + roomName );
           Set<String> listofrooms = rooms.keySet();
            messagingTemplate.convertAndSend("/topic/" + chatMessage.getUsername(), listofrooms);
           return;
        }
        messagingTemplate.convertAndSend("/topic/common", "Error creating room");
    }

    
    @MessageMapping("/joinRoom")
    public void joinRoom(ChatMessage chatMessage) {
        if (chatMessage.getMessageType().equals(MessageType.JOINROOM)) {
            String roomname = chatMessage.getMessageString();
            System.out.println(roomname);
            String user = chatMessage.getUsername();
            if (!rooms.isEmpty() && rooms.containsKey(roomname)) {
//                List<String> roomList = rooms.get(roomname);
//                roomList.add(user);
                for (String key : rooms.keySet()) {
                    System.out.println("Key: " + key + ", Value: " + rooms.get(key));
                }

                rooms.get(roomname).add(user);
//                rooms.put(roomname, roomList);

                messagingTemplate.convertAndSend("/topic/" + chatMessage.getUsername(), "room joined succesfully"+ roomname);
              return;
            }
        }
        messagingTemplate.convertAndSend("/topic/" + chatMessage.getUsername(), "error creating room ");
    }


    @MessageMapping("/message")
    public void sendMessage(MessageBody messageBody){
        String room = messageBody.getMessageRoom();
        String text = messageBody.getMessageText();
        messagingTemplate.convertAndSend("/topic/" + room , messageBody);
    }

}