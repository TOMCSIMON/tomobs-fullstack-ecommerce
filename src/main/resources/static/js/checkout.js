 document.getElementById('checkoutForm').addEventListener('submit', function(e) {

        // 1. Check if address is selected
        const addressSelected = document.querySelector('input[name="addressId"]:checked');
        const addressError = document.getElementById('addressError');

        if (!addressSelected) {
            e.preventDefault();  // Stop form submission
            addressError.classList.remove('d-none');  // Show error
            addressError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return false;
        } else {
            addressError.classList.add('d-none');  // Hide error if valid
        }

        // 2. Check if payment method is selected
        const paymentSelected = document.querySelector('input[name="paymentMethod"]:checked');
        const paymentError = document.getElementById('paymentError');

        if (!paymentSelected) {
            e.preventDefault();
            paymentError.classList.remove('d-none');
            paymentError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return false;
        } else {
            paymentError.classList.add('d-none');
        }

        // 3. Disable button to prevent double submission
        const btn = document.getElementById('placeOrderBtn');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processing...';

        return true;
    });

    // Hide errors when user selects an option
    document.querySelectorAll('input[name="addressId"]').forEach(radio => {
        radio.addEventListener('change', function() {
            document.getElementById('addressError').classList.add('d-none');
        });
    });

    document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
        radio.addEventListener('change', function() {
            document.getElementById('paymentError').classList.add('d-none');
        });
    });