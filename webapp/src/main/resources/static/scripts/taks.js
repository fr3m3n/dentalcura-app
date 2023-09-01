// if (!localStorage.jwt) {
//   location.replace('./index.html');
// }


window.addEventListener('load', function () {
  /* ------------------------- AOS lib. init -------------------------------- */
  AOS.init();

  // const token = JSON.parse(localStorage.jwt);

  const endpointDate = './json/turno.json'
  const endpointDentist = 'http://localhost:8080/dentist'
  const endpointPatient = 'http://localhost:8080/patient'
  const endpointAppointment = 'http://localhost:8080/appointment'  

  const formAddAppointment = document.querySelector('.new-appointment')
  const btnCloseApp = document.querySelector('#closeApp')

  getUserName()
  getAppointment()
  


  function renderPatient(){
    const selectPatient = document.getElementById('select-patient')

    fetch(endpointPatient)
      .then(response=>response.json())
      .then(data=>{
//        console.log(data);
        data.forEach(patient=>{
          selectPatient.innerHTML+=`
          <div><input type="radio" name="patient" id="${patient.id}">
          <label for="${patient.id}">${patient.name} ${patient.surname}</label>
          </div>`
        })
      })
  }
  renderPatient()


  function renderDentist(){
    const selectDentist = document.getElementById('select-dentist')

    fetch(endpointDentist)
      .then(response=>response.json())
      .then(data=>{
        console.log(data);
        data.forEach(dentist=>{
          selectDentist.innerHTML+=`
          <div><input type="radio" name="dentist" id="${dentist.id}">
          <label for="${dentist.id}">${dentist.name} ${dentist.surname}</label>
          </div>`
        })
      })
  }
  renderDentist()


  function renderDate(){
    const selectDate = document.getElementById('select-date')

    fetch(endpointDate)
      .then(response=>response.json())
      .then(data=>{
        console.log(data);
        data.forEach(date=>{
          selectDate.innerHTML+=`
          <div><input type="radio" name="date" id="${date.fecha_hora}">
          <label for="${date.fecha_hora}">${date.fecha_hora}</label>
          </div>`
        })
      })
  }
  renderDate()

    /* -------------------------------------- */
    /*           [1] FUNCTION: Logout         */
    /* -------------------------------------- */

  btnCloseApp.addEventListener('click', function () {

    Swal.fire({
      title: 'Are you leaving?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        localStorage.clear();
        location.replace('./index.html');
      }
    });

  });

  /* ------------------------------------------------------------------ */
  /*                 [2] FUNCTION: Get user name [GET]                */
  /* ------------------------------------------------------------------ */

  function getUserName() {

    const name = sessionStorage.getItem('userName')

    const userName = document.querySelector('.user-info p')

    userName.innerText = JSON.parse(name)

  //   const settings = {
  //     method: 'GET',
  //     headers: {
  //       authorization: token
  //     }
  //   };

  //   fetch(endpointGetUser, settings)
  //     .then(response => response.json())
  //     .then(data => {
  //       const usrName = document.querySelector('.user-info p');
  //       usrName.innerText = data.firstName;
  //     })
  //     .catch(error => console.log(error));
  }


  /* ------------------------------------------------------------------- */
  /*                    [3] FUNCTION: Add Appointment [POST]                    */
  /* ------------------------------------------------------------------- */

  formAddAppointment.addEventListener('submit', function (event) {
    event.preventDefault()

    const dentist = document.querySelectorAll('[name=dentist]')
    const patient = document.querySelectorAll('[name=patient]')
    const date = document.querySelectorAll('[name=date]')

    const payload = {
      date: "",
      patient: {id:""}, 
      dentist: {id:""} 
    }

    dentist.forEach(dentist=>{
      if(dentist.checked){
        payload.dentist.id = dentist.id
      }
    })

    patient.forEach(patient=>{
      if(patient.checked){
        payload.patient.id = patient.id
      }
    })

    date.forEach(date=>{
      if(date.checked){
        console.log(date)
        payload.date = date.id
      }
    })

    console.log(payload)


    const settings = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    }

    fetch(endpointAppointment, settings)
      .then(response => response.json())
      .then(response => {
        console.log(response.status)
        getAppointment()
      })
      .catch(error => console.log(error))

    formAddAppointment.reset()
  })


  /* ----------------------------------------------------------- */
  /*                  [4] FUNCTION: Render Appointment           */
  /* ----------------------------------------------------------- */

  function getAppointment(){
    fetch(endpointAppointment)
    .then(response=>response.json())
    .then(data=>{
      console.log(data)
      renderAppointment(data)
      searchAppointmentId()
    })
  }

  function renderAppointment(list){

    const appointmentToDo = document.querySelector('#appointment-list')
    const patient = document.getElementById('patientLabel')
    const dentist = document.getElementById('dentistLabel')

    appointmentToDo.innerHTML = ""
    patient.innerText = "/"
    dentist.innerText = "/"    

      list.forEach(appointment=>{
        appointmentToDo.innerHTML+=`
        <li>Date: ${appointment.date} Dentist: ${appointment.dentist.name} ${appointment.dentist.surname}, Patient: ${appointment.patient.name} ${appointment.patient.surname}
        </li>`
      })
  }



  /* -------------------------------------------------------------------------- */
  /*                     [5] FUNCTION: Search appointment [GET]                 */
  /* -------------------------------------------------------------------------- */

  function searchAppointmentId(){

    const searchForm = document.querySelector('.appointment-search')
    const patient = document.getElementById('patientLabel')
    const dentist = document.getElementById('dentistLabel')
    const date = document.getElementById('inputDate')

    const searchAppointmentId = document.getElementById('inputAppointmentId')


    searchForm.addEventListener('submit', function(e){
      e.preventDefault()

      const id = searchAppointmentId.value
      const url = `${endpointAppointment}/${id}`

      const settings = {
        method: 'GET',
      }

      fetch(url, settings)
      .then(response=>response.json())
      .then(data=>{
        console.log(data)
            patient.innerText = data.patient.surname + (', ') + data.patient.name
            dentist.innerText = data.dentist.surname + (', ') + data.dentist.name
            date.value = data.date
            updateAppointment()
            appointmentDelete()
      })
    })
  }


  /* -------------------------------------------------------------------------- */
  /*                     [6] FUNCTION: Update appointment [PUT]                 */
  /* -------------------------------------------------------------------------- */


  function updateAppointment(){

    const updateform = document.querySelector('.appointment-update')
    const searchForm = document.querySelector('.appointment-search')

    const updateButton = document.querySelector('.update-button')
    const appointmentId = document.getElementById('inputAppointmentId')

    const date = document.getElementById('inputDate')


    updateButton.addEventListener('click', function(e){
      e.preventDefault()

      const id = appointmentId.value
      const url = `${endpointAppointment}/${id}`

      console.log(id)

      const payload = {
        date: date.value
      }

      const settings = {
        method: 'PUT',
        headers: {
          "Content-type": "application/json"
        },
        body: JSON.stringify(payload)
      }

      fetch(url, settings)
      .then(response => {
        console.log(response.status)
        getAppointment()
      })
      updateform.reset()
      searchForm.reset()
    })
  }


  /* -------------------------------------------------------------------------- */
  /*                     [6] FUNCTION: Delete appointment [DELETE]              */
  /* -------------------------------------------------------------------------- */

  function appointmentDelete() {

    const updateform = document.querySelector('.appointment-update')
    const searchForm = document.querySelector('.appointment-search')

    const deleteButton = document.querySelector('#delete-button')
    const appointmentId = document.getElementById('inputAppointmentId')


      deleteButton.addEventListener('click', function (e){
          e.preventDefault()

          const id = appointmentId.value
          const url = `${endpointAppointment}/${id}`
    
          console.log(id)

          const settings = {
            method: 'DELETE'
          }

          fetch(url, settings)
          .then(response => {
            console.log(response.status);
            getAppointment()
          })
          updateform.reset()
          searchForm.reset()
      })
  }

})