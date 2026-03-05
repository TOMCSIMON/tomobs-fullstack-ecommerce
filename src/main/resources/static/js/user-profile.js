// 🟢 Sr. Tom's Reusable Password Toggle Helper
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

document.addEventListener('DOMContentLoaded', function() {

    // 1. Setup Password Toggles
    setupPasswordToggle("toggleOldPassword", "oldPassword", "/icons/hide.png", "/icons/view.png");
    setupPasswordToggle("toggleNewPassword", "newPassword", "/icons/hide.png", "/icons/view.png");
    setupPasswordToggle("toggleConfirmNewPassword", "confirmNewPassword", "/icons/hide.png", "/icons/view.png");

    const passwordForm = document.getElementById('changePasswordForm');

    if (passwordForm) {
        const strongPasswordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$/;

        passwordForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const oldPassword = document.getElementById('oldPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmNewPassword = document.getElementById('confirmNewPassword').value;

            // Error Elements
            const oldError = document.getElementById('oldPasswordError');
            const newError = document.getElementById('newPasswordError');
            const confirmError = document.getElementById('confirmNewPasswordError');

            // Clear previous errors
            oldError.textContent = "";
            newError.textContent = "";
            confirmError.textContent = "";

            let isValid = true;

            // 1. Old Password Check
            if (!oldPassword) {
                oldError.textContent = "Please enter your current password.";
                isValid = false;
            }

            // 2. New Password Check
            if (!strongPasswordRegex.test(newPassword)) {
                newError.textContent = "Must contain at least 8 chars, 1 uppercase, 1 lowercase, 1 number & 1 special char.";
                isValid = false;
            } else if (oldPassword && oldPassword === newPassword) {
                newError.textContent = "New password cannot be the same as the old password.";
                isValid = false;
            }

            // 3. Confirm Password Check
            if (newPassword !== confirmNewPassword) {
                confirmError.textContent = "Passwords do not match.";
                isValid = false;
            }

            // എവിടെയെങ്കിലും തെറ്റുണ്ടെങ്കിൽ ഇവിടെ വെച്ച് നിർത്തും (API വിളിക്കില്ല)
            if (!isValid) return;

            const payload = {
                oldPassword: oldPassword,
                newPassword: newPassword,
                confirmNewPassword: confirmNewPassword
            };

            fetch('/profile/password', {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            })
            .then(async response => {
                if (response.ok) {
                    Toast.fire({
                        icon: 'success',
                        title: 'Password successfully updated!'
                    });

                    document.getElementById('changePasswordForm').reset();
                    const modalEl = document.getElementById('changePasswordModal');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    modal.hide();
                } else {
                    const errorData = await response.json();

                    // ബാക്ക്എൻഡിൽ നിന്ന് "Wrong old password" എറർ വന്നാൽ അത് ഇൻപുട്ടിന് താഴെ കാണിക്കാം
                    if (errorData.message && errorData.message.toLowerCase().includes('old password')) {
                        oldError.textContent = "Incorrect old password. Please try again.";
                    } else {
                        Toast.fire({
                            icon: 'error',
                            title: errorData.message || "Failed to change password."
                        });
                    }
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Toast.fire({
                    icon: 'error',
                    title: 'Something went wrong!'
                });
            });
        });
    }
});