document.addEventListener('DOMContentLoaded', function () {

    const actionModal = document.getElementById('orderActionModal');
    const actionReasonInput = document.getElementById('actionReason');
    const submitBtn = document.getElementById('submitActionBtn');
    const modalTitle = document.getElementById('modalTitle');
    const reasonLabel = document.getElementById('reasonLabel');
    const reasonError = document.getElementById('reasonError'); // പുതിയ എറർ ഫീൽഡ്

    let currentOrderId = null;
    let currentAction = null;

    actionReasonInput.addEventListener('input', function() {
        if (this.value.trim() !== '') {
            this.classList.remove('is-invalid');
            reasonError.classList.add('d-none');
        }
    });

    actionModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        currentOrderId = button.getAttribute('data-id');
        currentAction = button.getAttribute('data-action');

        if (currentAction === 'RETURN') {
            modalTitle.innerText = "RETURN ORDER?";
            reasonLabel.innerText = "Reason for Return";
            submitBtn.innerText = "Submit Return";
            submitBtn.className = "btn btn-warning w-100 mt-3";
        } else {
            modalTitle.innerText = "ARE YOU SURE?";
            reasonLabel.innerText = "Enter your Reason";
            submitBtn.innerText = "Cancel Order";
            submitBtn.className = "btn btn-danger w-100 mt-3";
        }

        actionReasonInput.value = "";
        actionReasonInput.classList.remove('is-invalid');
        reasonError.classList.add('d-none');
    });

    submitBtn.addEventListener('click', function () {
        submitAction(currentOrderId, currentAction);
    });

});

function submitAction(orderId, actionType) {

    const actionReasonInput = document.getElementById('actionReason');
    const reasonError = document.getElementById('reasonError');
    const reason = actionReasonInput.value;

    if (!reason.trim()) {
        actionReasonInput.classList.add('is-invalid');
        reasonError.classList.remove('d-none');
        return;
    }

    const isReturn = actionType === 'RETURN';
    const endpoint = isReturn ? `/orders/return/${orderId}` : `/orders/cancel/${orderId}`;

    const params = new URLSearchParams();
    params.append(isReturn ? 'returnReason' : 'cancelReason', reason);

    const modalElement = document.getElementById('orderActionModal');
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    if(modalInstance) {
        modalInstance.hide();
    }

    fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            Toast.fire({
                icon: 'success',
                title: isReturn ? 'Return request submitted successfully!' : 'Order cancelled successfully!'
            }).then(() => {
                location.reload();
            });
        } else {
            Toast.fire({
                icon: 'error',
                title: data.message || 'Something went wrong!'
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Toast.fire({
            icon: 'error',
            title: 'An unexpected error occurred. Please try again.'
        });
    });
}