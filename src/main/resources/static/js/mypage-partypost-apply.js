$(document).ready(function () {
    getCategories();
    $('#mypartypost-apply-list').empty();
    getPartyPostApply();
});

//참석한 모집글 불러오기
function getPartyPostApply() {
    $.ajax({
        type: "GET",
        url: `/api/party-posts/my-join-list`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        error(error, response) {
            alert(response.msg)
            console.log(error)
            console.log(response)
        },
        success: function (response) {
            console.log(response.data)
            if (response.code === 200) {
                let rows = response['data']
                for (let i = 0; i < rows.length; i++) {
                    let partyPostId = response['data'][i]['id'];
                    let title = response['data'][i]['title'];
                    let closeDate = response['data'][i]['closeDate'];
                    let address = response['data'][i]['address'];
                    let status = response['data'][i]['status']

                    let temp_html = `<div id="mypartypost1" class="mypartypost">
                    ${partyPostId} 글 제목 : ${title} / 마감일 : ${closeDate} / 지역 : ${address} / 모집글상태 : ${status}
                    <button class="btn btn-warning rounded-pill" onclick="window.location.href= 'login.html'">글이동</button>
                    <button class="btn btn-dark rounded-pill">신청취소</button>
                </div>`

                    $('#mypartypost-apply-list').append(temp_html)
                }
            }

        }
    })
}

// 상단 카테고리 메뉴 불러오기
function getCategories() {
    $.ajax({
        type: "GET",
        url: `/api/categories`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        error(error, response) {
            alert(response.msg)
            console.error(error);
            console.error(response);
        },
        success: function (response) {
            console.log(response.data)
            if (response.code === 200) {
                let rows = response['data']
                for (let i = 0; i < rows.length; i++) {
                    let category_name = rows[i]['name']
                    let category_id = rows[i]['id']
                    let category_temp = `<li class="nav-item"><a class="nav-link" onclick=""><hidden>${category_id}</hidden> ${category_name} |</a></li>`

                    $('#categories').append(category_temp)
                }
            }
        }

    });
}