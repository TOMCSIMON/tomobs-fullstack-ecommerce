document.addEventListener("DOMContentLoaded", function() {

    const exportPdfBtn = document.getElementById('exportPdfBtn');
    if (exportPdfBtn) {
        exportPdfBtn.addEventListener('click', function() {
            let startDate = document.getElementById('startDate').value;
            let endDate = document.getElementById('endDate').value;
            let downloadUrl = `/admin/dashboard/export-pdf?startDate=${startDate}&endDate=${endDate}`;

            fetch(downloadUrl, {
                method: 'GET'
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to download PDF');
                }
                return response.blob();
            })
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = `ToMobs_Sales_Report_${startDate}_to_${endDate}.pdf`;
                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);

                Toast.fire({
                    icon: 'success',
                    title: 'PDF Report downloaded successfully!'
                });
            })
            .catch(error => {
                console.error('Error:', error);
                Toast.fire({
                    icon: 'error',
                    title: 'Could not download PDF. Please try again.'
                });
            });
        });
    }

    const canvasElement = document.getElementById('salesChart');
    if (canvasElement) {
        const ctx = canvasElement.getContext('2d');

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: typeof chartLabels !== 'undefined' ? chartLabels : [],
                datasets: [{
                    label: 'Daily Revenue (₹)',
                    data: typeof chartData !== 'undefined' ? chartData : [],
                    borderColor: '#212121',
                    backgroundColor: 'rgba(33, 33, 33, 0.05)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: '#fff',
                    pointBorderColor: '#212121',
                    pointBorderWidth: 2,
                    pointRadius: 5,
                    pointHoverRadius: 7
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        backgroundColor: '#000',
                        titleFont: { family: 'Poppins', size: 14 },
                        bodyFont: { family: 'Poppins', size: 12 },
                        padding: 12,
                        cornerRadius: 8,
                        displayColors: false,
                        callbacks: {
                            label: function(context) {
                                return '₹ ' + context.parsed.y.toLocaleString('en-IN');
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: { borderDash: [5, 5], color: '#eaeaea' },
                        ticks: {
                            font: { family: 'Poppins', size: 12 },
                            callback: function(value) {
                                return '₹' + value.toLocaleString('en-IN');
                            }
                        }
                    },
                    x: {
                        grid: { display: false },
                        ticks: { font: { family: 'Poppins', size: 12 } }
                    }
                }
            }
        });
    }
});