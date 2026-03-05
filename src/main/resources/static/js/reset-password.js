
function setupPasswordToggle(toggleId, inputId, hideIcon, viewIcon) {
    const toggle = document.getElementById(toggleId);
    const input = document.getElementById(inputId);

    toggle.addEventListener("click", () => {
        const type = input.type === "password" ? "text" : "password";
        input.type = type;
        toggle.src = type === "password" ? hideIcon : viewIcon;
    });
}

setupPasswordToggle("togglePassword", "password", "/icons/hide.png", "/icons/view.png");
setupPasswordToggle("toggleConfirmPassword", "confirm-password", "/icons/hide.png", "/icons/view.png");


document.addEventListener('DOMContentLoaded', function() {

    const resetForm = document.getElementById('resetPasswordForm');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirm-password');
    const passwordError = document.getElementById('passwordError');
    const confirmPasswordError = document.getElementById('confirmPasswordError');

    const strongPasswordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$/;

    function clearErrors() {
        passwordError.textContent = "";
        confirmPasswordError.textContent = "";
    }

    resetForm.addEventListener('submit', function(event) {

        event.preventDefault();
        clearErrors();

        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        let isValid = true;

        if (!strongPasswordRegex.test(password)) {
            passwordError.textContent = "Must contain at least 8 characters, one uppercase, one lowercase, one number & one special character.";
            isValid = false;
        }

        if (password !== confirmPassword) {
            confirmPasswordError.textContent = "Passwords do not match. Please try again.";
            isValid = false;
        }

        if (isValid) {
            resetForm.submit();
        }
    });

});
