package com.goodworkalan.prattle.viewer.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
public class Log {
    /** The entity id. */
    private long id;
    
    /** The log directory. */
    private String directory;
    
    /** The log file name prefix. */
    private String prefix;
    
    /** The grid views of this log. */
    public List<Grid> grids;
    
    @SuppressWarnings("unchecked")
    public static List<Log> toList(List list) {
        return list;
    }
    
    /**
     * Get the entity id.
     * 
     * @return The entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * Set the entity id.
     * 
     * @param id
     *            The entity id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the log directory.
     * 
     * @return The log directory.
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Set the log directory.
     * 
     * @param directory
     *            The log directory.
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Get the log file prefix.
     * 
     * @return The log file prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Set the log file prefix.
     * 
     * @param prefix
     *            The log file prefix.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get the grid views of this log.
     * 
     * @return The grid views of this log.
     */
    @OneToMany(mappedBy = "log")
    public List<Grid> getGrids() {
        return grids;
    }

    /**
     * Set the grid views of this log.
     * 
     * @param grids
     *            The grid views of this log.
     */
    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }
}
