document.addEventListener('DOMContentLoaded', function() {

    // 로그인 페이지
    if (window.location.pathname.includes('LoginPage.html')) {
        
        const emailInput = document.getElementById('email');
        const passwordInput = document.getElementById('password');
        const loginButton = document.getElementById('login-button');
        const emailHelper = document.getElementById('email-helper');
        const passwordHelper = document.getElementById('password-helper');
        const loginForm = document.getElementById('login-form');

        // 이메일 유효성 검사
        function validateEmail() {
            const email = emailInput.value;
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            
            if (email === "") {
                emailHelper.textContent = "";
                return false;
            } else if (!emailRegex.test(email)) {
                emailHelper.textContent = "올바른 이메일 주소 형식을 입력해주세요. (예: example@example.com)";
                return false;
            } else {
                emailHelper.textContent = "";
                return true;
            }
        }

        // 3. 비밀번호 유효성 검사 (8-20자, 대/소문자, 숫자, 특수문자 포함)
        function validatePassword() {
            const password = passwordInput.value;
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

            if (password === "") {
                passwordHelper.textContent = "";
                return false;
            } else if (!passwordRegex.test(password)) {
                passwordHelper.textContent = "비밀번호는 8자 이상, 20자 이하이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다.";
                return false;
            } else {
                passwordHelper.textContent = "";
                return true;
            }
        }

        // 4. 로그인 버튼 활성화
        function updateLoginButtonState() {
            if (validateEmail() && validatePassword()) {
                loginButton.disabled = false;
            } else {
                loginButton.disabled = true;
            }
        }

        // 유효성 검사 및 버튼 상태 업데이트
        emailInput.addEventListener('input', updateLoginButtonState);
        passwordInput.addEventListener('input', updateLoginButtonState);

        // 로그인 버튼 클릭
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const isEmailValid = validateEmail();
            const isPasswordValid = validatePassword();
            
            if (emailInput.value === "") {
                emailHelper.textContent = "이메일을 입력해주세요.";
            }
            if (passwordInput.value === "") {
                passwordHelper.textContent = "비밀번호를 입력해주세요.";
            }

            // 유효성 검사를 통과했을 경우
            if (isEmailValid && isPasswordValid) {
                // 서버에 로그인 요청 구현하기
                // passwordHelper.textContent = "아이디 또는 비밀번호를 확인해주세요.";
                
                window.location.href = 'PostListPage.html';
            }
        });
    }
});