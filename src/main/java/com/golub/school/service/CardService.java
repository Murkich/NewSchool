package com.golub.school.service;

import com.golub.school.entity.Card;
import com.golub.school.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;
    public List<Card> findCardByUsersId(Long id) {
        return cardRepository.findCardByUsersId(id);
    }
}
