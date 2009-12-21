$(document).ready(function () {
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
    function newColumn(name, id, expression) {
        if (name.evaluation) {
            var column = name;
            return newColumn(column.name, column.id, column.evaluation);
        }
        return {
            name: name,                  // display name.
            renderOnResize: true,               
            resizable: true, 
            id: "column_" + id,          // API identifier
            field: "column_" + id,       // bound data field
            expression: expression,
            evaluation: evaluation(expression)
        };
    }
    var gridId = /\/(\d+)$/.exec(location.pathname)[1];
    $.get('../columns/' + gridId, {}, function (results) {
        var columns = [];
        for (var i = 0; i < results.columns.length; i++) {
            var column = results.columns[i];
            columns.push(newColumn(results.columns[i]));
        }
        $('#grid').height($(window).height() - 64);
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
            var filterIds = [];
            function addFilter(expression) {
                try {
                    filters.push(eval('(function () { return function (entry) { return (' + expression + ') } })()'));
                } catch (e) {
                    $('#filter_error').update(e.message);
                    return false;
                }
                return true;
            }
            for (var i = 0; i < results.filters.length; i++) {
                var f = results.filters[i];
                addFilter(f.expression);
                filterIds.push(f.id);
                $('<div id="filter_' + f.id + '" class="filter"><a href="#"><code><pre>' + f.expression + '</pre></code></a></div>')
                    .appendTo('#filters');
            }
            lines = null;
            function filter() {
                rows.length = 0;
                ENTRY: for (var i = 0; i < entries.length; i++) {
                    var entry = entries[i];
                    var row = {};
                    for (var j = 0; j < filters.length; j++) {
                        if (!filters[j](entry)) {
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
                ENTRY: for (var i = 0; i < entries.length; i++) {
                    for (var j = 0; j < filters.length; j++) {
                        if (!filters[j](entry)) {
                            continue ENTRY;
                        }
                    }
                    rows[k++][col.field] = col.evaluation(entries[i]);
                }
                grid.removeAllRows();
                grid.render();
            }
            console.log(rows);
            grid = new SlickGrid($('#grid'), rows, columns, options); 
            $("#grid").resizable({ minWidth: 908, maxWidth: 908 });
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
                if (addFilter(expression)) {
                    filterAndDisplay();
                    filters.pop();
                }
            });
            $('#filter_edit_ok').click(function () {
                var expression = $('#filter_expression').val();
                if (addFilter(expression)) {
                    filterAndDisplay();
                    if (adding) {
                        $.post('../filters/add', {
                            'filter[grid][id]': gridId,
                            'filter[expression]': expression
                        }, function (response) {
                            filterIds.push(response.filter.id);
                        }, 'json');
                    }
                    $('#filter_editor').hide('fast');
                }
            });
            $('#filter_edit_cancel').click(function () {
                filterAndDisplay();
                $('#filter_editor').hide('fast');
            });
            $('#add_filter').click(function () {
                adding = true;
                $('#filter_editor').show('fast');
                return false;
            });
        });
    }, 'json');
});
