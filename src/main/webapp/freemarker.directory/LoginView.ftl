<#import "structure.ftl" as layout>
<@layout.document purpose="Prattle Viewer Login">
<@layout.head>
</@layout.head>
<@layout.body>
  <form method="POST">
    <input type="password" name="password"><input type="submit" value="Log In">
  </form>
</@layout.body>
</@layout.document>
<#-- vim: set ts=2 sw=2 tw=0 nowrap: -->
