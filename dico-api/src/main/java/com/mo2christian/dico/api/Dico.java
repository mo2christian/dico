package com.mo2christian.dico.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Service
public class Dico {

    private final Letter root;

    @Value("#{environment.WORD_SET_LOCATION}")
    private String location;

    public Dico(){
        root = new Letter('-');
    }

    @PostConstruct
    private void init(){
        WordSet wordSet = new WordSet(location);
        for (String word : wordSet) {
            add(word);
        }
    }

    public void add(String word){
        int i = 0;
        Cursor c = find(root, root, word.charAt(i++));
        while (c.current != null && i < word.length()){
            c = find(c.current, c.current.getSon(), word.charAt(i++));
        }
        if (c.current != null){
            c.current.setWord(true);
            c.current.setFullWord(word);
        }
        else {
            if (c.previous == null){
                createPath(c.parent, word, --i);
            }
            else {
                Letter l = new Letter(word.charAt(i - 1));
                c.previous.setBrother(l);
                if (i == word.length()){
                    l.setWord(true);
                    l.setFullWord(word);
                }
                else {
                    createPath(c.previous.getBrother(), word, i);
                }
            }
        }
    }

    @NewSpan("com.mo2christian.dico.api.Dico.exist")
    public boolean exist(String word){
        Letter l = root;
        for (int i = 0; i < word.length() && l != null; i++){
            while (l != null && l.getValue() != word.charAt(i)){
                l = l.getBrother();
            }
            if (l != null && i != word.length() - 1){
                l = l.getSon();
            }
        }
        return l == null ? false : l.isWord();
    }

    @NewSpan("com.mo2christian.dico.api.Dico.suggest")
    public List<String> suggest(String word, int nbWord, int nbLetter){
        List<String> words = new LinkedList<>();
        suggest(root, words, new StringBuilder(word), nbWord, nbLetter);
        return words;
    }

    private void suggest(Letter root, List<String> words, StringBuilder word, int nbWord, int nbLetter){
        if (word.toString().isEmpty() || words.size() >= nbWord){
            return;
        }

        while (root != null){
            int index = word.indexOf(root.getValue() + "");
            if (index > -1){
                if (root.isWord() && root.getFullWord().length() >= nbLetter){
                    if (words.size() >= nbWord){
                        return;
                    }
                    words.add(root.getFullWord());
                }
                StringBuilder sb = new StringBuilder(word);
                sb.deleteCharAt(index);
                suggest(root.getSon(), words, sb, nbWord, nbLetter);
            }
            root = root.getBrother();
        }
    }

    private void createPath(Letter root, String word, int index){
        Letter l = new Letter(word.charAt(index++));
        root.setSon(l);
        for (;index < word.length(); index++){
            l.setSon(new Letter(word.charAt(index)));
            l = l.getSon();
        }
        l.setWord(true);
        l.setFullWord(word);
    }

    private Cursor find(Letter parent, Letter root, char c){
        Cursor cursor = new Cursor();
        cursor.parent = parent;
        cursor.current = root;
        Letter l = root;
        while (l != null && l.getValue() != c){
            cursor.previous = l;
            cursor.current = l.getBrother();
            l = l.getBrother();
        }
        return cursor;
    }

    private class Cursor{

        Letter parent;

        Letter previous;

        Letter current;

    }

}
