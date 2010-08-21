package com.goodworkalan.notice.viewer.controller;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import javax.persistence.EntityManager;

import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.controller.qualifiers.Request;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.stream.Output;
import com.goodworkalan.notice.json.JsonEntry;
import com.goodworkalan.notice.json.JsonEntryReader;
import com.goodworkalan.notice.viewer.model.Log;
import com.google.inject.Inject;

// FIXME EntriesGet.
@Actors(value = InfusionActor.class)
public class EntryGet {
    /** The log id. */
    private long id;
    
    private Date start = new Date(0);
    
    /** The entity manager. */
    private final EntityManager em;
    
    @Inject
    public EntryGet(EntityManager em) {
        this.em = em;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setStart(Date start) {
        this.start = start;
    }
    
    @Output(contentType = "text/plain")
    public void text(@Request Writer out) throws IOException {
        Log log = em.find(Log.class, id);
        File directory = new File(log.getDirectory());
        JsonEntryReader reader = new JsonEntryReader(directory, log.getPrefix(), start);
        for (JsonEntry entry : reader) {
            out.write(entry.getJsonString());
            out.write("\n");
        }
    }
}
