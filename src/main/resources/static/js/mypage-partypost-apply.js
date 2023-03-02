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
                    <button class="btn btn-warning rounded-pill" onclick="movePost(${partyPostId})">글이동</button>
                    <button class="btn btn-dark rounded-pill">신청취소</button>
                </div>`

                    $('#mypartypost-apply-list').append(temp_html)
                }
            }

        }
    })
}

function movePost(postId) {
    window.location.href = `/page/partypost?partypostId=${postId}`;
}