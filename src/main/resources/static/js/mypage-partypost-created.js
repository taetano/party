$(document).ready(function () {
    getCategories();
    $('#mypartypost-created-list').empty();
    getPartyPostCreated();

});

function getPartyPostCreated() {
    $.ajax({
        type: "GET",
        url: `/api/party-posts/mylist`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        error(error, response) {
            alert(response.msg)
            console.log(error)
            console.log(response)
        },
        success: function (response) {
            console.log("받아왔음" + response.data)
            if (response.code === 200) {
                let rows = response['data']

                for (let i = 0; i < rows.length; i++) {
                    let temp_html = `<div id="mypartypost" class="mypartypost"> </div>`
                    $('#mypartypost-created-list').append(temp_html)

                    let partyPostId = response['data'][i]['id'];
                    let title = response['data'][i]['title'];
                    let closeDate = response['data'][i]['closeDate'];
                    let address = response['data'][i]['address'];
                    let status = response['data'][i]['status']

                    let partypost_temp_html = ` <div id = "createdPartyPostInfo"> <strong> 글 제목 : [${partyPostId}] ${title} <br> 마감일자 : ${closeDate} / 위치 : ${address} / 모집 상태 : ${status}</strong> </div>`
                    $('#mypartypost').append(partypost_temp_html)


                    let joinMemberRows = response['data'][i]['joinMember']
                    for (let j = 0; j < joinMemberRows.length; j++) {
                        let applicationId = joinMemberRows[j]['id']
                        let memberNickname = joinMemberRows[j]['nickname']
                        let memberStatus = joinMemberRows[j]['status']
                        let noShowCnt = joinMemberRows[j]['noShowCnt']

                        let partypost_member_temp_html = `
                        <div class="user" id="user">참여자 -  [${applicationId}] ${memberNickname} / 신청 상태 : ${memberStatus} / 노쇼포인트 : ${noShowCnt}
                        <button class="btn btn-primary rounded-pill" onclick="applicationAccept(${applicationId})">수락</button>
                        <button class="btn btn-dark rounded-pill" onclick="applicationReject(${applicationId})">거절</button>
                    </div>`

                        let partypost_member_notPending_temp_html = `
                        <div class="user" id="user">참여자 -  [${applicationId}] ${memberNickname} / 신청 상태 : ${memberStatus} / 노쇼포인트 : ${noShowCnt}
                    </div>`

                        if (memberStatus === 'PENDING') {
                            $('#createdPartyPostInfo').append(partypost_member_temp_html)
                        } else {
                            $('#createdPartyPostInfo').append(partypost_member_notPending_temp_html)
                        }


                    }


                }
            }

        }
    })

}

//수락버튼 함수
function applicationAccept(applicationId) {
    $.ajax({
        type: "POST",
        url: `/api/applications/accept/${applicationId}`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        error(error, response) {
            alert(response.msg)
            console.log(error)
            console.log(response)
        },
        success: function (response) {
            alert(response.msg)
            window.location.reload()
        }
    })
}

//거절버튼 함수
function applicationReject(applicationId) {
    $.ajax({
        type: "POST",
        url: `/api/applications/reject/${applicationId}`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        error(error, response) {
            alert(response.msg)
            console.log(error)
            console.log(response)
        },
        success: function (response) {
            alert(response.msg)
            window.location.reload()

        }
    })
}
