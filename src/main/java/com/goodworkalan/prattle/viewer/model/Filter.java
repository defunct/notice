package com.goodworkalan.prattle.viewer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Filter {
    /** The entity id. */
    private long id;
    
    /** The grid. */
    private Grid grid;
    
    /** The JavaScript filter expression. */
    private String expression;
    
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
     * Get the JavaScript filter expression.
     * 
     * @return The JavaScript filter expression.
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Set the JavaScript filter expression.
     * 
     * @param expression
     *            The JavaScript filter expression.
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }
}
