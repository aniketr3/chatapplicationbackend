package com.aniket.chatspringboot.model;


import com.aniket.chatspringboot.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class ChatMessage {

   private String username;
   private MessageType messageType;
   private String messageString;

}
