package com.goodworkalan.prattle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serialization
{
    private final byte[] serialized;
    
    private final Class<?> serializationClass;
    
    private Serialization(byte[] serialized, Class<?> serializationClass) throws IOException
    {
        this.serializationClass = serializationClass;
        this.serialized = serialized; 
    }
    
    public Class<?> getSerializationClass()
    {
        return serializationClass;
    }
    
    public Object deserialize() throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bytes = new ByteArrayInputStream(serialized);
        ObjectInputStream in = new ObjectInputStream(bytes);
        return in.readObject();
    }
    
    public static Serialization getInstance(Serializable serializable) throws IOException
    {
        if (serializable == null)
        {
            return null;
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(serializable);
        out.close();
        
        return new Serialization(bytes.toByteArray(), serializable.getClass());
    }
}
