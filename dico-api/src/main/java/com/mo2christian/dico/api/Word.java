package com.mo2christian.dico.api;

public class Word {

    private String value;

    private String explanation;

    public Word(){
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        return getValue().equals(word.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
