$(document).ready(function () {
    getCategories();
    //검색어 값으로 받음
    let searchText = new URLSearchParams(window.location.search).get('searchText');
    if (searchText) {
        firstsearchPartyPost(searchText); //검색어가 들어왔을때 검색적용
    }
});

// 토큰값 분리
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

function partypostClick(postId) {
    console.log(postId)
    // input 요소에서 검색어를 가져옵니다.
    // const searchText = document.getElementById("postId").value;
    // // 검색어를 인코딩합니다.
    // const encodedSearchText = encodeURIComponent(searchText);
    // 검색 결과 페이지 URL을 생성합니다. ex)/search?searchText=검색어
    const partypostPageUrl = `/page/partypost?partypostId=` + postId;

    // 검색 결과 페이지로 이동합니다.
    window.location.href = partypostPageUrl;
}

//페이지 바로 로딩시 검색어가 들어왔을때
function firstsearchPartyPost(inputVal) {
    $('#searchPartyPostResult').empty();
    $("#search").val(inputVal);
    $.ajax({
        url: "http://localhost:8080/api/party-posts/search?",
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        data: {
            searchText: inputVal
        },
        type: "GET",
        success: function (response) {
            console.log(response);
            let responseData = response['data']
            for (let i = 0; i < responseData.length; i++) {
                let obj = responseData[i];
                let postId = obj['postId'];
                let title = obj['title'];
                let partyOwner = obj['partyOwner'];
                let status = obj['status'];
                let acceptedMember = obj['acceptedMember'] + 1;
                let maxMember = obj['maxMember'];
                let partyDate = new Date(obj['partyDate']);
                let closeDate = new Date(obj['closeDate']);
                let partyAddress = obj['partyAddress'];
                let partyPlace = obj['partyPlace'];
                let tempHtml = `
        <div class="col-lg-4 my-5">
            <div class="card h-100 shadow border-0">
                <div class="card-body p-4" onclick="partypostClick(${postId})">
                    <div class="badge bg-primary bg-gradient rounded-pill mb-2">모집상태 :${status}</div>
                    <a class="text-decoration-none link-dark stretched-link" href="#!"><h5 class="card-title mb-3">${title}</h5></a>
                    <p class="card-text mb-0">위치 정보: ${partyAddress} / ${partyPlace}</p>
                    <p class="card-text mb-0">모임일: ${partyDate}</p>
                    <p class="card-text mb-0">모집 인원 ${acceptedMember}/${maxMember}</p>
                </div>
                <div class="card-footer p-4 pt-0 bg-transparent border-top-0">
                    <div class="d-flex align-items-end justify-content-between">
                        <div class="d-flex align-items-center">
                            <img class="rounded-circle me-3" src="https://dummyimage.com/40x40/ced4da/6c757d"
                                 alt="..."/>
                            <div class="small">
                                <div class="fw-bold">파티장 : ${partyOwner}</div>
                                <div class="text-muted">마감일: ${closeDate}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
          `

                $('#searchPartyPostResult').append(tempHtml)

            }
        }
    });
}

//로딩 이후 따로 재검색시 이용
function searchPartyPost(page) {
    var inputVal = document.getElementById("search").value;

    console.log("입력된 값은 " + inputVal + "입니다.");

    $('#searchPartyPostResult').empty()

    $.ajax({
        url: "http://localhost:8080/api/party-posts/search",
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        data: {
            searchText: inputVal,
            page: page
        },
        type: "GET",
        success: function (response) {
            console.log(response);
            let responseData = response['data']
            for (let i = 0; i < responseData.length; i++) {
                let obj = responseData[i];
                let postId = obj['postId'];
                let title = obj['title'];
                let partyOwner = obj['partyOwner'];
                let status = obj['status'];
                let acceptedMember = obj['acceptedMember'] + 1;
                let maxMember = obj['maxMember'];
                let partyDate = new Date(obj['partyDate']);
                let closeDate = new Date(obj['closeDate']);
                let partyAddress = obj['partyAddress'];
                let partyPlace = obj['partyPlace'];
                let tempHtml = `
        <div class="col-lg-4 my-5">
            <div class="card h-100 shadow border-0">
                <div class="card-body p-4" onclick="partypostClick(${postId})">
                    <div class="badge bg-primary bg-gradient rounded-pill mb-2">모집상태 :${status}</div>
                    <a class="text-decoration-none link-dark stretched-link" href="#!"><h5 class="card-title mb-3">${title}</h5></a>
                    <p class="card-text mb-0">위치 정보: ${partyAddress} / ${partyPlace}</p>
                    <p class="card-text mb-0">모임일: ${partyDate}</p>
                    <p class="card-text mb-0">모집 인원 ${acceptedMember}/${maxMember}</p>
                </div>
                <div class="card-footer p-4 pt-0 bg-transparent border-top-0">
                    <div class="d-flex align-items-end justify-content-between">
                        <div class="d-flex align-items-center">
                            <img class="rounded-circle me-3" src="https://dummyimage.com/40x40/ced4da/6c757d"
                                 alt="..."/>
                            <div class="small">
                                <div class="fw-bold">파티장 : ${partyOwner}</div>
                                <div class="text-muted">마감일: ${closeDate}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
          `

                $('#searchPartyPostResult').append(tempHtml)

            }
        }
    });
}

