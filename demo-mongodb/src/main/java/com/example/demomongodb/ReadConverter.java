package com.example.demomongodb;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class ReadConverter implements Converter<Document, DocumentEntity> {

    public DocumentEntity convert(Document source) {
        DocumentEntity p = new DocumentEntity((String) source.get("_id"), (String) source.get("name"));
        return p;
    }
}