<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.MemCandidateStore" %>
<%@ page import="ru.job4j.dream.store.PsqlCandidateStore" %>
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
    <script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>
    <script>
        $(document).ready(function(){
            var photo = $('#photo');
            if(photo.attr('src').endsWith(null)) {
                photo.hide();
            }
        });

        function show(input) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $('#photo').attr('src', e.target.result);
            }
            reader.readAsDataURL(input.files[0]);
            $('#photo').show();
        }

        function validate() {
            if(document.getElementById('name').value === '' || document.getElementById('city').value === '') {
                alert('Please enter the candidate name and city!');
                return false;
            } else {
                return true;
            }
        }

        function getCities() {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/dreamjob/city',
                dataType: 'json',
                async: false
            }).done(function (data) {
                var jsonResp = JSON.parse(JSON.stringify(data));
                var predefinedCity = $('#cityIdHolder').val();
                if (predefinedCity !== 'null') {
                    $('#city').attr('value', predefinedCity);
                }
                for (var i in jsonResp.cities) {
                    $('#cities').append('<option value="' + jsonResp.cities[i] + '">');
                }
            }).fail(function (err) {
                alert(err);
            });
        }
    </script>
</head>
<body onload="getCities()">
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "");
    if (id != null) {
        candidate = (Candidate) PsqlCandidateStore.instOf().findById(Integer.valueOf(id));
    }
%>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <ul class="nav">
                    <li><% if (id == null) { %>
                        Новый кандидат
                        <% } else { %>
                        Редактирование кандидата
                        <% } %></li>
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
                    <li><a href="<%=request.getContextPath()%>/index.do">На главную</a></li>
                </ul>
            </div>
            <div class="card-body">
                <form id="can" action="<%=request.getContextPath()%>/upload.do?id=<%=candidate.getId()%>&photoId=<%=request.getAttribute("photoId") == null? request.getParameter("photoId"):request.getAttribute("photoId")%>" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="name">Имя: </label>
                        <input id="name" type="text" class="form-control" name="name" value="<%=candidate.getName()%>">
                        <label for="city">Город: </label>
                        <input id="city" list="cities" class="form-control" name="city" placeholder="Укажите свой город">
                        <input type="hidden" id="cityIdHolder" value="<%=candidate.getCityId()%>">
                        <datalist id="cities">
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label for="file">Загрузите фото: </label>
                        <input id="file" class="fileInput" type="file" name="file" onchange="show(this)">
                        <img id="photo" align="middle" src="<%=request.getContextPath()%>/download?photoId=<%=request.getAttribute("photoId") == null? request.getParameter("photoId"):request.getAttribute("photoId")%>" width="200px" height="200px"/>
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>