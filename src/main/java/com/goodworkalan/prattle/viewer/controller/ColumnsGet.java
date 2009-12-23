package com.goodworkalan.prattle.viewer.controller;

import org.json.simple.JSONValue;

import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.stream.Output;
import com.goodworkalan.prattle.Entry;
import com.goodworkalan.prattle.viewer.model.Grid;

// FIXME Rename GridGet.
@Actors(InfusionActor.class)
public class ColumnsGet {
    private Grid grid;

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    @Output(contentType = "application/json")
    public CharSequence text() {
        return JSONValue.toJSONString(Entry.flatten(grid, "filters", "columns", "log"));
    }
}
