package com.example.viikko9test;

public class Theatre {

    String name;
    String ID;

    Theatre(){
    }

    public void setName (String s){
        this.name = s;
    }

    public String getName(){
        return name;
    }

    public void setID(String i){
        ID = i;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
