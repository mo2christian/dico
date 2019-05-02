package com.mo2christian.dico.api;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/v1")
public class DicoController {

    @Autowired
    private Dico dico;

    @Autowired
    private Logger logger;

    public DicoController() {
    }

    @GetMapping("/exist")
    public ResponseEntity<Status> isExist(@RequestParam("word") String word) {
        logger.info("Find existing word {}", word);
        boolean exist = dico.exist(word.toLowerCase());
        if(exist){
            return ResponseEntity.ok(new Status(String.format("Word found : %s", word)));
        }
        else{
            return ResponseEntity.status(404).body(new Status(Status.ERROR, String.format("Word found : %s", word)));
        }
    }

    @GetMapping("/suggest")
    public ResponseEntity<?> suggest(@RequestParam("word") String letters,
                                     @RequestParam(name = "nbWord", defaultValue = "1000") int nbWord,
                                     @RequestParam(name = "nbLetter", defaultValue = "-1") int nbLetter){
        logger.info("Find {} suggestions of {}-letters word with letters {}", nbWord, nbLetter, letters);
        List<String> words = dico.suggest(letters.toLowerCase(), nbWord, nbLetter);
        return ResponseEntity.ok(words);
    }

    @RequestMapping(value = "/health")
    public ResponseEntity<?> health(){
        dico.exist("test");
        return ResponseEntity.ok("OK");
    }

}
