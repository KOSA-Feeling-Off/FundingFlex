document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.querySelector('form.form-horizontal');
    loginForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(loginForm);
        const userEmail = formData.get('email');
        const userPassword = formData.get('password');

        fetch('/api/signin', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email: userEmail, password: userPassword})
        })
        .then(response => response.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem('jwtToken', data.token);
                window.location.href = '/';
            } else {
                alert("No token received, check the server response.");
            }
        })
        .catch(error => {
            console.error('Authentication failed:', error);
            alert("Authentication failed, check console for more information.");
        });
    });
});