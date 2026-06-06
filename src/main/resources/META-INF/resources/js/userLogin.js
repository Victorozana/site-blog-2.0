const formLogin = document.getElementById("formLogin");

async function login(event){
    event.preventDefault();

    const typedEmail = document.querySelector("#email").value;
    const typedPassword = document.querySelector("#password").value;

    try {
        const response = await fetch("/login/auth", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email: typedEmail, password: typedPassword})
        });

        if (!response.ok){
            throw new Error("Invalid Credentials!");
        }

        const data = await response.json();
        // set cookie for authGuard to detect logged user (2 hours)
        if (data && data.name) {
            document.cookie = `userName=${encodeURIComponent(data.name)}; path=/; max-age=${60*60*2}`;
        }

        // store token and userType in localStorage
        if (data && data.token) {
            localStorage.setItem('token', data.token);
        }

        if (data && data.userType) {
            localStorage.setItem('userType', data.userType);
        }

        console.log("chegou no auth")

        window.location.href = "/";
    }
    catch (error) {
        alert(error.message);
    }
}

formLogin.addEventListener("submit", login);