package com.example.demomongodb;

public class DocumentEntity {
    private String name;
    private String _id;
    public DocumentEntity(String id, String name){
        this._id = id;
        this.name = name;
    }
    public DocumentEntity(){}
    public String toString(){
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
