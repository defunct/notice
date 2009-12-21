package com.goodworkalan.prattle.viewer.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * A grid display of log data. 
 * 
 * @author Alan Gutierrez
 */
@Entity
public class Grid {
    /** The entity id. */
    private long id;
    
    /** The log. */
    private Log log;
    
    /** The grid name. */
    private String name;
    
    /** The grid filters. */
    private List<Filter> filters;

    /** The grid columns. */
    private List<Column> columns;

    @SuppressWarnings("unchecked")
    public static List<Grid> toList(List list) {
        return list;
    }

    /**
     * Get the entity id.
     * 
     * @return The entity id.
     */
    @Id
    @javax.persistence.Column(nullable = false)
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
     * Get the log.
     * 
     * @return The log.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public Log getLog() {
        return log;
    }

    /**
     * Set the log.
     * 
     * @param log
     *            Get the log.
     */
    public void setLog(Log log) {
        this.log = log;
    }

    /**
     * Get the grid name.
     * 
     * @return The grid name.
     */
    @javax.persistence.Column(nullable = false)
    public String getName() {
        return name;
    }

    /**
     * Set the grid name.
     * 
     * @param name
     *            The grid name.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get the grid filters.
     * 
     * @return The grid filters.
     */
    @OneToMany(mappedBy = "grid")
    public List<Filter> getFilters() {
        return filters;
    }
    
    /**
     * Set the grid filters.
     * 
     * @param columns
     *            The grid filters.
     */
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * Get the grid columns.
     * 
     * @return The grid columns.
     */
    @OneToMany(mappedBy = "grid")
    public List<Column> getColumns() {
        return columns;
    }
    
    /**
     * Set the grid columns.
     * 
     * @param columns
     *            The grid columns.
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
