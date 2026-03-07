
document.addEventListener("DOMContentLoaded", function() {

    function setupPasswordToggle(toggleId, inputId, hideIcon, viewIcon) {
        const toggle = document.getElementById(toggleId);
        const input = document.getElementById(inputId);

        if (toggle && input) {
            toggle.addEventListener("click", () => {
                const type = input.type === "password" ? "text" : "password";
                input.type = type;
                toggle.src = type === "password" ? hideIcon : viewIcon;
            });
        }
    }


        const emailInput = document.getElementById("email");
        const passwordInput = document.getElementById("password");
        const emailError = document.getElementById("emailError");
        const passwordError = document.getElementById("passwordError");

        const serverErrorBox = document.querySelector('.server-error-box');

        if (serverErrorBox) {
            emailInput.addEventListener('input', function() {
                serverErrorBox.style.display = 'none';
            });

            passwordInput.addEventListener('input', function() {
                serverErrorBox.style.display = 'none';
            });
        }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            
            event.preventDefault();

            emailError.textContent = "";
            passwordError.textContent = "";

            const email = emailInput.value.trim();
            const password = passwordInput.value.trim();
            let isValid = true;

            if (email === "") {
                emailError.textContent = "Email is required.";
                isValid = false;
            } else if (!emailRegex.test(email)) {
                emailError.textContent = "Please enter a valid email format.";
                isValid = false;
            }

            if (password === "") {
                passwordError.textContent = "Password is required.";
                isValid = false;
            }

            if (isValid) {
                loginForm.submit();
            }
        });
    }
});