<#macro document purpose>
<#assign purpose=purpose/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <#nested>
</html>
</#macro>

<#macro head>
<head>
  <title>${purpose}</title>
  <meta name="COPYRIGHT" CONTENT="Copyright 1999-2009 (c) by Alan Gutierrez">
  <meta name="AUTHOR" CONTENT="Alan Gutierrez">
  <meta name="robots" CONTENT="index,follow">
  <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
  <link rel="stylesheet" type="text/css" media="screen" href="${controller.applicationPath}/static.directory/stylesheets/screen.css">
  <#nested>
</head>
</#macro>

<#macro body>
<body>
  <#nested>
</body>
</#macro>
<#-- vim: set nowrap ts=2 sw=2 et tw=0 nowrap: -->
