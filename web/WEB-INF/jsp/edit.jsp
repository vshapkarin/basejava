<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.PrintSectionToHtml" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
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
        <c:forEach var="type" items="${ContactType.values()}">
            <c:set var="contact" value="${resume.getContact(type)}"/>
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${contact == null ? "" : contact}"></dd>
            </dl>
        </c:forEach>
        <c:forEach var="type" items="${SectionType.values()}">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <dl>
                <dd>
                <dt><h3><b>${type.title}:</b></h3></dt>
                <c:choose>
                    <c:when test="${type == SectionType.PERSONAL || type == SectionType.OBJECTIVE}">
                        <textarea rows="3" cols="100"
                                  name="${type}">${section == null ? "" :  section.toString()}</textarea>
                    </c:when>
                    <c:when test="${type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS}">
                        <textarea rows="10" cols="150"
                                  name="${type}">${section == null ? "" : PrintSectionToHtml.listToString(section)}</textarea>
                    </c:when>
                    <c:when test="${type == SectionType.EXPERIENCE || type == SectionType.EDUCATION}">
                        <input hidden name="${type}" value="0"/>
                        <br>
                        <c:forEach var="organisation" items="${section.content}"
                                   varStatus="organisationID">
                            <c:set var="url" value="${organisation.homePage.url}"/>
                            <table style="border:1px grey solid;background: lavender;">
                                <tr>
                                    <td colspan="2"><input type="text" size="51" name="${type} organisationName"
                                                           value="${organisation.homePage.name}"></td>
                                    <td><input type="text" name="${type} url" value="${url == null ? "" : url}"></td>
                                </tr>
                                <c:forEach var="period" items="${organisation.periods}" varStatus="periodID">
                                    <c:set var="start" value="${period.start}"/>
                                    <c:set var="end" value="${period.end}"/>
                                    <c:set var="optionalText" value="${period.optionalText}"/>
                                    <tr>
                                        <td>
                                            <input type="date"
                                                   name="${type} organisationID=${organisationID.index} startDate"
                                                   value="${start == DateUtil.NOW ? "" : start}"><br>
                                            <input type="date"
                                                   name="${type} organisationID=${organisationID.index} endDate"
                                                   value="${end == DateUtil.NOW ? "" : end}">
                                        </td>
                                        <td><textarea rows="3" cols="31"
                                                      name="${type} organisationID=${organisationID.index} text">${period.text}</textarea>
                                        </td>
                                        <td><textarea rows="3" cols="88"
                                                      name="${type} organisationID=${organisationID.index} optionalText">${optionalText == null ? "" : optionalText}</textarea>
                                        </td>
                                        <td>
                                            <button type="submit" name="deletePeriod"
                                                    value="${type} organisationID=${organisationID.index} periodID=${periodID.index}">
                                                <img src="img/delete.png"></button>
                                        </td>
                                    <tr/>
                                </c:forEach>
                                <tr>
                                    <td colspan="2">
                                        <button type="submit" name="addPeriod" style="width: 145px"
                                                value="${type} organisationID=${organisationID.index}">Добавить период
                                        </button>
                                        <button type="submit" name="deleteOrganisation" style="background: lightpink"
                                                value="${type} organisationID=${organisationID.index}">Удалить организацию
                                        </button>
                                    </td>
                                </tr>
                            </table>
                            <br>
                        </c:forEach>
                        <button type="submit" name="addOrganisation" value="${type}">Добавить организацию</button>
                    </c:when>
                </c:choose>
                </dd>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>