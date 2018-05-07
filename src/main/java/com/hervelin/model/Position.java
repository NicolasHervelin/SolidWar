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
        int x_diff=q.getX()-p.getX();
        int y_diff=q.getY()-p.getY();
        if (x_diff<0){
            x_diff=Math.abs(x_diff);
        }
        if (y_diff<0){
            y_diff=Math.abs(y_diff);
        }
        return (x_diff+y_diff);
    }
}

