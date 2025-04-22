function updateTime() {
    const now = new Date();
    const options = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    };
    document.getElementById('clientTime').textContent = now.toLocaleString('en-US', options);
}

setInterval(updateTime, 1000);
window.onload = updateTime;
