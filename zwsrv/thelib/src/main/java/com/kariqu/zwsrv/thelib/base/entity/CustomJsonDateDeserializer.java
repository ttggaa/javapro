package com.kariqu.zwsrv.thelib.base.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by simon on 14/03/17.
 */
public class CustomJsonDateDeserializer extends JsonDeserializer<Date>
{
    @Override
    public Timestamp deserialize(JsonParser jsonparser,
                                 DeserializationContext deserializationcontext) throws IOException{

        SimpleDateFormat format = new SimpleDateFormat(BaseEntity.TIMESTAMP);
        String date = jsonparser.getText();
        try {
            return new Timestamp(format.parse(date).getTime());
        } catch (ParseException e) {
            format = new SimpleDateFormat(BaseEntity.SQL_TIMESTAMP);
            try {
                return new Timestamp(format.parse(date).getTime());
            } catch (ParseException e1) {
                return null;
            }
        }

    }

}
