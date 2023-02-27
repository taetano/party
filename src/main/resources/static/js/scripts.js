//쿠키값 가져오는 함수(cookieName 자리에 Authorization사용)
function getCookieValue(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieParts = decodedCookie.split(';');
    for (var i = 0; i < cookieParts.length; i++) {
        var cookiePart = cookieParts[i].trim();
        if (cookiePart.indexOf(name) === 0) {
            return cookiePart.substring(name.length, cookiePart.length);
        }
    }
    return "";
}

// 로그아웃 메소드
function logout() {
    $.ajax({
        type: "POST",
        url: `/api/users/signout`,
        contentType: "application/json",
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        success: function () {
            document.cookie = 'Authorization' + '=' + ""
            let host = window.location.host;
            let url = host + '/page/indexPage';
            window.location.href = 'http://' + url;

        }
    })

}