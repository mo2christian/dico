package com.mo2christian.dico.api;

public class Letter {

    private char value;

    private boolean word;

    private String fullWord;

    private String explanation;

    private Letter brother;

    private Letter son;

    public Letter(char c){
        word = false;
        brother = null;
        son = null;
        this.value = c;
    }

    public String getFullWord() {
        return fullWord;
    }

    public void setFullWord(String fullWord) {
        this.fullWord = fullWord;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public boolean isWord() {
        return word;
    }

    public void setWord(boolean word) {
        this.word = word;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Letter getBrother() {
        return brother;
    }

    public void setBrother(Letter brother) {
        this.brother = brother;
    }

    public Letter getSon() {
        return son;
    }

    public void setSon(Letter son) {
        this.son = son;
    }
}
