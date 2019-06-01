<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <dl>
            <dt>Личные качества:</dt>
            <dd>
                <textarea rows="3" cols="100" name="${SectionType.PERSONAL}">${resume.getSection(SectionType.PERSONAL).toString()}</textarea>
            </dd>
        </dl>

        <dl>
            <dt>Позиция:</dt>
            <dd>
                <textarea rows="3" cols="100" name="${SectionType.OBJECTIVE}">${resume.getSection(SectionType.OBJECTIVE).toString()}</textarea>
            </dd>
        </dl>

        <dl>
            <dt>Достижения:</dt>
            <dd>
                <textarea rows="10" cols="150" name="${SectionType.ACHIEVEMENT}">${SectionType.printList(resume.getSection(SectionType.ACHIEVEMENT))}</textarea>
            </dd>
        </dl>

        <dl>
            <dt>Квалификация:</dt>
            <dd>
                <textarea rows="10" cols="150" name="${SectionType.QUALIFICATIONS}">${SectionType.printList(resume.getSection(SectionType.QUALIFICATIONS))}</textarea>
            </dd>
        </dl>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>