package com.example.dotsandboxes.objects;

import androidx.annotation.Nullable;

public class Line {

    private final Direction direction;
    private final int row;
    private final int column;
    public Line(Direction direction,int row, int column){
        this.direction = direction;
        this.row = row;
        this.column = column;

    }
    public Direction direction(){
        return direction;
    }
    public int row(){
        return row;
    }
    public int column(){
        return column;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass()!=obj.getClass())return false;
        Line line = (Line) obj;
        return row == line.row && column == line.column && direction==line.direction;
    }



    @Override
    public String toString() {
        return "direction:" + direction().toString() + "row:" + row + "column" + column;
    }
}
