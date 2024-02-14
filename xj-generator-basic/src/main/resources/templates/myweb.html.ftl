<!DOCTYPE html>
<html>
<head>
    <title>xj官网</title>
</head>
<body>
<h1>欢迎来到xj官网</h1>
<ul>
    <#--循环渲染导航条-->
    <#list menuItems as item>
        <li><a href="${item.url}">${item.label}</a></li>
    </#list>
</ul>
<#--底部版权信息-->
<footer>
    ${currentYear} xj官网 ALL rights reserved.
</footer>
</body>
</html>