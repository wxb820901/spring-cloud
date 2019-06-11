package com.example.demomongodb;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class WriteConverter implements Converter<DocumentEntity, Document> {
    public Document convert(DocumentEntity source) {
        Document document = new Document();
        document.put("_id", source.get_id());
        document.put("name", source.getName());
        return document;
    }
}
