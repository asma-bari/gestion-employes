document.addEventListener("DOMContentLoaded", function () {

    const passwordInput = document.getElementById("password");
    const toggleButton = document.getElementById("togglePassword");

    if (toggleButton && passwordInput) {

        toggleButton.addEventListener("click", function () {

            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                toggleButton.textContent = "🙈";
                toggleButton.setAttribute(
                    "aria-label",
                    "Masquer le mot de passe"
                );
            } else {
                passwordInput.type = "password";
                toggleButton.textContent = "👁";
                toggleButton.setAttribute(
                    "aria-label",
                    "Afficher le mot de passe"
                );
            }

        });

    }

});
