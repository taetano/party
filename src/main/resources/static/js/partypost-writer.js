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

//페이지 로딩될때 실행되는 함수입니다
$(document).ready(function () {
    get_category();

    // timepicker 옵션 설정
    $("#time").timepicker({
        timeFormat: 'HH:mm',
        interval: 60,
        minTime: '7',
        maxTime: '22:00',
        defaultTime: '11',
        startTime: '07:00',
        dynamic: false,
        dropdown: true,
        scrollbar: false
    });

    // 날짜 선택 제한
    // 현재 시간 이전은 선택할 수 없도록 제한
    const now = new Date();
    const currentDate = now.toISOString().slice(0, 10);
    document.getElementById("date").setAttribute("min", currentDate);

    // 오늘부터 일주일 뒤 까지 선택 가능하도록 제한
    const weekLater = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);
    const weekLaterDate = weekLater.toISOString().slice(0, 10);
    document.getElementById("date").setAttribute("max", weekLaterDate);

    // //카카오 지도 관련
    const container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
    mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        level: 3
    };
});

// 취소 버튼 클릭 시 이전 페이지로 이동
function cancel() {
    window.history.back();
}

// json 만들어서 보내는 함수 만들기
function sendPartyPost() {
    $.ajax(
        {
            type: "POST",
            url: `/api/party-posts`,
            headers: {
                "Authorization": getCookieValue('Authorization')
            },
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                "title": $('#title').val(),
                "content": $('#content').val(),
                "categoryId": $('#category').val(),
                "maxMember": $('#max-member').val(),
                "partyDate": $('#date').val() + ' ' + $('#time').val(),
                "partyAddress": $('#place-address').val(),
                "partyPlace": $('#place-name').val()
            }),
            success: function (json) {
                console.log("갔어요")
                console.log(json)
                alert(json.msg)
            },
            error(error, status, request) {
                console.log("안갔어요")
                console.error(error)
            }
        })
}

//카테고리 가져오기
function get_category() {
    $.ajax({
        type: "GET",
        url: `/api/categories`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        error(error) {
            console.error(error);
        },
        success: function (response) {
            console.log(response.data)
            let rows = response['data']
            for (let i = 0; i < rows.length; i++) {
                console.log(response)
                console.log(rows)
                let category_name = rows[i]['name']
                let category_temp = `<option value= ${i}> ${category_name} </option>`

                $('#category').append(category_temp)
            }
        }

    });
}