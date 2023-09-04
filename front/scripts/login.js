<<<<<<< HEAD
window.addEventListener('load', function () {

    const form = document.querySelector('form')
    const email = document.querySelector('#inputEmail')
    const password = document.querySelector('#inputPassword')
    const endpointLogin = 'http://localhost:8080/user/login'


    form.addEventListener('submit', function (event) {
        event.preventDefault()

        const payload = {
            email: email.value,
            password: password.value
        }

        const config = {
            method: 'POST',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
            body: JSON.stringify(payload),   
        }

        if(payload.email == '' || payload.email.includes(' ')){
            errorMessage()
        }
        else if (payload.password == '' || payload.password.includes(' ')) {
            errorMessage()
        }
        else{
            loginUser(config)            
        }

        form.reset()
    })


    function loginUser(config) {

        fetch(endpointLogin, config)
        .then((response) => response.json())
        .then((data) => {
            console.log("Promise OK!")
            console.log(data)

            //if (data.jwt) {
            //    localStorage.setItem('jwt', JSON.stringify(data.jwt));
                sessionStorage.setItem('userName', JSON.stringify(data.userName))

                if (data.token == 33) {
                    location.replace('./admin.html')
                }
                else if (data.token == 1){
                    location.replace('./tasks.html')                    
                }                
                else if (data.token == 0){
                    alert('User not found')
                    location.replace('./root.html')                    
                }

            //}

        }).catch(err => {
            console.log("Promise rejected...");
            console.log(err);      
        })
    }


    function errorMessage(){
        const bugBox = document.querySelector('#errores')
    
        if (bugBox) {
            bugBox.remove()
        }
    
        const divTemplate = document.createElement('div')
        divTemplate.setAttribute('id', 'errores')
        divTemplate.style = "background:rgba(255, 0, 0, 0.3);padding:.5em 1em;color: white;margin-top: 1em;"
        divTemplate.innerHTML += `<p><small>You must complete data correctly, without leaving empty fields or spaces</small></p>`
        form.appendChild(divTemplate)
    }

    

})
=======
window.addEventListener('load', function () {

    const form = document.querySelector('form')
    const email = document.querySelector('#inputEmail')
    const password = document.querySelector('#inputPassword')
    const endpointLogin = 'http://localhost:8082/user'


    form.addEventListener('submit', function (event) {
        event.preventDefault()

        const payload = {
            email: email.value,
            password: password.value
        }

        const config = {
            method: 'POST',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
            body: JSON.stringify(payload),   
        }

        loginUser(config)
        form.reset()

    })


    function loginUser(config) {

        fetch(endpointLogin, config)
        .then((response) => response.json())
        .then((data) => {
            console.log("Promise OK!");
            console.log(data);

            //if (data.jwt) {
            //    localStorage.setItem('jwt', JSON.stringify(data.jwt));

                if (data.admin) {
                    location.replace('./admin.html')
                }
                location.replace('./tasks.html')
            //}

        }).catch(err => {
            console.log("Promise rejected...");
            console.log(err);      
        })
    }

    

});
>>>>>>> main
