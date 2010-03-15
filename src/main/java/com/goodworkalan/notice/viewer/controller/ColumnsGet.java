package com.goodworkalan.notice.viewer.controller;

import org.json.simple.JSONValue;

import com.goodworkalan.diffuse.Diffuse;
import com.goodworkalan.notice.viewer.model.Grid;
import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.stream.Output;

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
        return JSONValue.toJSONString(Diffuse.flatten(grid, "filters", "columns", "log"));
    }
}
