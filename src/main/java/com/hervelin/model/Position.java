package com.hervelin.model;



public class Position extends java.lang.Object{

    public int x;
    public int y;


    public Position(int x,int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String toString(){
        return new String(this.x+","+this.y);
    }

    public int distance(Position p,Position q){
        return 0;
    }
}

