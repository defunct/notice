package com.goodworkalan.prattle.viewer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A column in a grid view of a log.
 * 
 * @author Alan Gutierrez
 */
@Entity
@Table(name = "col")
public class Column {
    /** The entity id. */
    private long id;

    /** The grid. */
    private Grid grid;

    /** The grid name. */
    private String name;

    /** The JavaScript to evaluate to get the value. */
    private String evaluation;

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
     * Get the grid.
     * 
     * @return The grid.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public Grid getGrid() {
        return grid;
    }

    /**
     * Set the grid.
     * 
     * @param grid
     *            The grid.
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    /**
     * Get the column name.
     * 
     * @return The column name.
     */
    @javax.persistence.Column(nullable = false)
    public String getName() {
        return name;
    }

    /**
     * Set the column name.
     * 
     * @param name
     *            The column name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get The JavaScript to evaluate to get the value.
     * 
     * @return The JavaScript to evaluate to get the value.
     */
    @javax.persistence.Column(nullable = false)
    public String getEvaluation() {
        return evaluation;
    }

    /**
     * Set the JavaScript to evaluate to get the value.
     * 
     * @param evaluation
     *            The JavaScript to evaluate to get the value.
     */
    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}
