package com.dreamgamescasestudy.rest.Controllers;

import com.dreamgamescasestudy.rest.Repo.TournamentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController {

    @Autowired
    private TournamentRepo tournametRepository;

}
