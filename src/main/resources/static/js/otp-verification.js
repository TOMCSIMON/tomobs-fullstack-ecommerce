document.addEventListener('DOMContentLoaded', () => {

    const inputs = document.querySelectorAll('.otp-field');
    const form = document.getElementById('otp-form');
    const finalOtpInput = document.getElementById('final-otp');
    const resendBtn = document.getElementById('resend-btn');
    const timerDisplay = document.getElementById('timer');
    const expiryTextContainer = document.querySelector('.expiry-text');
    const otpError = document.getElementById('otpError');

    let timeLeft = 120;
    let countdownInterval;

    inputs.forEach((input, index) => {
        input.addEventListener('input', (e) => {
            e.target.value = e.target.value.replace(/[^0-9]/g, '');

            if (otpError) {
                otpError.classList.add('d-none');
            }

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
            if (otpError) {
                otpError.classList.remove('d-none');
            }
            return;
        }

        finalOtpInput.value = combinedValue;
    });

    const startTimer = () => {
        if (countdownInterval) clearInterval(countdownInterval);

        countdownInterval = setInterval(() => {
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;

            const displayMinutes = minutes.toString().padStart(2, '0');
            const displaySeconds = seconds.toString().padStart(2, '0');

            const currentTimerDisplay = document.getElementById('timer');
            if (currentTimerDisplay) {
                currentTimerDisplay.textContent = `${displayMinutes}:${displaySeconds}`;
            }

            if (timeLeft <= 0) {
                clearInterval(countdownInterval);
                if (resendBtn) resendBtn.classList.remove('disabled');

                if (expiryTextContainer) {
                    expiryTextContainer.innerHTML = "OTP expired. Please resend.";
                }
            } else {
                timeLeft--;
            }
        }, 1000);
    };

    if (resendBtn) {
        resendBtn.addEventListener('click', (e) => {
            e.preventDefault();

            const emailInput = document.querySelector('input[name="email"]');
            const email = emailInput ? emailInput.value : "";

            fetch(`/resend-otp?email=${encodeURIComponent(email)}`, {
                method: 'GET'
            })
            .then(async response => {
                const data = await response.json();
                if (response.ok) {
                    Toast.fire({
                        icon: 'success',
                        title: 'New OTP sent successfully!'
                    });

                    timeLeft = 120;
                    resendBtn.classList.add('disabled');

                    if (expiryTextContainer) {
                        expiryTextContainer.innerHTML = 'The OTP will expire in <span id="timer">02:00</span>';
                    }
                    startTimer();
                } else {
                    Toast.fire({
                        icon: 'error',
                        title: data.message || "Failed to resend OTP."
                    });
                }
            })
            .catch(err => {
                console.error("AJAX Error:", err);
                Toast.fire({
                    icon: 'error',
                    title: "An error occurred. Please try again."
                });
            });
        });
    }
    startTimer();
});