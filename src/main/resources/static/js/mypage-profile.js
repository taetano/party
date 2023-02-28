
//페이지 시작 시 호출 함수
jQuery(document).ready(function () {
    getCategories();
    get_profile();
});

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

//프로필 유저 정보 가져오기
function get_profile() {
    $('#profile').empty()

    $.ajax({
        type: "GET",
        url:'/api/users/profile',
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        success: function (response) {
            let responseData = response['data']
            console.log(response)
            let nickname = responseData['nickName']
            let comment = responseData['comment']
            let profileImg = responseData['profileImg'] //프로필 사진
            let email = responseData['email']
            let phoneNum = responseData['phoneNum']
            let tempHtml = `
                
            <!-- 여기부터 본문 -->
            <div class="mypage-info">
                <div class="profile-profileImg">
                    <span id="profile-profileImg">프로필 이미지: ${profileImg}</span>
                    <!-- 유저 프로필 이미지 들어갈 곳 -->
<!--                    <img src="../static/css/image/person.png" width=100 height=80>-->

<!--            //프로필 이미지가 없을 때-->
<!--            profileUrl = (profileUrl ==null)? "../static/css/image/person.png":profileUrl;// 기본 프로필 이미지-->

                </div>
                <div class="profile-nickname">
                    <!-- 유저 이름 들어갈 곳 -->
                    <span id="profile-nickname">유저 닉네임: ${nickname}</span>
                    <br>
                    <!-- 상태메세지 들어갈 곳 -->
                    <span id="profile-comment">상태메세지: ${comment}</span>
                    
<!--                    //소개글이 없을git p 때-->
<!--                    if (comment == null) {-->
<!--                        comment = "소개글이 없습니다"-->
<!--                    }-->
            
                </div>
                <div>이메일: ${email}</div>
                <!-- 유저 이메일 들어갈 곳 -->
                <span id="profile-email"></span>
                <br>
                <div>전화번호: ${phoneNum}</div>
                    <!-- 유저 전화번호 들어갈 곳 -->
                <span id="profile-phoneNum"></span>
            </div>
                `

            console.log("프로필정보" + nickname, comment, profileImg, email, phoneNum)

            $('#profile').append(tempHtml)
        }
    });
}

//프로필 수정하기
function edit_profile() {
    $.ajax({
        type: "PATCH",
        url: `/api/users/profile`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify({
            "profileImg": $('#profileImg').val(),
            "nickname": $('#nickname').val(),
            "comment": $('#comment').val(),
            "phoneNum": $('#phoneNumber').val()
    }),
        success: function (response) {
            console.log(response);
            alert('성공적으로 수정되었습니다.');
            window.location.reload();
        },
        error(error, status, request) {
            console.error(error);
        }
    });
}