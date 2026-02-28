document.addEventListener('DOMContentLoaded', () => {
    const inputs = document.querySelectorAll('.otp-field');
    const form = document.getElementById('otp-form');
    const finalOtpInput = document.getElementById('final-otp');
    const resendBtn = document.getElementById('resend-btn');
    const timerDisplay = document.getElementById('timer');

    inputs.forEach((input, index) => {
        input.addEventListener('input', (e) => {

            e.target.value = e.target.value.replace(/[^0-9]/g, '');

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

    let timeLeft = 300;

    const startTimer = () => {
        const countdown = setInterval(() => {
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;

            if (timerDisplay) {
                timerDisplay.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
            }

            if (timeLeft <= 0) {
                clearInterval(countdown);

                if (resendBtn) {
                    resendBtn.classList.remove('disabled');
                }

                const expiryText = document.querySelector('.expiry-text');
                if (expiryText) {
                    expiryText.innerHTML = "OTP expired. Please resend.";
                }
            } else {
                timeLeft--;
            }
        }, 1000);
    };

    startTimer();
});