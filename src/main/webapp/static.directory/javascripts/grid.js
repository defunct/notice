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
            lines = null;
            for (var i = 0; i < entries.length; i++) {
                var entry = entries[i];
                var row = {};
                for (var j = 0; j < columns.length; j++) {
                    var column = columns[j];
                    row[column.field] = column.evaluation(entry);
                }
                rows.push(row);
            }
            function refreshRows(col) {
                for (var i = 0; i < entries.length; i++) {
                    rows[i][col.field] = col.evaluation(entries[i]);
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
            $('#add a').click(function () {
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
        });
    }, 'json');
});
