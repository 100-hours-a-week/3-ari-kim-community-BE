document.addEventListener('DOMContentLoaded', function() {
    loadHeader();
});

// 공통 header 적용
function loadHeader() {
    fetch('layout/header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header-placeholder').innerHTML = data;
            setupHeaderUI();
            activateDropdown();
        });
}

function setupHeaderUI() {
    const currentPage = window.location.pathname;

    const backButton = document.getElementById('back-button');
    const profileContainer = document.getElementById('profile-container');

    // 로그인/회원가입 페이지인 경우, 프로필 아이콘/뒤로가기 버튼 비활성화
    if (currentPage.includes('LoginPage.html') || currentPage.includes('SignupPage.html')) {
    } 
    // 게시글 상세, 작성, 수정 페이지인 경우, 프로필 아이콘/뒤로가기 버튼 활성화
    else if (currentPage.includes('PostDetailPage.html') || currentPage.includes('PostCreatePage.html')) {
        backButton.style.display = 'block';
        profileContainer.style.display = 'block';
    }
    // 그 외 모든 페이지, 프로필 아이콘 활성화
    else {
        profileContainer.style.display = 'block';
    }
}

//  프로필 아이콘을 클릭했을 때 드롭다운 메뉴
function activateDropdown() {
    const profileIcon = document.getElementById('profile-icon');
    const profileMenu = document.getElementById('profile-menu');

    if (profileIcon) {
        profileIcon.addEventListener('click', function() {
            profileMenu.classList.toggle('show');
        });
    }
}