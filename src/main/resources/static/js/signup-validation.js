function setupPasswordToggle(toggleId, inputId, hideIcon, viewIcon) {
    const toggle = document.getElementById(toggleId);
    const input = document.getElementById(inputId);

    toggle.addEventListener("click", () => {
        const type = input.type === "password" ? "text" : "password";
        input.type = type;

        toggle.src = type === "password" ? hideIcon : viewIcon;
    });
}

// Password field
setupPasswordToggle("togglePassword", "password", "/icons/hide.png", "/icons/view.png");

// Confirm password field
setupPasswordToggle("toggleConfirmPassword", "confirmPassword", "/icons/hide.png", "/icons/view.png");
