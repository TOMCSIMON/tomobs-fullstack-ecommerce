document.addEventListener('DOMContentLoaded', function () {

    const actionModal = document.getElementById('orderActionModal');
    const actionReasonInput = document.getElementById('actionReason');
    const submitBtn = document.getElementById('submitActionBtn');
    const modalTitle = document.getElementById('modalTitle');
    const reasonLabel = document.getElementById('reasonLabel');

    let currentOrderId = null;
    let currentAction = null;

    // 🔹 When modal opens
    actionModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        currentOrderId = button.getAttribute('data-id');
        currentAction = button.getAttribute('data-action');

        // UI changes
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
    });

    // 🔹 Submit button click
    submitBtn.addEventListener('click', function () {
        submitAction(currentOrderId, currentAction);
    });

});

function submitAction(orderId, actionType) {

    const reason = document.getElementById('actionReason').value;

    if (!reason.trim()) {
        alert("Please enter a reason");
        return;
    }

    const isReturn = actionType === 'RETURN';

    const endpoint = isReturn
        ? `/orders/return/${orderId}`
        : `/orders/cancel/${orderId}`;

    const params = new URLSearchParams();
    params.append(isReturn ? 'returnReason' : 'cancelReason', reason);

    fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(isReturn ? "Return request submitted!" : "Order cancelled!");
            location.reload();
        } else {
            alert("Error: " + data.message);
        }
    })
    .catch(error => console.error('Error:', error));
}