$(document).ready(function () {
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
