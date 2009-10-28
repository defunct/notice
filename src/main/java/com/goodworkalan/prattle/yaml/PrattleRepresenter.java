package com.goodworkalan.prattle.yaml;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import com.goodworkalan.prattle.Bean;
import com.goodworkalan.prattle.Serialization;

// TODO Document
public class PrattleRepresenter extends Representer
{
    // TODO Document
    public PrattleRepresenter()
    {
        this.representers.put(Bean.class, new BeanRepresent());
        this.representers.put(Serialization.class, new SerializationRepresent());
    }
    
    // TODO Document
    public class BeanRepresent implements Represent
    {
        public Node representData(Object data)
        {
            Bean bean = (Bean) data;
            return representMapping("!!" + bean.getBeanClass().getCanonicalName(), bean.getProperties(), null);
        }
    }

    /**
     * Writes a serialized object to the YAML stream by first deserializing it.
     * 
     * @author Alan Gutierrez
     */
    public class SerializationRepresent implements Represent
    {
        /**
         * Write the deserialized object to the YAML stream.
         * 
         * @param data
         *            The serialization object.
         */
        public Node representData(Object data)
        {
            Serialization serialization = (Serialization) data;
            Object deserialized;
            try
            {
                deserialized = serialization.deserialize();
            }
            catch (Exception e)
            {
                String message = "Unable to deserialized object of " + serialization.getSerializationClass();
                deserialized = new YamlError(message, e);
            }
            return PrattleRepresenter.this.representData(deserialized);
        }
    }
}
