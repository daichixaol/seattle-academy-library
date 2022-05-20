<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>貸し出し履歴一覧｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="resources/css/lightbox.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/lightbox.js" /></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
                <table class="table table-bordered mx-auto" style="width:50%">
                    <thead>
                        <tr class="bg-info">
                            <th>書籍名</th>
                            <th>貸出日</th>
                            <th>返却日</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="historyInfo" items="${historyList}">
                            <tr>
                                <td>
                                    <form method="post" class="book_title" action="<%=request.getContextPath()%>/details">
                                        <a href="javascript:void(0)" onclick="this.parentNode.submit();"> <src="${historyInfo.title}">${historyInfo.title}</a> <input type="hidden" name="bookId" value="${historyInfo.bookId}">
                                    </form>
                                </td>
                                <td>${historyInfo.lentDate}</td>
                                <td>${historyInfo.returnDate}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
        </form>
    </main>
</body>
</html>