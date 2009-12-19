
$(document).ready(function () {
    var grid;
    var data = [];
    var options = {
        editable: false,
        enableAddRow: false,
        enableCellNavigation: false
    };  
    var id = /\/(\d+)$/.exec(location.pathname)[1];
    $.get('../columns/' + id, {}, function (results) {
        var columns = [];
        for (var i = 0; i < results.columns.length; i++) {
            var column = results.columns[i];
            columns.push({
                name: column.name,
                renderOnResize: true,
                resizable: true, 
                id: "column_" + column.id,
                field: "column_" + column.id,
                evaluation: eval('(function (entry) { return (' + column.evaluation + ')})')
            });
            var showing = true;
            var index = 2;
        }
        $('#grid').height($(window).height() - 64);
        $.get('../entries/' + results.log.id, {}, function (entries) {
            var rows = [];
            var lines = entries.split("\n");
            for (var i = 0; i < lines.length; i++) {
                try {
                    var line = lines[i].replace(/^\\s+/, '').replace(/\\s+$/, '');
                    if (line.length != 0) {
                        var entry = eval('(' + line + ')');
                        var row = {};
                        for (var j = 0; j < columns.length; j++) {
                            var column = columns[j];
                            row[column.field] = column.evaluation(entry);
                        }
                        rows.push(row);
                    }
                } catch (e) {
                    console.log("Failure: " + lines[i] + ", " + e);
                }
            }
            console.log(rows);
            new SlickGrid($('#grid'), rows, columns, options); 
            $("#grid").resizable({ minWidth: 908, maxWidth: 908 });
        });
    }, 'json');
});
