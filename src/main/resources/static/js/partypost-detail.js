//페이지 시작 시 호출 함수
jQuery(document).ready(function () {
    let partypostId=new URLSearchParams(window.location.search).get('partypostId');
    if(partypostId) {
        go_to_partypost(partypostId);
    }
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

//포스트 아이디 가져오기
function go_to_partypost(postId) {
    get_partypost(postId);
}

//프로필 유저 정보 가져오기
function get_partypost(postId) {
    $('#partypost').empty()

    $.ajax({
        type: "GET",
        url:'/api/party-posts/' + postId,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        success: function (response) {
            let responseData = response['data']
            console.log(response)
            let nickname = responseData['nickname']
            let title = responseData['title']
            let content = responseData['content']
            let categoryId = responseData['categoryId']
            let status = responseData['status']
            let acceptedMember = responseData['acceptedMember']+1
            let maxMember = responseData['maxMember']
            let partyDate = responseData['partyDate']
            let closeDate = responseData['closeDate']
            let day = responseData['day']
            let address = responseData['address']
            let detailAddress = responseData['detailAddress']
            let partyPlace = responseData['partyPlace']
            let viewCnt = responseData['viewCnt']
            let joinMember = responseData['joinMember']
            let tempHtml = `

            <!-- Page Content-->
            <div id="partypost">
                <section class="py-5">
                    <div class="container px-5 my-5">
                        <div class="row gx-5">
                            <div class="col-lg-3">
                                <div class="d-flex align-items-center mt-lg-5 mb-4">
                                    <img class="img-fluid rounded-circle" src="https://dummyimage.com/50x50/ced4da/6c757d.jpg" alt="..." />
                                    <div class="ms-3">
                                        <div class="fw-bold">파티장 닉네임: ${nickname}</div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-9">
                                <!-- Post content-->
                                <article>
                                    <!-- Post header-->
                                    <header class="mb-4 mt-5">
                                        <!-- Post title-->
                                        <h1 class="fw-bolder mb-1">파티포스트 타이틀: ${title}</h1>
                                        <!-- Post meta content-->
                                        <div class="fw-bold fst-italic mb-2 fs-4 text-end">마감일자 : ${closeDate}</div>
                                        <div class="text-muted fst-italic text-end">조회수: ${viewCnt}</div>
                                        <!-- Post categories-->
                                        <a class="badge bg-secondary text-decoration-none link-light">Status: ${status}</a>
                                        <a class="badge bg-secondary text-decoration-none link-light" href="#!">카테고리 명: ${categoryId}</a>
                                    </header>
                                    <!-- Post content-->
                                    <section>
                                        <div class="card bg-light mb-3">
                                            <div class="card-body my-2 fs-2">
                                                내용: ${content}
                                            </div>
                                        </div>
                                    </section>
                                    <!-- Post feature-->
                                    <section class="mb-5">
                                        <a class="badge bg-secondary text-decoration-none link-light p-sm-3 rounded-pill" href="#!">신고</a>
                                        <a class="badge bg-secondary text-decoration-none link-light p-sm-3 rounded-pill" href="#!">좋아요</a>
                                        <p class="fs-5 mb-2 fw-bold">현재 파티원: </p>
                                        <div id="joinmember"></div>
                                        <p class="fs-5 mb-2 fw-bold">모집 인원: ${acceptedMember} / ${maxMember}</p>
                                        <p class="fs-5 mb-2 fw-bold">모임 일자: ${partyDate} + ${day} </p>
                                        <p class="fs-5 mb-2 fw-bold">주소: ${address} ${detailAddress}</p>
                                        <p class="fs-5 fw-bold">장소: ${partyPlace}</p>
                                    </section>
                                </article>
                                <a class="btn-long btn-primary btn-xl-long rounded-pill" href="#!">신청 (1:1 채팅)</a>
                                <a class="btn-long btn-primary btn-xl-long rounded-pill" href="#!">수정하기</a>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
                `

            console.log("포스트 정보" + nickname, title, content, categoryId, status, acceptedMember, maxMember, partyDate,
                closeDate, day, address, detailAddress, partyPlace, viewCnt, joinMember)

            $('#partypost').append(tempHtml)

            let joinMemberRows = response['data']['joinMember']
            for (let j = 0; j < joinMemberRows.length; j++) {
                let applicationId = joinMemberRows[j]['id']
                let memberNickname = joinMemberRows[j]['nickname']
                let memberStatus = joinMemberRows[j]['status']

                let partypost_member_temp_html = `
                    <div class="container" onclick="">
                        <div class="ms-3">
                            <div class="fw-bold mb-2">파티원 닉네임: ${memberNickname} / 상태: ${memberStatus}</div>
                        </div>
                    </div>`

                $('#joinmember').append(partypost_member_temp_html)
            }
        }
    });
}