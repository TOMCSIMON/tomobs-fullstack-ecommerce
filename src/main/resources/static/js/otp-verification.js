document.addEventListener('DOMContentLoaded', () => {
    const inputs = document.querySelectorAll('.otp-field');
    const form = document.getElementById('otp-form');
    const finalOtpInput = document.getElementById('final-otp');

    // Auto-focus next field and handle backspace
    inputs.forEach((input, index) => {
        input.addEventListener('input', (e) => {
            if (e.target.value.length === 1 && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !e.target.value && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });

    // Combine 6 boxes into 1 string before submission
    form.addEventListener('submit', (e) => {
        let combinedValue = "";
        inputs.forEach(input => {
            combinedValue += input.value;
        });

        if (combinedValue.length < 6) {
            e.preventDefault();
            alert("Please enter the full 6-digit code.");
            return;
        }

        finalOtpInput.value = combinedValue;
    });
});