let postId = new URLSearchParams(window.location.search).get('postId');

$(document).ready(function () {
    get_edit_category();
    loadPartyPost(postId);
})

function loadPartyPost(postId) {
    $.ajax({
        type: "GET",
        url: `/api/party-posts/${postId}`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        error() {
            alert("오류가 발생했습니다.")
            window.history.back()
        },
        success: function (response) {
            let data = response['data']

            let title = data['title']
            let content = data['content']
            let address = data['address']
            let detailAddress = data['detailAddress']
            let partyPlace = data['partyPlace']

            $('#title').val(title);
            $('#content').val(content);
            $('#place-address').val(address + ' ' + detailAddress)
            $('#place-name').val(partyPlace)
            $('#keyword').val(partyPlace)
        }
    })
}

//수정 실행
function submitUpdatePartyPost(postId) {
    $.ajax({
        type: "PATCH",
        url: `/api/party-posts/${postId}`,
        headers: {
            "Authorization": getCookieValue('Authorization')
        },
        data: JSON.stringify({
            title: $('#title').val(),
            content: $('#content').val(),
            categoryId: $('#category').val(),
            partyAddress: $('#place-address').val(),
            partyPlace: $('#place-name').val
        }),
        error() {
            alert("오류가 발생했습니다. 계속 될경우 관리자에게 문의해주세요.")
        },
        success: function (response) {

        }

    })
}

//카테고리 가져오기
function get_edit_category() {
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
                    console.log(response)
                    console.log(rows)
                    let category_name = rows[i]['name']
                    let category_temp = `<option value= ${i + 1}> ${category_name} </option>`

                    $('#category').append(category_temp)
                }
            }
        }

    });
}