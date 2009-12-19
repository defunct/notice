package com.goodworkalan.prattle.viewer.controller;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.prattle.viewer.model.Grid;
import com.google.inject.Inject;

@Actors(value = InfusionActor.class)
public class GridView extends FreemarkerController {
    private Grid grid;

    @Inject
    public GridView(NamedValues namedValues, HttpServletRequest request) {
        super(namedValues, request);
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
    
    public Grid getGrid() {
        return grid;
    }
}
