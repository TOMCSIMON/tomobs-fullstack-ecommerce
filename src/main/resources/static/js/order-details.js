function submitCancelOrder(orderId) {
    const reason = document.getElementById('cancelReason').value;

    if(!reason.trim()) {
        alert("Please enter a reason");
        return;
    }
console.log("js called!!!!!!1");
    fetch(`/orders/cancel/${orderId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'cancelReason': reason
        })
    })
    .then(response => response.json())
    .then(data => {
        if(data.success) {
            alert(data.message);
            location.reload();
        } else {
            alert("Error: " + data.message);
        }
    })
    .catch(error => console.error('Error:', error));
}