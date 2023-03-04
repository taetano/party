
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
            let tempHtml = `
                
            <!-- 여기부터 본문 -->
            <div class="mypage-info">
              <div class="profile-profileImg">
                <p><strong>프로필 이미지: </strong></p>
                <img src="${profileImg}" id="profile-profileImg" width=100 height=80>
              </div>
              <div class="profile-nickname">
                <!-- 유저 닉네임 들어갈 곳 -->
                <span id="profile-nickname">유저 닉네임: ${nickname}</span>
                <br>
                <!-- 상태메세지 들어갈 곳 -->
                <span id="profile-comment">상태메세지: ${comment}</span>
              </div>
              <div>이메일: ${email}</div>
              <!-- 유저 이메일 들어갈 곳 -->
                <span id="profile-email"></span>
            </div>
                `

            console.log("프로필정보" + nickname, comment, profileImg, email)

            $('#profile').append(tempHtml)
        }
    });
}

//프로필 수정하기

$('#btn-save').on('click', editProfile);

function editProfile() {
    var file = $('#img')[0].files[0];
    console.log(file)
    var data = {"profileImg": $('#profileImg').val(),
                "nickname": $('#nickname').val(),
                "comment": $('#comment').val()
                };
    console.log(data)
    var formData = new FormData();
    formData.append('file', file);
    formData.append('dto', new Blob([JSON.stringify(data)] , {type: "application/json"}));
    console.log(formData)

    $.ajax({
        type: "POST",
        url: `/api/users/profile`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        data: formData,
        processData: false,
        contentType: false
    }).done(function (file) {
        $('#result-image').attr("src", file);
        alert("수정완료");
        window.location.href = `/page/myPage/profile`
    }).fail(function (error) {
        alert("오류가 발생했습니다.");
    })
}