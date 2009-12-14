<#import "structure.ftl" as layout>
<@layout.document purpose="Minimal Application">
<@layout.head>
  <script src="${controller.applicationPath}/static.directory/javascripts/jquery.js" type="text/javascript"></script>
  <script src="${controller.applicationPath}/static.directory/javascripts/landing.js" type="text/javascript"></script>
</@layout.head>
<@layout.body>
  <p id="message"></p>
</@layout.body>
</@layout.document>
<#-- vim: set ts=2 sw=2 tw=0 nowrap: -->
