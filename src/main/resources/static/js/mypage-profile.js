
//페이지 시작 시 호출 함수
jQuery(document).ready(function () {
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

//프로필 유저 정보 수정하기
function edit_profile() {
    $('#editProfile').empty()

    $.ajax({
        type: "PATCH",
        url: `/api/users/profile`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        data: JSON.stringify({
            profileImg: profileImg, nickname: nickname, comment: comment,
            newpassword: newpassword, confirmpassword: confirmpassword, nowpassword: nowpassword
        }),
        success: function (response, status, xhr) {
            if (response === 'success') {
                let host = window.location.host;
                let url = host + '/api/index';

                document.cookie =
                    'Authorization' + '=' + xhr.getResponseHeader('Authorization') + ';path=/';
                window.location.href = 'http://' + url;
            } else {
                alert('프로필 수정 적용을 실패했습니다')
                window.location.reload();
            }

            let profileImg = $('#image-input-text').val();
            let nickname = $('#nickname-input-text').val();
            let comment = $('#comment-input-text').val();
            let newpassword = $('#new-password').val();
            let confirmpassword = $('#confirm-password').val();
            let nowpassword = $('#now-password').val();
            //빈 입력값 확인, 수정비번-재확인비번 일치확인
            if (nickname == '') {
                alert('닉네임를 입력해주세요');
                return;
            } else if (nowpassword == '') {
                alert('현재 비밀번호를 입력해주세요');
                return;
            } else if (newpassword == confirmpassword) {
                alert('수정 비밀번호와 확인 비밀번호가 불일치합니다.');
                return;
            }
        }
    })
}
