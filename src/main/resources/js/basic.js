function getToken() {
    let cName = 'Authorization' + '=';
    let cookieData = document.cookie;
    let cookie = cookieData.indexOf('Authorization');
    let auth = '';
    if (cookie !== -1) {
        cookie += cName.length;
        let end = cookieData.indexOf(';', cookie);
        if (end === -1) end = cookieData.length;
        auth = cookieData.substring(cookie, end);
    }

    // kakao 로그인 사용한 경우 Bearer 추가
    if (auth.indexOf('Bearer') === -1 && auth !== '') {
        auth = 'Bearer ' + auth;
    }

    return auth;
}

//회원가입 페이지로 이동
function singuppage(ev) {
    let host = window.location.host;
    let url = host + '/page/signup';
    window.location.href = 'http://' + url;
}

//로그인 가입 페이지로 이동
function loginpage(ev) {
    let host = window.location.host;
    let url = host + '/page/loginPage';
    window.location.href = 'http://' + url;
}


//로그인 가입 페이지로 이동
function MyPageProfilePage(ev) {
    let host = window.location.host;
    let url = host + '/page/MyPageProfilePage';
    window.location.href = 'http://' + url;
}

function categoryFoodPage(ev) {
    let host = window.location.host;
    let url = host + '/page/category_foodPage';
    window.location.href = 'http://' + url;
}

function categorySturdyPage(ev) {
    let host = window.location.host;
    let url = host + '/page/category_sturdyPage';
    window.location.href = 'http://' + url;
}

function categoryGamePage(ev) {
    let host = window.location.host;
    let url = host + '/page/category_gamePage';
    window.location.href = 'http://' + url;
}

function categoryEntertaimentPage(ev) {
    let host = window.location.host;
    let url = host + '/page/category_entertainmentPage';
    window.location.href = 'http://' + url;
}