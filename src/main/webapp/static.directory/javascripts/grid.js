$(document).ready(function () {
    $("#splitter").splitter({
        type: "v",
        outline: true,
        minLeft: 100, sizeLeft: 150, minRight: 100,
        resizeToWidth: true,
        cookie: "vsplitter",
        accessKey: 'I'
    });
    var grid;
    var data = [];
    var options = {
        editable: false,
        enableAddRow: false,
        enableCellNavigation: false
    };  
    function evaluation(e) {
        return eval('(function() { return function (entry) { return (' + e + ') } })()');
    }
    var prototypeColumn = {
        renderOnResize: true,
        resizable: true
    };
    function newColumn(name, id, expression) {
        if (name.evaluation) {
            var column = name;
            return newColumn(column.name, column.id, column.evaluation);
        }
        return $.extend({
            name: name,                  // display name.
            id: "column_" + id,          // API identifier
            field: "column_" + id,       // bound data field
            expression: expression,
            evaluation: evaluation(expression)
        }, prototypeColumn);
    }
    var gridId = /\/(\d+)$/.exec(location.pathname)[1];
    $.get('../columns/' + gridId, {}, function (results) {
        var columns = [];
        for (var i = 0; i < results.columns.length; i++) {
            var column = results.columns[i];
            columns.push(newColumn(results.columns[i]));
        }
        $.get('../entries/' + results.log.id, {}, function (entries) {
            var rows = [];
            var lines = entries.split("\n");
            var entries = [];
            for (var i = 0; i < lines.length; i++) {
                var line = lines[i].replace(/^\\s+/, '').replace(/\\s+$/, '');
                if (line.length != 0) {
                    try {
                        entries.push(eval('(' + line + ')'));
                    } catch (e) {
                        console.log("Failure: " + lines[i] + ", " + e);
                    }
                }
            }
            var filters = [];
            function addFilter(filter) {
                try {
                    filters.push($.extend(filter, { evaluation: evaluation(filter.expression) }));
                } catch (e) {
                    $('#filter_error').update(e.message);
                    return false;
                }
                return true;
            }
            function displayFilter(filter) {
                $('<div id="filter_' + filter.id + '" class="filter"><a href="#"><code><pre>' + filter.expression + '</pre></code></a></div>')
                    .appendTo('#filters');
            }
            for (var i = 0; i < results.filters.length; i++) {
                addFilter(results.filters[i]);
                displayFilter(results.filters[i]);
            }
            lines = null;
            function filter() {
                rows.length = 0;
                var evaluations = $.map(filters, function (filter) {
                    return filter.evaluation;
                });
                ENTRY: for (var i = 0; i < entries.length; i++) {
                    var entry = entries[i];
                    var row = {};
                    for (var j = 0; j < evaluations.length; j++) {
                        if (!evaluations[j](entry)) {
                            continue ENTRY;
                        }
                    }
                    for (var j = 0; j < columns.length; j++) {
                        var column = columns[j];
                        row[column.field] = column.evaluation(entry);
                    }
                    rows.push(row);
                }
            }
            filter();
            function refreshRows(col) {
                var k = 0;
                var evaluations = $.map(filters, function (filter) {
                    return filter.evaluation;
                });
                ENTRY: for (var i = 0; i < entries.length; i++) {
                    var entry = entries[i];
                    for (var j = 0; j < evaluations.length; j++) {
                        if (!evaluations[j](entry)) {
                            continue ENTRY;
                        }
                    }
                    rows[k++][col.field] = col.evaluation(entry);
                }
                grid.removeAllRows();
                grid.render();
            }
            console.log(rows);
            grid = new SlickGrid($('#entries .grid'), rows, columns, options); 
//            $("#grid").resizable({ minWidth: 908, maxWidth: 908 });
            var editColumn = null, savedColumn = null;
            grid.onColumnHeaderClick = function (column) {
                if (editColumn == null) {
                    adding = false;
                    $('#column_name').val(column.name);
                    $('#expression').val(column.expression);
                    $('#column_editor').show('fast');
                    editColumn = column;
                    savedColumn = $.extend({}, editColumn);
                }
            }
            var preview = function (name, expression) {
                $('#column_error').hide('fast');
                var col = $.extend(editColumn, {
                    name: name,
                    expression: expression
                });

                try {
                   col.evaluation =  eval('(function () { return function (entry) { return (' + col.expression + ')} })()')
                } catch (e) {
                    $('#column_error').html(e.message).show('fast');
                    return false;
                }

                grid.setColumn({ id: col.id, name: col.name });

                refreshRows(col);

                return true;
            };
            $('#column_edit_preview').click(function () {
                preview($('#column_name').val(), $('#expression').val());
            });
            var adding = false;
            $('#column_edit_ok').click(function() {
                if (editColumn != null && preview($('#column_name').val(), $('#expression').val())) {
                    $('#column_editor').hide('fast');
                    if (adding) {
                        grid.removeColumn('column_new');
                        $.post('../columns/add', {
                            'column[grid][id]': gridId,
                            'column[name]': editColumn.name,
                            'column[evaluation]': editColumn.expression
                        }, function (response) {
                            var col = newColumn(response.column);
                            grid.addColumn(col);
                            refreshRows(col);
                        }, 'json');
                    } else {
                        $.post('../columns/save', {
                            'column[id]': /\bcolumn_(\d+)\b/.exec(editColumn.id)[1],
                            'column[name]': editColumn.name,
                            'column[evaluation]': editColumn.expression
                        }, function (response) {
                        }, 'json');
                    }
                    editColumn = null;
                }
            });
            $('#column_edit_cancel').click(function() {
                if (adding) {
                    grid.removeColumn('column_new');
                } else {
                    preview(savedColumn.name, savedColumn.expression);
                }
                $('#column_editor').hide('fast');
                editColumn = null;
            });
            $('#add_column').click(function () {
                if (editColumn == null) {
                    adding = true;
                    $('#column_name').val('New Column');
                    $('#expression').val('');
                    $('#column_editor').show('fast');
                    editColumn = newColumn('New Column', 'new', "''")
                    grid.addColumn(editColumn);
                }
                return false;
            });
            function filterAndDisplay() {
                filter();
                grid.updateRowCount();
                grid.removeAllRows();
                grid.render();
            }
            $('#filter_edit_preview').click(function () {
                var expression = $('#filter_expression').val();
                if (addFilter({id: 'temp', expression: expression})) {
                    filterAndDisplay();
                    filters.pop();
                }
            });
            var editFilter = null;
            $('#filter_edit_ok').click(function () {
                var expression = $('#filter_expression').val();
                if (adding) {
                    if (addFilter({id: 'temp', expression: expression})) {
                        $.post('../filters/add', {
                            'filter[grid][id]': gridId,
                            'filter[expression]': expression
                        }, function (response) {
                            filters[filters.length - 1].id = response.filter.id;
                            displayFilter(filters[filters.length - 1]);
                            editFilter = null;
                        }, 'json');
                    }
                } else {
                    if (addFilter({id: editFilter.id, expression: expression})) {
                        $('#filter_' + editFilter.id + ' pre').html(expression);
                        $.post('../filters/save', {
                            'filter[grid][id]': gridId,
                            'filter[id]': editFilter.id,
                            'filter[expression]': expression
                        }, function (response) {
                            editFilter = null;
                        }, 'json');
                    }
                }
                filterAndDisplay();
                $('#filter_editor').hide('fast');
            });
            $('#filter_edit_cancel').click(function () {
                if (editFilter != null) {
                    addFilter(editFilter);
                    editFilter = null;
                }
                filterAndDisplay();
                $('#filter_editor').hide('fast');
            });
            $('#add_filter').click(function () {
                adding = true;
                $('#filter_editor').show('fast');
                return false;
            });
            $('#filters').click(function (e) {
                if (editFilter == null) {
                    var link = $(e.target).parents('a');
                    if (link.size() != 0) {
                        var filterId = /^filter_(\d+)/.exec(link.parents('div').get(0).id)[1];
                        for (var i = 0; editFilter == null && i < filters.length; i++) {
                            if (filters[i].id == filterId) {
                                editFilter = filters.splice(i, 1)[0];
                            }
                        }
                        $('#filter_expression').val(editFilter.expression);
                        $('#filter_editor').show('fast');
                        adding = false;
                    }
                }
                return false;
            });
            
            var entry = [];
            var expanded = [];

            var pathFormatter  = function(row, cell, value, columnDef, record) {
                var spacer = "<span style='display:inline-block;height:1px;width:" + (15 * record.depth) + "px'></span>";
                var index = record.index;
                if (index + 1 != entry.length && entry[index + 1].depth > entry[index].depth) {
                    if (record.expanded)
                        return spacer + " <span class='toggle collapse'></span>&nbsp;" + value;
                    else
                        return spacer + " <span class='toggle expand'></span>&nbsp;" + value;
                }               
                else
                    return spacer + " <span class='toggle'></span>&nbsp;" + value;
        };

            var inspector = new SlickGrid($('#inspector .grid'), expanded, [
                $.extend({ name: "Path", id: "path", field: "path", formatter: pathFormatter }, prototypeColumn),
                $.extend({ name: "Value", id: "value", field: "value" }, prototypeColumn)
            ], options); 

            function createPropertyRow(path, value, depth, parent) {
                if      (value instanceof Object)   { child = value; value = ''; }
                else if (value instanceof Array)    { child = value; value = ''; }
                else                                { child = null; }
                entry.push({
                    path: path,             value: value,           parent: parent,
                    depth: depth,           index: entry.length,
                    displayed: depth == 0,  expanded: false
                });
                if (child) addProperty(child, depth + 1, entry.length - 1);
            }

            function addProperty(property, depth, parent) {
                if (property instanceof Object) {
                    var keys = []; for (var key in property) keys.push(key); keys = keys.sort();
                    for (var i = 0; i < keys.length; i++)
                        createPropertyRow(keys[i], property[keys[i]], depth, parent);
                } else if (property instanceof Array) {
                    for (var i = 0; i < property.length; i++)
                        createPropertyRow(i, property[i], depth, parent);
                }
            }

            function filterEntry() {
                expanded.length = 0;
                for (var i = 0; i < entry.length; i++) if (entry[i].displayed) expanded.push(entry[i]);
                inspector.updateRowCount();
                inspector.removeAllRows();
                inspector.render();
            }

            function expandProperty(row) {
                entry[row].expanded = true;
                for (var i = row + 1; i < entry.length && entry[i].depth > entry[row].depth; i++)
                    if (entry[i].depth == entry[row].depth + 1)
                        entry[i].displayed = true; 
            }

            function collapseProperty(row) {
                entry[row].expanded = false;
                var i = row + 1;
                while (i < entry.length && entry[i].depth > entry[row].depth) {
                    entry[i].displayed = false; 
                    if (entry[i].expanded) i = collapseProperty(i);
                    else i++; 
                }
                return i;
            }

            inspector.onClick = function (e, row, cell) {
                if ($(e.target).hasClass("toggle")) {
                    var expand = expanded[row];
                    if (expand.expanded) collapseProperty(expand.index);
                    else expandProperty(expand.index);
                    filterEntry();
                }
            }

            grid.onClick = function (e, row, cell) {

                // repopulate the entry array.
                entry.length = 0; addProperty(entries[row], 0, -1);

                // filter to display expanded entries.
                filterEntry();
            }
        });
    }, 'json');
});

/* vim: set nowrap tw=0: */
