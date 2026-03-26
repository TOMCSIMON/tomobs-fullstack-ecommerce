document.addEventListener('DOMContentLoaded', function () {
    const statusMenu = document.getElementById('statusMenu');

    if (statusMenu) {
        statusMenu.addEventListener('click', function (event) {
            const target = event.target;

            if (target.classList.contains('status-link')) {
                event.preventDefault();

                const newStatus = target.getAttribute('data-status');
                const orderId = target.closest('[data-order-id]').getAttribute('data-order-id');

                fetch(`/admin/orders/update-status/${orderId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({ 'status': newStatus })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        document.querySelector('.dropdown-toggle span').innerText = newStatus;
                        const summaryStatus = document.getElementById('order-status');
                        if(summaryStatus) {
                            summaryStatus.innerText = newStatus;
                        }
                        Toast.fire({
                            icon: 'success',
                            title: 'Status updated successfully!'
                        });
                    } else {
                        Toast.fire({
                            icon: 'error',
                            title: data.message || 'Status not changed!'
                        });
                    }
                })
                .catch(err => {
                    console.error("API Error:", err);
                    Toast.fire({
                        icon: 'error',
                        title: 'Server connection failed!'
                    });
                });
            }
        });
    }
});