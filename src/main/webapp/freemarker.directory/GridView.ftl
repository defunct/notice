<#import "structure.ftl" as layout>
<@layout.document purpose="Minimal Application">
<@layout.head>
  <style></style>
  <script src="${controller.applicationPath}/static.directory/javascripts/jquery.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/ui.core.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/ui.sortable.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/ui.resizable.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/jquery.getScrollbarWidth.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/jquery.rule.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/slick.globaleditorlock.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/slick.grid.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/slick.model.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/date.format.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/firebugx.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/grid.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/jquery.cookie.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/splitter.js" type="text/javascript"></script>
  <link href="${controller.applicationPath}/static.directory/stylesheets/smoothness/jquery-ui-1.7.2.custom.css" media="print, screen, projection" rel="stylesheet" type="text/css">
  <link href="${controller.applicationPath}/static.directory/stylesheets/slick.grid.css" media="print, screen, projection" rel="stylesheet" type="text/css">
</@layout.head>
<@layout.body>
  <div><a id="add_column" href="#">Add Column</a> | <a id="add_filter" href="#">Add Filter</a></div>
  <div id="filter_editor">
    <label>
      <span class="label">Expression</span>
      <span class="control"><textarea id="filter_expression" rows="4" cols="40"></textarea></span>
    </label>
    <div class="buttons">
      <button id="filter_delete">Delete</button>
      <button id="filter_edit_preview">Preview</button>
      <button id="filter_edit_ok">OK</button>
      <button id="filter_edit_cancel">Cancel</button>
    </div>
  </div>
  <div id="filters"></div>
  <div id="column_error"></div> 
  <div id="column_editor">
    <label>
      <span class="label">Column Name</span>
      <span class="control"><input id="column_name"></span>
    </label>
    <label>
      <span class="label">Expression</span>
      <span class="control"><textarea id="expression" rows="4" cols="40"></textarea></span>
    </label>
    <div class="buttons">
      <button id="column_delete">Delete</button>
      <button id="column_edit_preview">Preview</button>
      <button id="column_edit_ok">OK</button>
      <button id="column_edit_cancel">Cancel</button>
    </div>
  </div>
  <div id="splitter">
    <div id="entries"><div class="grid"></div></div>
    <div id="inspector"><div class="grid"></div></div>
  </div>
</@layout.body>
</@layout.document>
<#-- vim: set ts=2 sw=2 tw=0 nowrap: -->