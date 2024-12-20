package com.aniket.chatspringboot.model;

import com.aniket.chatspringboot.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class MessageBody {

   private String messageText;
   private String messageRoom;
   private String messageSender;

}
