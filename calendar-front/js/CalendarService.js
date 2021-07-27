export class CalendarService {

  static URL = "http://localhost:8080/agenda";


  fetchAllScheduledDateByDate(strDate, successHandler, errorHandler) {
    console.log(strDate);
    const request = fetch(`${CalendarService.URL}/${strDate}`);
    request.then(response => {
      if(response.ok) {
        return response.json();
      }
      throw new Error("Error fetching data");
    })
    .then(data => successHandler(data))
    .catch(err => errorHandler(err));
  }


  fetchAllScheduledDates(successHandler, errorHandler) {
    const request = fetch(CalendarService.URL);
    request.then(response => {
      if(response.ok) {
        return response.json()
      }
      throw new Error("Error fetching scheduled dates");
    })
    .then(data => successHandler(data))
    .catch(err => errorHandler(err));
  }


  sendScheduledDate(dataToBeSent, successHandler, errorHandler) {
    fetch(CalendarService.URL, {
      method: 'POST',
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(dataToBeSent)
    }).then((r) => {
      if(r.ok) {
        successHandler();
      }
    }).catch((r) => {
      errorHandler();
    })
  }


  deleteScheduledDate(scheduleId, successHandler, errorHandler) {
    const request = fetch(`${CalendarService.URL}/${scheduleId}`, {
      method: 'DELETE'
    });
    request.then(response => {
      if(response.ok) {
        successHandler();
      } else {
        throw new Error("Error deleting scheduled date");
      }
    }).catch(response => errorHandler(response));
  }

}
