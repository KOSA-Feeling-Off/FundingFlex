package com.fundingflex.mybatis.mapper.chat;

import org.apache.ibatis.annotations.*;

import com.fundingflex.consultation.chat.domain.dto.ChatDTO;

import java.util.List;

@Mapper
public interface ChatMapper {

    @Insert("INSERT INTO CHAT (chat_number, content, created_at, user_id, is_admin) VALUES (#{chatNumber}, #{content}, #{createdAt}, #{userId}, #{isAdmin})")
    void save(ChatDTO chat);

    ChatDTO selectChatById(Long chatId);

    List<ChatDTO> selectAllChats();

    void updateChat(ChatDTO chat);

    void deleteChat(Long chatId);
}
