<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Работа мечты</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>
    <script>
        $(document).ready(function(){
            $('.photo').show().filter(function(){
                return $(this).attr('src').endsWith(null);
            }).parents('td').replaceWith('<td><p>Без фото</p></td>');
        });
    </script>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <ul class="nav">
                    <li>Кандидаты</li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">
                            <c:choose>
                                <c:when test="${user.name == null}">
                                    Авторизоваться
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${user.name}"/> | Выйти
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Имена</th>
                        <th scope="col">Город</th>
                        <th scope="col">Фото</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${candidates}" var="candidate">
                    <tr>
                        <td>
                            <a href='<c:url value="/candidate/edit.jsp?id=${candidate.id}&photoId=${candidate.photoId}"/>'>
                               <i class="fa fa-edit mr-3"></i>
                            </a>
                            <c:out value="${candidate.name}"/>
                            <br>
                            <a href='<c:url value="/delete?photoId=${candidate.photoId}&id=${candidate.id}"/>'>(удалить кандидата)</a>
                        </td>
                        <td>
                            <c:out value="${candidate.cityId}"/>
                        </td>
                        <td>
                            <img class="photo" src="<c:url value='/download?photoId=${candidate.photoId}'/>" width="100px" height="100px"/>
                            <a href="<c:url value='/download?photoId=${candidate.photoId}'/>">(скачать фото)</a>
                        </td>
                    </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
