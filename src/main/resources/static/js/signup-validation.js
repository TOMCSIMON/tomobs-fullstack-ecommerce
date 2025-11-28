// PASSWORD TOGGLE
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
setupPasswordToggle("toggleConfirmPassword", "confirmPassword", "/icons/hide.png", "/icons/view.png");


// AJAX EMAIL CHECK
const emailInput = document.getElementById('email');
const emailErrorSpan = document.getElementById('emailError');
const signupForm = document.getElementById('signup-form');

emailInput.addEventListener('input', function () {
    console.log("Typing detected:", this.value);
    const email = this.value.trim();

    if (email === '') {
        emailErrorSpan.textContent = '';
        signupForm.dataset.emailExists = 'false';
        return;
    }

    fetch(`/ajax/check-email?email=${encodeURIComponent(email)}`)
        .then(response => response.json())
        .then(data => {
            emailErrorSpan.textContent = data.message;
            if (data.exists) {
                emailErrorSpan.classList.add('text-danger');   // red for already in use
                emailErrorSpan.classList.remove('text-success');
            } else {
                emailErrorSpan.classList.add('text-success');   // green for available
                emailErrorSpan.classList.remove('text-danger');
            }

            signupForm.dataset.emailExists = data.exists;
        })
        .catch(err => console.error('AJAX error:', err));
});


// FORM SUBMIT
signupForm.addEventListener('submit', function(event) {
    if (!validateRegistrationForm()) {
        event.preventDefault();
    }
});

function clearErrors() {
    document.querySelectorAll(".error-msg").forEach(e => e.textContent = "");
}

function validateRegistrationForm() {
    clearErrors();

    const username = document.getElementById('userName');
    const email = document.getElementById('email');
    const phoneNumber = document.getElementById('phoneNumber');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');

    let isValid = true;

    // USERNAME VALIDATION
    if (username.value.trim() === '') {
        document.getElementById('usernameError').textContent = "Username cannot be empty.";
        isValid = false;
    } else if (username.value.length < 3 || username.value.length > 20) {
        document.getElementById('usernameError').textContent = "Username must be between 3 and 20 characters.";
        isValid = false;
    }

    // EMAIL VALIDATION
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email.value.trim() === '') {
        document.getElementById('emailError').textContent = "Email cannot be empty.";
        isValid = false;
    } else if (!emailRegex.test(email.value)) {
        document.getElementById('emailError').textContent = "Please enter a valid email.";
        isValid = false;
    } else if (signupForm.dataset.emailExists === 'true') {
        document.getElementById('emailError').textContent = "Email already in use";
        isValid = false;
    }

    // PHONE VALIDATION
    const phoneRegex = /^[0-9]{10,17}$/;
    if (!phoneRegex.test(phoneNumber.value.trim())) {
        document.getElementById('phoneError').textContent = "Phone must be 10–17 digits.";
        isValid = false;
    }

    // PASSWORD VALIDATION
    const strongPasswordRegex =
        /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$/;

    if (!strongPasswordRegex.test(password.value)) {
        document.getElementById('passwordError').textContent =
            "Must contain uppercase, lowercase, number & special char.";
        isValid = false;
    }

    // CONFIRM PASSWORD VALIDATION
    if (password.value !== confirmPassword.value) {
        document.getElementById('confirmPasswordError').textContent = "Passwords do not match.";
        isValid = false;
    }

    return isValid;
}
