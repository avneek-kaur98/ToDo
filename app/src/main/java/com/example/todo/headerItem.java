package com.example.todo;

public class headerItem implements itemInterface{

    public String getHeader_date() {
        return header_date;
    }

    public void setHeader_date(String header_date) {
        this.header_date = header_date;
    }

    String header_date;
    public int type = 0;

    @Override
    public int getItemType() {
        return type;
    }
}
