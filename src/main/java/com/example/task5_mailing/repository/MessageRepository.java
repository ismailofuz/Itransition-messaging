package com.example.task5_mailing.repository;

import com.example.task5_mailing.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where m.receiver.username = ?1 order by m.sentTime desc")
    List<Message> findByReceiver_Username(String username);
}
